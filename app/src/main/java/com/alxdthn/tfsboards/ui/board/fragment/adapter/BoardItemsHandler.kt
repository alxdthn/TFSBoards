package com.alxdthn.tfsboards.ui.board.fragment.adapter

import com.alxdthn.tfsboards.model.BtnCardItem
import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.model.ColumnItem
import com.alxdthn.tfsboards.ui.board.fragment.BoardViewHandler
import com.alxdthn.tfsboards.ui.board.view_model.BoardViewModel
import io.reactivex.subjects.PublishSubject

class BoardItemsHandler(private val viewModel: BoardViewModel) : BoardViewHandler {

	val renderedColumn = PublishSubject.create<ColumnItem>()

	private val itemColumns = mutableListOf<ColumnItem>()

	fun renderColumns() {
		val boardColumns = viewModel.getBoardData().columns

		itemColumns.clear()
		boardColumns.forEach {
			val column = ColumnItem() render it

			it.cards.forEach { card ->
				if (!card.removed) {
					column.items.add(CardItem().render(card))
				}
			}
			itemColumns.add(column)
			renderedColumn.onNext(column)
		}
		itemColumns.add(ColumnItem(BoardViewModel.COLUMN_BUTTON_ID))
	}

	override fun onItemDragEnded(fromColumn: Int, fromRow: Int, toColumn: Int, toRow: Int) {
		if (fromColumn != toColumn || fromRow != toRow) {
			viewModel.handleCardMove(fromColumn, fromRow, toColumn, toRow)
		}
	}

	override fun onItemDragStarted(column: Int, row: Int) {

	}

	override fun canDragItemAtPosition(column: Int, dragPosition: Int): Boolean {
		if (viewModel.onEdit.isActive() || viewModel.process.isActive()) return false
		return try {
			itemColumns[column].items[dragPosition] is CardItem
		} catch (e: IndexOutOfBoundsException) {
			false
		}
	}

	override fun canDropItemAtPosition(p0: Int, p1: Int, newColumn: Int, newRow: Int): Boolean {
		if (itemColumns[newColumn].id == BoardViewModel.COLUMN_BUTTON_ID) return false
		if (itemColumns[newColumn].items.size < newRow
			&& itemColumns[newColumn].items[newRow] is BtnCardItem
		) return false
		return true
	}
}