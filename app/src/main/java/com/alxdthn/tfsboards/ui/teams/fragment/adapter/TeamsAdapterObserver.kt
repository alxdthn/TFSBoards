package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TeamsAdapterObserver(
	private val recyclerView: RecyclerView,
	private val stubView: View
) : RecyclerView.AdapterDataObserver() {

	init {
		checkIfEmpty()
	}

	private fun checkIfEmpty() {
		if (recyclerView.adapter != null) {
			val emptyViewVisible = recyclerView.adapter!!.itemCount == 0

			stubView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
			recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
		}
	}

	override fun onChanged() {
		checkIfEmpty()
	}

	override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
		checkIfEmpty()
	}

	override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
		checkIfEmpty()
	}
}