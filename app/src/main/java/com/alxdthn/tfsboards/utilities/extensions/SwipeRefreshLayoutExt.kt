package com.alxdthn.tfsboards.utilities.extensions

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alxdthn.tfsboards.R

fun SwipeRefreshLayout.setAppColors() {
	setColorSchemeColors(
		ContextCompat.getColor(context, R.color.colorAccent),
		ContextCompat.getColor(context, R.color.sky),
		ContextCompat.getColor(context, R.color.colorAccent),
		ContextCompat.getColor(context, R.color.sky)
	)
}