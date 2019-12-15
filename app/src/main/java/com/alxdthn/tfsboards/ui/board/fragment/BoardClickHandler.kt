package com.alxdthn.tfsboards.ui.board.fragment

import android.view.View
import android.view.ViewGroup
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.ui.board.view_model.BoardViewModel
import com.alxdthn.tfsboards.ui.dialogs.RemoveItemDialogFragment
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.DELETE_COLUMN_REQUEST
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.layout_board_toolbar.*

class BoardClickHandler(
	private val main: BoardFragment
) : View.OnClickListener, View.OnLongClickListener {

	private val viewModel: BoardViewModel = main.viewModel

	init {
		initListeners()
	}

	private fun initListeners() {
		main.apply {
			btnBoardToolbarBack.setOnClickListener(this@BoardClickHandler)
			btnBoardToolbarCancelInput.setOnClickListener(this@BoardClickHandler)
			btnBoardToolbarAcceptInput.setOnClickListener(this@BoardClickHandler)
			btnBoardToolbarFilter.setOnClickListener(this@BoardClickHandler)
		}
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.btnAddCard -> addNewCard(v)
			R.id.btnAddColumn -> addNewColumn()
			R.id.flColumnHeader -> editColumnName(v)
			R.id.btnBoardToolbarFilter -> viewModel.showFilterFragment()
			R.id.btnBoardToolbarBack -> GlobalEvent.set(Events.CLOSE)
			R.id.btnBoardToolbarAcceptInput -> viewModel.acceptInput()
			R.id.btnBoardToolbarCancelInput -> viewModel.cancelInput()
		}
	}

	override fun onLongClick(v: View): Boolean {
		return when (v.id) {
			R.id.flColumnHeader -> removeColumn(v)
			else -> false
		}
	}

	/**
	 *		Click handlers
	 */

	fun onItemClick(cardItem: CardItem) {
		viewModel.showCard(cardItem.id)
	}

	private fun removeColumn(header: View): Boolean {
		if (stateValid()) {
			val index = main.bvBoardView.getColumnOfHeader(header)
			val column = viewModel.getColumn(index)
			val question = main.getString(R.string.column_question)
			val message = main.getString(R.string.remove_dialog_message, question, column.name)

			viewModel.selectColumn(column)
			RemoveItemDialogFragment.show(
				main.childFragmentManager, message, column.id, DELETE_COLUMN_REQUEST, BOARD_FRAGMENT)
			return true
		}
		return false
	}

	private fun addNewCard(footer: View) {
		if (stateValid()) {
			val header = (footer.parent as ViewGroup).getChildAt(0)
			val columnIndex = main.bvBoardView.getColumnOfHeader(header)

			viewModel.startAddNewCardState(columnIndex)
		}
	}

	private fun addNewColumn() {
		if (stateValid()) {
			viewModel.startAddNewColumnState()
		}
	}

	private fun editColumnName(header: View) {
		if (stateValid()) {
			val columnIndex = main.bvBoardView.getColumnOfHeader(header)

			viewModel.startEditColumnState(columnIndex)
		}
	}

	private fun stateValid(): Boolean {
		return !viewModel.onEdit.isActive() && !viewModel.process.isActive()
	}
}