package com.alxdthn.tfsboards.ui.board.fragment

import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.utilities.extensions.showToastBy
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_board.*

class BoardEventObserver {

	fun bind(main: BoardFragment) {
		onError(main)
		onNewToast(main)
		onCardAdded(main)
		onCardDeleted(main)
		onColumnDeleted(main)
		onColumnRendered(main)
	}

	private fun onNewToast(main: BoardFragment) = main.apply { 
		viewModel.newToast.subscribe { stringRes ->
			showToastBy(stringRes)
		}.addTo(compositeDisposable)
	}

	private fun onColumnDeleted(main: BoardFragment) = main.apply {
		viewModel.columnDeleted.subscribe { columnIndex ->
			bvBoardView.removeColumn(columnIndex)
		}.addTo(compositeDisposable)
	}

	private fun onCardDeleted(main: BoardFragment) = main.apply {
		viewModel.cardDeleted.subscribe { cardIndex ->
			selectedAdapter?.removeItem(cardIndex)
		}.addTo(compositeDisposable)
	}

	private fun onCardAdded(main: BoardFragment) = main.apply {
		viewModel.cardAdded.subscribe { cardWithIndex ->
			selectedAdapter?.addItem(
				cardWithIndex.second,
				CardItem().render(cardWithIndex.first)
			)
			selectedEditText?.setText("")
		}.addTo(compositeDisposable)
	}

	private fun onError(main: BoardFragment) = main.apply {
		viewModel.errorCode.subscribe { error ->
			mainActivity.handleError(error)
		}.addTo(compositeDisposable)
	}

	private fun onColumnRendered(main: BoardFragment) = main.apply {
		itemsHandler.renderedColumn.subscribe { columnItem ->
			bvBoardView.initColumnView(columnItem, this)
		}.addTo(compositeDisposable)
	}
}