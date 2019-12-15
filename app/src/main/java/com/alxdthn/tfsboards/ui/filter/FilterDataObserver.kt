package com.alxdthn.tfsboards.ui.filter

import android.util.Log
import androidx.core.content.ContextCompat
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.utilities.extensions.observe
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterDataObserver {

	fun bind(main: FilterFragment) {
		observeColors(main)
		observeFilteredData(main)
	}

	private fun observeFilteredData(main: FilterFragment) {
		main.observe({ viewModel.filteredData }) { filteredData ->
			itemsHandler render filteredData
		}
	}

	private fun observeColors(main: FilterFragment) {
		main.observe({ viewModel.colors }) { colors ->
			val toolbarColor = colors.darkValue

			cvFilterFragmentToolbar.setCardBackgroundColor(toolbarColor)
			llFilterFragmentBackground.setBackgroundColor(
				ContextCompat.getColor(context!!, R.color.alpha25)
			)
		}
	}
}