package com.alxdthn.tfsboards.ui.board.fragment

import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.utilities.AppAnimator
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.layout_board_toolbar.*

class BoardDataObserver {

	fun bind(main: BoardFragment) {
		observeBoardData(main)
		observeHeaderTitle(main)
	}

	private fun observeBoardData(main: BoardFragment) = main.apply {
		viewModel.boardData.observe(viewLifecycleOwner, Observer { board ->
			val colors = board.getColors()

			txvBoardToolbarTitle.text = board.name
			cvBoardToolbar.setCardBackgroundColor(colors.mediumValue)
			AppAnimator.animateStatusBarColor(mainActivity, colors.darkValue)
			loadImageBackground(ivBoardBackground, board.backgroundImage, colors.value)
			updateBoardView()
		})
	}

	private fun observeHeaderTitle(main: BoardFragment) = main.apply {
		viewModel.headerTitle.observe(viewLifecycleOwner, Observer { title ->
			txvBoardToolbarTitle.text = title
		})
	}
}