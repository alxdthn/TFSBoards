package com.alxdthn.tfsboards.ui.teams.fragment.adapter.sticky_header

import android.view.View

interface StickyHeaderInterface {
	fun getHeaderPositionForItem(itemPosition: Int): Int
	fun getHeaderLayout(headerPosition: Int): Int
	fun bindHeaderData(header: View, headerPosition: Int)
	fun isHeader(itemPosition: Int): Boolean
}