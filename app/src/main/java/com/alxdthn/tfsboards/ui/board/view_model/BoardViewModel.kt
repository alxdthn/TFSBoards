package com.alxdthn.tfsboards.ui.board.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.LocalState
import com.alxdthn.tfsboards.base.ViewModelInputHandler
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.BtnCardItem
import com.alxdthn.tfsboards.model.local.Board
import com.alxdthn.tfsboards.model.local.BoardData
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.model.local.Column
import com.alxdthn.tfsboards.repositories.AppResources
import com.alxdthn.tfsboards.repositories.BoardRepository
import com.alxdthn.tfsboards.repositories.CardRepository
import com.alxdthn.tfsboards.repositories.ListRepository
import com.alxdthn.tfsboards.ui.card.view_model.CardViewModel
import com.alxdthn.tfsboards.ui.filter.FilterViewModel
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_KEY
import com.alxdthn.tfsboards.utilities.extensions.subscribeToGlobal
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BoardViewModel @Inject constructor(
	private val boardRepository: BoardRepository,
	private val listRepository: ListRepository,
	private val cardRepository: CardRepository,
	private val appResources: AppResources
) : ViewModelInputHandler() {

	val downloadingBoard = LocalState()
	val addNewColumn = LocalState()
	val editColumnName = LocalState()
	val addNewCard = LocalState()
	val process = LocalState()
	val filter = LocalState()

	val cardAdded = PublishSubject.create<Pair<Card, Int>>()
	val cardDeleted = PublishSubject.create<Int>()
	val columnDeleted = PublishSubject.create<Int>()
	val newToast = PublishSubject.create<Int>()

	private val _boardData = MutableLiveData<BoardData>()
	val boardData: LiveData<BoardData> = _boardData

	private val _headerTitle = MutableLiveData<String>()
	val headerTitle: LiveData<String> = _headerTitle

	private var selectedColumn: Column? = null
	private var selectedCard: Card? = null
	private var cardBtn: BtnCardItem? = null

	private var downloaded = false
	var focusedColumn = 0

	init {
		subscribeToGlobal { event ->
			when (event) {
				Events.CARD_CLOSE -> invalidate()
				Events.CARD_DELETE -> deleteCard()
				Events.FILTER -> filter.start()
				Events.FILTER_CLOSE -> filter.cancel()
				Events.FILTER_SHOW_CARD -> {
					val args = GlobalEvent.args()
					showCard(args[CARD_KEY] as String)}
				else -> Unit
			}
		}
	}

	fun initBoard() {
		if (!downloaded) {
			downloadBoard()
		}
	}

	override fun initializer() {
		val boardData = BoardData()
		val board = GlobalEvent.args()[BOARD_KEY] as Board

		boardData.color = board.color
		boardData.id = board.id
		boardData.name = board.name
		boardData.backgroundImage = board.backgroundImage
		_boardData.value = boardData
	}

	/**
	 *		Rx Part
	 */
	private fun uploadColumn() {
		val newColumn = Column()

		process.start()
		newColumn.name = getBufferedInput()
		newColumn.idBoard = getBoardData().id
		listRepository.addNewColumn(newColumn)
			.subscribe({
				process.cancel()
				addLocalColumn(newColumn)
				invalidate()
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	private fun updateColumn() {
		val column = getSelectedColumn().copy()

		process.start()
		column.name = getBufferedInput()
		listRepository.updateColumn(column)
			.subscribe({
				process.cancel()
				getSelectedColumn().name = getBufferedInput()
				invalidate()
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	private fun uploadCard() {
		val newCard = Card()

		selectedCard = newCard
		process.start()
		newCard.name = getBufferedInput()
		newCard.idColumn = getSelectedColumn().id
		cardRepository.addNewCard(newCard)
			.subscribe({
				process.cancel()
				addLocalCard(newCard)
				cardAdded.onNext(Pair(newCard, getSelectedColumnSize() - 1))
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	fun deleteCard() {
		process.start()
		cardRepository.deleteCard(getSelectedCard().id)
			.subscribe({
				val index = getSelectedCardIndex()
				deleteLocalCard()
				cardDeleted.onNext(index)
				process.cancel()
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	fun archiveColumn() {
		process.start()
		listRepository.archiveColumn(getSelectedColumn().id, true)
			.subscribe({
				val index = getSelectedColumnIndex()
				process.cancel()
				deleteLocalColumn()
				columnDeleted.onNext(index)
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	private fun downloadBoard() {
		downloadingBoard.start()
		boardRepository.getBoard(getBoardData())
			.subscribe({
				invalidate()
				downloadingBoard.cancel()
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	fun handleCardMove(fromColumn: Int, fromRow: Int, toColumn: Int, toRow: Int) {
		process.start()

		val columns = getBoardData().columns
		val card = columns[fromColumn].cards[fromRow]
		val tmpCard = calculateCardPosition(fromColumn, fromRow, toColumn, toRow)

		cardRepository.updateCard(tmpCard)
			.subscribe({
				process.cancel()
				moveLocalCard(card, tmpCard, fromColumn, toColumn, toRow)
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	/**
	 * 		Tools
	 */

	//			Card manage
	fun startAddNewCardState(index: Int) {
		_headerTitle.value = appResources.getString(R.string.add_card_toolbar_title)
		cardBtn = BtnCardItem(COLUMN_BUTTON_ID)
		selectedColumn = getBoardData().columns[index]
		onEdit.start()
		addNewCard.start()
	}

	fun selectCard(idCard: String): Card {
		val columns = getBoardData().columns

		columns.forEach { column ->
			column.cards.forEach { card ->
				if (card.id == idCard) {
					selectedColumn = column
					selectedCard = card
					return card
				}
			}
		}
		throw IllegalStateException()
	}

	private fun addLocalCard(newCard: Card) {
		getSelectedColumn().cards.add(newCard)
	}

	private fun deleteLocalCard() {
		getSelectedColumn().cards.remove(getSelectedCard())
	}

	private fun moveLocalCard(
		card: Card,
		tmpCard: Card,
		fromColumn: Int,
		toColumn: Int,
		toRow: Int
	) {
		val columns = getBoardData().columns

		card.pos = tmpCard.pos
		card.idColumn = tmpCard.idColumn
		columns[fromColumn].cards.remove(card)
		columns[toColumn].cards.add(toRow, card)
		columns[toColumn].cards.sortBy { it.pos }
		process.cancel()
	}

	//			Column manage
	fun startAddNewColumnState() {
		_headerTitle.value = appResources.getString(R.string.add_column_toolbar_title)
		addNewColumn.start()
		onEdit.start()
	}

	fun startEditColumnState(index: Int) {
		_headerTitle.value = appResources.getString(R.string.edit_column_name_toolbar_title)
		selectedColumn = getBoardData().columns[index]
		editColumnName.start()
		onEdit.start()
	}

	fun selectColumn(column: Column) {
		selectedColumn = column
	}

	fun getColumn(columnIndex: Int): Column {
		return getBoardData().columns[columnIndex]
	}

	private fun addLocalColumn(newColumn: Column) {
		getBoardData().columns.add(newColumn)
	}

	private fun deleteLocalColumn() {
		getBoardData().columns.remove(getSelectedColumn())
	}

	fun showCard(idCard: String) {
		if (!onEdit.isActive() && !process.isActive()) {
			CardViewModel.start(
				card = selectCard(idCard),
				boardMembers = getBoardData().members,
				columnName = getSelectedColumn().name,
				boardName = getBoardData().name,
				toolbarColor = getBoardData().getColors().mediumValue
			)
		}
	}

	//			common
	override fun getStartTextForInput(): String {
		return if (editColumnName.isActive() && getBufferedInput().isEmpty()) {
			getSelectedColumn().name
		} else {
			getBufferedInput()
		}
	}

	override fun acceptInput() {
		if (process.isActive()) return
		if (isCanAccept.isActive()) {
			when {
				addNewColumn.isActive() -> uploadColumn()
				editColumnName.isActive() -> updateColumn()
				addNewCard.isActive() -> uploadCard()
			}
		} else {
			newToast.onNext(R.string.enter_name)
		}
	}

	override fun cancelInput() {
		super.cancelInput()
		_headerTitle.value = getBoardData().name
		when {
			addNewColumn.isActive() -> addNewColumn.cancel()
			editColumnName.isActive() -> editColumnName.cancel()
			addNewCard.isActive() -> addNewCard.cancel()
		}
	}

	private fun calculateCardPosition(
		fromColumn: Int,
		fromRow: Int,
		toColumn: Int,
		toRow: Int
	): Card {
		val columns = getBoardData().columns
		val card = columns[fromColumn].cards[fromRow]
		val tmpCard = card.copy()
		val tmpColumn = mutableListOf<Card>()

		columns[toColumn].cards.forEach {
			tmpColumn.add(it)
		}
		tmpColumn.remove(card)
		tmpColumn.add(toRow, card)

		val posAbove = if (toRow != 0) {
			tmpColumn[toRow - 1].pos
		} else 0f
		val posBelow = if (toRow < tmpColumn.size - 1) {
			tmpColumn[toRow + 1].pos
		} else null

		tmpCard.pos = if (posBelow != null) {
			(posBelow - posAbove) / 2 + posAbove
		} else {
			posAbove + columns[toColumn].pos
		}
		tmpCard.idColumn = columns[toColumn].id
		return tmpCard
	}

	fun saveFocus(focus: Int) {
		focusedColumn = focus
	}

	fun getSelectedCard(): Card {
		return selectedCard ?: throw IllegalArgumentException("Missing newCard value")
	}

	fun getSelectedCardIndex(): Int {
		val index = getSelectedColumn().cards.indexOf(getSelectedCard())

		return if (index >= 0) index else
			throw IllegalStateException("Missing _selectedCard in data")
	}

	fun getSelectedColumn(): Column {
		return selectedColumn
			?: throw IllegalArgumentException("Missing _selectedColumn value")
	}

	fun getBoardData(): BoardData {
		return _boardData.value ?: throw IllegalArgumentException("Missing boardData value")
	}

	fun getSelectedColumnIndex(): Int {
		val index = getBoardData().columns.indexOf(getSelectedColumn())

		return if (index >= 0) index else
			throw IllegalStateException("Missing _selectedColumn in data")
	}

	fun getSelectedColumnSize(): Int {
		return getSelectedColumn().cards.size
	}

	fun getBtnCardItem(): BtnCardItem {
		return cardBtn ?: throw IllegalStateException("Missing btnCardItem value")
	}

	fun invalidate() {
		_boardData.value = _boardData.value
	}

	fun showFilterFragment() {
		FilterViewModel.start(getBoardData())
	}

	override fun onCleared() {
		super.onCleared()

		GlobalEvent.set(Events.BOARD_CLOSE)
	}

	companion object {
		const val COLUMN_BUTTON_ID = "ff"

		fun start(board: Board) {
			val args = mapOf(
				Pair(BOARD_KEY, board)
			)

			GlobalEvent.set(Events.BOARD, args)
		}
	}
}