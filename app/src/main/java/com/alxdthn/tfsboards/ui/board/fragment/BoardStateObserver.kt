package com.alxdthn.tfsboards.ui.board.fragment

import android.content.res.Resources
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.ui.board.fragment.adapter.ColumnAdapter
import com.alxdthn.tfsboards.utilities.AppAnimator
import com.alxdthn.tfsboards.utilities.AppColors
import com.alxdthn.tfsboards.utilities.extensions.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.layout_board_add_column_item.*
import kotlinx.android.synthetic.main.layout_board_add_column_item.view.*
import kotlinx.android.synthetic.main.layout_board_column_header.view.*
import kotlinx.android.synthetic.main.layout_board_toolbar.*

class BoardStateObserver {

	fun bind(main: BoardFragment) {
		onEdit(main)
		onFilter(main)
		canAccept(main)
		onAddNewCard(main)
		onAddNewColumn(main)
		onEditColumnName(main)
		onDownloadingBoard(main)
	}

	private fun onFilter(main: BoardFragment) = main.apply {
		viewModel.filter.observe(viewLifecycleOwner, Observer { onFilter ->
			val color = viewModel.getBoardData().getColors().darkValue
			val statusBarColor = if (onFilter) AppColors.getDarkColor(color, 0.7f) else color

			AppAnimator.animateStatusBarColor(mainActivity, statusBarColor)
			btnBoardToolbarFilter.isEnabled = !onFilter
			if (onFilter) AppAnimator hide bvBoardView else AppAnimator show bvBoardView
			bvBoardView invisibleIf onFilter
		})
	}

	private fun canAccept(main: BoardFragment) = main.apply {
		viewModel.isCanAccept.observe(viewLifecycleOwner, Observer { isCanAccept ->
			btnBoardToolbarAcceptInput.isEnabled = isCanAccept
		})
	}

	private fun onDownloadingBoard(main: BoardFragment) = main.apply {
		viewModel.downloadingBoard.observe(viewLifecycleOwner, Observer { downloading ->
			if (!viewModel.filter.isActive() && !downloading) {
				AppAnimator show bvBoardView
			}
			pbBoardProgressBar hereIf downloading
		})
	}

	private fun onEditColumnName(main: BoardFragment) = main.apply {
		viewModel.editColumnName.observe(viewLifecycleOwner, Observer { onEditName ->
			if (onEditName) {
				val header = bvBoardView.getHeaderView(viewModel.getSelectedColumnIndex())
				val input = header.edxColumnHeaderInput
				val nameView = header.txvColumnHeaderName

				mainActivity.softInputAdjustNothing()
				selectedColumnHeader = header
				input.visible()
				nameView.gone()
				bvBoardView.scrollToColumn(viewModel.getSelectedColumnIndex(), false)
				selectedEditText = input
			} else {
				selectedEditText = null
				selectedColumnHeader?.edxColumnHeaderInput?.gone()
				selectedColumnHeader?.txvColumnHeaderName?.visible()
			}
		})
	}

	private fun onAddNewColumn(main: BoardFragment) = main.apply {
		viewModel.addNewColumn.observe(viewLifecycleOwner, Observer { onAddNewColumn ->
			btnAddColumn goneIf onAddNewColumn
			edxAddColumnInput hereIf onAddNewColumn
			selectedEditText = if (onAddNewColumn) {
				mainActivity.softInputAdjustNothing()
				bvBoardView.scrollToColumn(bvBoardView.columnCount - 1, true)
				edxAddColumnInput
			} else null
		})
	}

	private fun onEdit(main: BoardFragment) = main.apply {
		viewModel.onEdit.observe(viewLifecycleOwner, Observer { onEdit ->
			btnBoardToolbarBack invisibleIf onEdit
			btnBoardToolbarFilter invisibleIf onEdit

			btnBoardToolbarCancelInput visibleIf onEdit
			btnBoardToolbarAcceptInput visibleIf onEdit
			bvBoardView setPaddingBottom getPadding(onEdit, resources)
		})
	}

	private fun getPadding(onEdit: Boolean, resources: Resources): Int {
		return if (onEdit) {
			resources.getDimensionPixelOffset(R.dimen.on_edit_padding)
		} else {
			resources.getDimensionPixelOffset(R.dimen.normal_padding)
		}
	}

	private fun onAddNewCard(main: BoardFragment) = main.apply {
		viewModel.addNewCard.observe(viewLifecycleOwner, Observer { onAddNewCard ->
			if (onAddNewCard) {
				mainActivity.softInputAdjustResize()

				selectedRecycler = bvBoardView.getRecyclerView(viewModel.getSelectedColumnIndex())
				val parent = (selectedRecycler?.parent as ViewGroup)
				selectedAdapter = selectedRecycler?.adapter as ColumnAdapter
				selectedColumnHeader = parent.getChildAt(0)
				selectedColumnFooter = parent.getChildAt(2)
				selectedColumnFooter?.gone()

				selectedAdapter?.addItem(
					viewModel.getSelectedColumnSize(),
					viewModel.getBtnCardItem()
				)
				selectedRecycler?.scrollToPosition(viewModel.getSelectedColumnSize())
				bvBoardView.scrollToColumn(viewModel.getSelectedColumnIndex(), false)
			} else {
				Handler().post {
					selectedAdapter?.removeItem(viewModel.getSelectedColumnSize())
				}
				selectedColumnFooter?.visible()
			}
		})
	}
}