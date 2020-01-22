package com.alxdthn.tfsboards.ui.teams.fragment

import androidx.core.content.ContextCompat
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.utilities.AppAnimator
import io.reactivex.rxkotlin.addTo
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

	private fun onBoardClose(main: TeamsFragment) = main.apply {
		GlobalEvent.subscribe { event ->
			if (event == Events.BOARD_CLOSE) {
				val color = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
				AppAnimator.animateStatusBarColor(mainActivity, color)
			}
		}
	}

	private fun onBoardSwipe(main: TeamsFragment) = main.apply {
		itemsHandler.onBoardSwipe.subscribe { idBoard ->
			viewModel.startRemoveDialog(idBoard)
		}.addTo(compositeDisposable)
	}

	private fun onBoardClicked(main: TeamsFragment) = main.apply {
		itemsHandler.onBoardClick.subscribe { idBoard ->
			viewModel.showBoard(idBoard)
		}.addTo(compositeDisposable)
	}

	private fun onRemoveBoard(main: TeamsFragment) = main.apply {
		viewModel.removingBoard.subscribe { board ->
			showRemoveDialog(board)
		}.addTo(compositeDisposable)
	}

	private fun onBoardRemoved(main: TeamsFragment) = main.apply {
		viewModel.boardRemoved.subscribe { boards ->
			itemsHandler render boards
		}.addTo(compositeDisposable)
	}

	private fun onBoardInserted(main: TeamsFragment) = main.apply {
		viewModel.boardInserted.subscribe { boards ->
			itemsHandler render boards
		}.addTo(compositeDisposable)
	}

	private fun onError(main: TeamsFragment) = main.apply {
		viewModel.errorCode.subscribe { error ->
			mainActivity.handleError(error)
		}.addTo(compositeDisposable)
	}

	private fun onBoardMoving(main: TeamsFragment) = main.apply {
		itemsHandler.onBoardMove.subscribe { isMoving ->
			srlRefreshBoardsList.isEnabled = !isMoving
		}.addTo(compositeDisposable)
	}
}