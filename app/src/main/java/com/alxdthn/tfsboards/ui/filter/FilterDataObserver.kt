package com.alxdthn.tfsboards.ui.filter

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.R
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterDataObserver {

	fun bind(main: FilterFragment) {
		observeColors(main)
		observeFilteredData(main)
	}

	private fun observeFilteredData(main: FilterFragment) = main.apply {
		viewModel.filteredData.observe(viewLifecycleOwner, Observer { filteredData ->
			itemsHandler render filteredData
		})
	}

	private fun observeColors(main: FilterFragment) = main.apply {
		viewModel.colors.observe(viewLifecycleOwner, Observer { colors ->
			val toolbarColor = colors.darkValue

			cvFilterFragmentToolbar.setCardBackgroundColor(toolbarColor)
			llFilterFragmentBackground.setBackgroundColor(
				ContextCompat.getColor(context!!, R.color.alpha25)
			)
		})
	}
}