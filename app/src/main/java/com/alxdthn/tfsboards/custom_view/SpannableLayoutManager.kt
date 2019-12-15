package com.alxdthn.tfsboards.custom_view

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 		Будет работать, если
 * 		adapter.getItemViewType(position)
 * 		будет возвращать span size
 *
 * 		Костыль, зато кода мало)
 */

class SpannableLayoutManager(
	context: Context?, span: Int,
	adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
) : GridLayoutManager(context, span) {

	init {
		if (adapter != null) {
			spanSizeLookup = object : SpanSizeLookup() {
				override fun getSpanSize(position: Int): Int {
					return adapter.getItemViewType(position)
				}
			}
		}
	}
}