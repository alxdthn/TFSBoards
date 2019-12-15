package com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper

import android.view.View
import com.alxdthn.tfsboards.model.Item

interface TouchHelperInterface {
	fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
	fun onItemClick(item: Item, view: View)
	fun onItemSwipe(position: Int): Boolean
	fun onItemMoveEnd()
	fun onItemMoveStart(position: Int)
}