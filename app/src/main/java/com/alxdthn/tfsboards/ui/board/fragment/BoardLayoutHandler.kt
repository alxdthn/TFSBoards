package com.alxdthn.tfsboards.ui.board.fragment

import android.view.View
import com.alxdthn.tfsboards.utilities.extensions.gone
import com.alxdthn.tfsboards.utilities.extensions.visible

class BoardLayoutHandler(
	private val main: BoardFragment
) : View.OnLayoutChangeListener{

	private var headerHided = false

	private var currentHeight = 0
		set(value) {
			if (main.selectedColumnHeader != null) {
				layoutResizeHandle(value)
			}
			field = value
		}

	private fun layoutResizeHandle(value: Int) {
		val onAddNewCard = main.viewModel.addNewCard.isActive()

		main.apply {
			if (onAddNewCard && value < 400) {
				selectedColumnHeader?.gone()
				selectedRecycler?.requestLayout()
				headerHided = true
			} else if (headerHided && value > 400) {
				selectedColumnHeader?.visible()
				selectedRecycler?.requestLayout()
				headerHided = false
			}
			if (onAddNewCard) {
				selectedRecycler?.scrollToPosition(viewModel.getSelectedColumnSize())
			}
		}
	}

	override fun onLayoutChange(
		v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int,
		oldTop: Int, oldRight: Int, oldBottom: Int
	) {
		val oldHeight = oldBottom - oldTop

		if (v.height != oldHeight) {
			currentHeight = v.height
		}
	}
}