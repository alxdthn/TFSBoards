package com.alxdthn.tfsboards.ui.board.fragment

import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.utilities.extensions.showToastBy
import com.alxdthn.tfsboards.utilities.extensions.subscribe
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

	private fun onNewToast(main: BoardFragment) {
		main.subscribe({ viewModel.newToast }) { stringRes ->
			showToastBy(stringRes)
		}
	}

	private fun onColumnDeleted(main: BoardFragment) {
		main.subscribe({ viewModel.columnDeleted }) { columnIndex ->
			bvBoardView.removeColumn(columnIndex)
		}
	}

	private fun onCardDeleted(main: BoardFragment) {
		main.subscribe({ viewModel.cardDeleted }) { cardIndex ->
			selectedAdapter?.removeItem(cardIndex)
		}
	}

	private fun onCardAdded(main: BoardFragment) {
		main.subscribe({ viewModel.cardAdded }) { cardWithIndex ->
			selectedAdapter?.addItem(
				cardWithIndex.second,
				CardItem().render(cardWithIndex.first)
			)
			selectedEditText?.setText("")
		}
	}

	private fun onError(main: BoardFragment) {
		main.subscribe({ viewModel.errorCode} ) { error ->
			mainActivity.handleError(error)
		}
	}

	private fun onColumnRendered(main: BoardFragment) {
		main.subscribe({ itemsHandler.renderedColumn }) { columnItem ->
			bvBoardView.initColumnView(columnItem, this)
		}
	}
}