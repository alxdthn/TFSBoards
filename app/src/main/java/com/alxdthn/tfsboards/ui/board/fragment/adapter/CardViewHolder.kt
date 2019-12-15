package com.alxdthn.tfsboards.ui.board.fragment.adapter

import android.view.View
import com.alxdthn.tfsboards.common.CardViewHolderBinder
import com.alxdthn.tfsboards.model.CardItem
import com.woxthebox.draglistview.DragItemAdapter

class CardViewHolder(itemView: View, grabHandleId: Int, dragOnLongPress: Boolean) :
	DragItemAdapter.ViewHolder(itemView, grabHandleId, dragOnLongPress) {

	private val binder = CardViewHolderBinder()

	fun bindTo(cardItem: CardItem) {
		binder(itemView, cardItem)
	}
}