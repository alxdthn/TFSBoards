package com.alxdthn.tfsboards.ui.board.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.ui.board.fragment.BoardFragment
import com.woxthebox.draglistview.DragItemAdapter
import kotlinx.android.synthetic.main.layout_board_add_card_item.view.*

class ColumnAdapter(
	list: List<Item>,
	private val boardFragment: BoardFragment
): DragItemAdapter<Item, CardViewHolder>() {

	init {
		itemList = list
	}

	override fun getItemViewType(position: Int): Int {
		return when (itemList[position]) {
			is CardItem -> R.layout.layout_board_card_item
			else -> R.layout.layout_board_add_card_item
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
		return CardViewHolder(
			LayoutInflater.from(parent.context).inflate(
				viewType, parent, false), R.id.cvBoardColumnDragItem,
			viewType == R.layout.layout_board_card_item)
	}

	override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
		super.onBindViewHolder(holder, position)
		val item = itemList[position]

		if (item is CardItem) {
			holder.bindTo(item)
			holder.itemView.setOnClickListener {
				boardFragment.clickHandler.onItemClick(item)
			}
		} else {
			boardFragment.selectedEditText = holder.itemView.edxAddCardInput
		}
	}

	@SuppressLint("DefaultLocale")
	override fun getUniqueItemId(position: Int): Long {
		return itemList[position].id.toUpperCase().toBigInteger(radix = 16).toLong()
	}
}