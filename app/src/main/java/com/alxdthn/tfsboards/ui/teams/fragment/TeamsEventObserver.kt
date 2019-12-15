package com.alxdthn.tfsboards.ui.teams.fragment

import androidx.core.content.ContextCompat
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.utilities.AppAnimator
import com.alxdthn.tfsboards.utilities.extensions.subscribe
import com.alxdthn.tfsboards.utilities.extensions.subscribeToGlobal
import kotlinx.android.synthetic.main.layout_teams_content.*

class TeamsEventObserver {

	fun bind(main: TeamsFragment) {
		onError(main)
		onBoardClose(main)
		onBoardSwipe(main)
		onRemoveBoard(main)
		onBoardMoving(main)
		onBoardClicked(main)
		onBoardRemoved(main)
		onBoardInserted(main)
	}

	private fun onBoardClose(main: TeamsFragment) {
		main.subscribeToGlobal { event ->
			if (event == Events.BOARD_CLOSE) {
				val color = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
				AppAnimator.animateStatusBarColor(mainActivity, color)
			}
		}
	}

	private fun onBoardSwipe(main: TeamsFragment) {
		main.subscribe({ itemsHandler.onBoardSwipe }, { idBoard ->
			viewModel.startRemoveDialog(idBoard)
		})
	}

	private fun onBoardClicked(main: TeamsFragment) {
		main.subscribe({ itemsHandler.onBoardClick }, { idBoard ->
			viewModel.showBoard(idBoard)
		})
	}

	private fun onRemoveBoard(main: TeamsFragment) {
		main.subscribe({ viewModel.removingBoard }, { board ->
			showRemoveDialog(board)
		})
	}

	private fun onBoardRemoved(main: TeamsFragment) {
		main.subscribe({ viewModel.boardRemoved }, { boards ->
			itemsHandler render boards
		})
	}

	private fun onBoardInserted(main: TeamsFragment) {
		main.subscribe({ viewModel.boardInserted }, { boards ->
			itemsHandler render boards
		})
	}

	private fun onError(main: TeamsFragment) {
		main.subscribe({ viewModel.errorCode }, { error ->
			mainActivity.handleError(error)
		})
	}

	private fun onBoardMoving(main: TeamsFragment) {
		main.subscribe({ itemsHandler.onBoardMove }, { isMoving ->
			srlRefreshBoardsList.isEnabled = !isMoving
		})
	}
}