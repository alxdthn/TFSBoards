package com.alxdthn.tfsboards.ui.card.view_model

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.LocalState
import com.alxdthn.tfsboards.base.ViewModelInputHandler
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.local.Action
import com.alxdthn.tfsboards.model.local.Attachment
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.repositories.AppResources
import com.alxdthn.tfsboards.repositories.CardRepository
import com.alxdthn.tfsboards.ui.members.MembersViewModel
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_MEMBERS_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_NAME_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.COLOR_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.COLUMN_NAME_KEY
import com.alxdthn.tfsboards.utilities.extensions.removeFirstIf
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class CardViewModel @Inject constructor(
	private val repository: CardRepository,
	private val resources: AppResources
) : ViewModelInputHandler() {

	val editName = LocalState()
	val editDescription = LocalState()
	val updatingCard = LocalState()
	val downloadingActions = LocalState().withLog("AC")
	val attachmentsOpen = LocalState()
	val actionsOpen = LocalState()

	private val _cardData = MutableLiveData<Card>()
	val cardData: LiveData<Card> = _cardData

	private val _position = MutableLiveData("?")
	val position: LiveData<String> = _position

	private val _toolbarColor = MutableLiveData(0)
	val toolbarColor: LiveData<Int> = _toolbarColor

	lateinit var boardMembers: List<User>
	private var tmpCard = Card()

	val openAttachment = PublishSubject.create<Attachment>()
	val actionsDownloaded = PublishSubject.create<List<Action>>()
	val membersChanged = PublishSubject.create<List<User>>()
	val attachmentsChanged = PublishSubject.create<List<Attachment>>()

	init {
		GlobalEvent.subscribe { event ->
			when (event) {
				Events.MEMBERS_CLOSE -> updateMembers()
				else -> Unit
			}
		}
	}

	override fun initializer() {
		val args = GlobalEvent.args()

		boardMembers = args[BOARD_MEMBERS_KEY] as List<User>
		_toolbarColor.value = args[COLOR_KEY] as Int
		_cardData.value = args[CARD_KEY] as Card
		_position.value = resources.getString(
			R.string.card_position,
			args[BOARD_NAME_KEY] as String,
			args[COLUMN_NAME_KEY] as String
		)
		initCardMembers(cardData.value!!)
		actionsOpen.set(getCardData().actionsOpen)
		attachmentsOpen.set(getCardData().attachmentsOpen)
	}

	private fun initCardMembers(card: Card) {
		if (card.members == null) {
			card.members = boardMembers.filter { card.idMembers.contains(it.id) }
		}
	}

	override fun getStartTextForInput(): String {
		return when {
			getBufferedInput().isNotEmpty() -> getBufferedInput()
			editDescription.isActive() -> getCardData().desc
			editName.isActive() -> getCardData().name
			else -> ""
		}
	}

	override fun acceptInput() {
		if (editDescription.isActive() || isCanAccept.isActive()) {
			prepareTmpCard()
			when {
				editDescription.isActive() -> tmpCard.desc = getBufferedInput()
				editName.isActive() -> tmpCard.name = getBufferedInput()
			}
			updateCard()
		}
	}

	override fun cancelInput() {
		super.cancelInput()

		when {
			editDescription.isActive() -> editDescription.cancel()
			editName.isActive() -> editName.cancel()
		}
	}

	fun showActions() {
		getCardData().actionsOpen = !actionsOpen.isActive()
		if (getCardData().actions == null) {
			downloadCardActions()
		} else {
			actionsOpen.toggle()
		}
	}

	fun showAttachments() {
		getCardData().attachmentsOpen = !attachmentsOpen.isActive()
		attachmentsOpen.toggle()
	}

	fun downloadCard() {
		updatingCard.start()
		repository.getCard(getCardData())
			.subscribe({
				invalidate()
				updatingCard.cancel()
			}, { error ->
				handleError(error)
			}).addTo(compositeDisposable)
	}

	private fun downloadCardActions() {
		downloadingActions.start()
		repository.getCardActions(getCardData())
			.subscribe({ actions ->
				actionsDownloaded.onNext(actions)
			}, { error ->
				handleError(error)
			}).addTo(compositeDisposable)
	}

	private fun updateCardMembers() {
		repository.updateCardMembers(tmpCard)
			.map {
				applyCardData()
				boardMembers.filter { tmpCard.idMembers.contains(it.id) }
			}
			.subscribe({ cardMembers ->
				membersChanged.onNext(cardMembers)
			}, { error ->
				handleError(error)
			}).addTo(compositeDisposable)
	}

	fun downloadAttachment(idAttachment: String?) {
		if (idAttachment == null) return
		val attachment = getCardData().attachments.find { it.id == idAttachment }
			?: throw java.lang.IllegalArgumentException()
		val request = DownloadManager.Request(Uri.parse(attachment.url))
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachment.name)
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
		resources.getDownloadManager().enqueue(request)
	}

	fun removeAttachment(idAttachment: String?) {
		if (idAttachment == null) return
		repository.deleteAttachment(getCardData().id, idAttachment)
			.subscribe({
				val attachments = getCardData().attachments
				attachments.removeFirstIf { it.id == idAttachment }
				attachmentsChanged.onNext(attachments)
			}) { error ->
				handleError(error)
			}.addTo(compositeDisposable)
	}

	private fun updateCard() {
		repository.updateCard(tmpCard)
			.subscribe({
				cancelInput()
				applyCardData()
				invalidate()
			}, { error ->
				cancelInput()
				handleError(error)
			}).addTo(compositeDisposable)
	}

	fun updateMembers() {
		prepareTmpCard()
		updateCardMembers()
	}

	private fun applyCardData() {
		getCardData().apply {
			name = tmpCard.name
			desc = tmpCard.desc
			members = tmpCard.members
			idMembers = tmpCard.idMembers
		}
	}

	private fun prepareTmpCard() {
		tmpCard = getCardData().copy()
	}

	fun startDescriptionEdit() {
		onEdit.start()
		editDescription.start()
	}

	fun startNameEdit() {
		onEdit.start()
		editName.start()
	}

	private fun invalidate() {
		_cardData.value = _cardData.value
	}

	fun getCardData(): Card {
		return cardData.value ?: throw IllegalStateException("cardData value is empty")
	}

	fun showAttachment(id: String?) {
		if (id != null) {
			val attachment = getCardData().attachments.find { it.id == id }
				?: throw IllegalArgumentException()

			openAttachment.onNext(attachment)
		}
	}

	fun deleteCard() {
		GlobalEvent.set(Events.CARD_DELETE)
		GlobalEvent.set(Events.CARD_CLOSE)
	}

	fun showMembers() {
		MembersViewModel.start(getCardData(), boardMembers)
	}

	override fun onCleared() {
		super.onCleared()

		GlobalEvent.set(Events.CARD_CLOSE)
	}

	companion object {
		fun start(
			card: Card,
			boardMembers: List<User>,
			boardName: String,
			columnName: String,
			toolbarColor: Int
		) {
			val args = mapOf(
				Pair(CARD_KEY, card),
				Pair(BOARD_NAME_KEY, boardName),
				Pair(COLUMN_NAME_KEY, columnName),
				Pair(BOARD_MEMBERS_KEY, boardMembers),
				Pair(COLOR_KEY, toolbarColor)
			)

			GlobalEvent.set(Events.CARD, args)
		}
	}
}

