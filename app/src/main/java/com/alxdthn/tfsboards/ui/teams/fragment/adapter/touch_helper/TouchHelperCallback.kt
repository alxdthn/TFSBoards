package com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.TeamsViewHolder

class TouchHelperCallback(
	private val listener: TouchHelperInterface
) : ItemTouchHelper.Callback() {

	private var isOnMove = false

	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		return if (viewHolder is TeamsViewHolder.BoardViewHolder) {
			val dragFlags = ItemTouchHelper.UP or
					ItemTouchHelper.DOWN or
					ItemTouchHelper.START or
					ItemTouchHelper.END
			val swipeFlags = ItemTouchHelper.START or
					ItemTouchHelper.END
			return makeMovementFlags(dragFlags, swipeFlags)
		} else {
			makeMovementFlags(0, 0)
		}
	}

	override fun isLongPressDragEnabled() = true

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		if (viewHolder is TeamsViewHolder.BoardViewHolder) {
			listener.onItemSwipe(viewHolder.adapterPosition)
		}
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		return listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
	}

	override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
		super.onSelectedChanged(viewHolder, actionState)

		if (viewHolder != null) {
			if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && !isOnMove) {
				isOnMove = true
				listener.onItemMoveStart(viewHolder.adapterPosition)
				if (viewHolder is TouchHelperViewHolder) {
					viewHolder.onItemSelected()
				}
			}
		}
		if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && isOnMove) {
			isOnMove = false
			listener.onItemMoveEnd()
		}

	}

	override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

		if (viewHolder is TouchHelperViewHolder) {
			viewHolder.onItemClear()
		}
	}

	private fun printAction(actionState: Int) {
		when (actionState) {
			ItemTouchHelper.ACTION_STATE_DRAG -> Log.d("bestTAG", "ACTION_DRAG")
			ItemTouchHelper.ACTION_STATE_IDLE -> Log.d("bestTAG", "ACTION_IDLE")
			ItemTouchHelper.ACTION_STATE_SWIPE -> Log.d("bestTAG", "ACTION_SWIPE")
		}
	}
}
