package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.model.BoardItem
import com.alxdthn.tfsboards.model.HeaderItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.sticky_header.StickyHeaderInterface
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper.TouchHelperInterface
import kotlinx.android.synthetic.main.layout_teams_list_header.view.*

class TeamsAdapter: BaseAdapter(),
	TouchHelperInterface,
	StickyHeaderInterface {

	lateinit var itemsHandler: TeamsItemsHandler

	companion object {
		const val HEADER = 3
		const val ITEM = 1
	}

	override fun getItems() = itemsHandler.items

	override fun getItemViewType(position: Int): Int {
		return when (getItems()[position]) {
			is HeaderItem -> HEADER
			else -> ITEM
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return when (viewType) {
			HEADER -> {
				TeamsViewHolder.HeaderViewHolder(
					layoutInflater.inflate(
						R.layout.layout_teams_list_header,
						parent,
						false
					)
				)
			}
			else -> {
				TeamsViewHolder.BoardViewHolder(
					layoutInflater.inflate(
						R.layout.layout_teams_list_item,
						parent,
						false
					)
				)
			}
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is TeamsViewHolder.BoardViewHolder -> {
				holder.bindTo(getItems()[position] as BoardItem)
				holder.itemView.setOnClickListener { view ->
					onItemClick(getItems()[position], view) }
			}
			is TeamsViewHolder.HeaderViewHolder -> holder.bindTo(getItems()[position] as HeaderItem)
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		when (holder) {
			is TeamsViewHolder.BoardViewHolder -> holder.bindTo(payloads)
			is TeamsViewHolder.HeaderViewHolder -> holder.bindTo(payloads)
		}
	}

	override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
		if (toPosition > 0) {
			val items = getItems() as MutableList<Item>
			val item = items[fromPosition] as BoardItem

			notifyItemMoved(fromPosition, toPosition)

			items.removeAt(fromPosition)
			items.add(toPosition, item)

			return itemsHandler.onItemMove(fromPosition, toPosition)
		}
		return false
	}

	override fun onItemSwipe(position: Int): Boolean {
		return itemsHandler.onItemSwipe(position)
	}

	override fun onItemClick(item: Item, view: View) {
		itemsHandler.onItemClick(item, view)
	}

	override fun onItemMoveStart(position: Int) {
		itemsHandler.onItemMoveStart(position)
	}

	override fun onItemMoveEnd() {
		itemsHandler.onItemMoveEnd()
	}

	override fun isHeader(itemPosition: Int): Boolean {
		return if (itemPosition >= 0) {
			getItems()[itemPosition] is HeaderItem
		} else {
			false
		}
	}

	override fun getHeaderPositionForItem(itemPosition: Int): Int {
		var position = itemPosition

		while (position >= 0) {
			if (this.isHeader(position)) {
				return position
			}
			position--
		}
		return 0
	}

	override fun getHeaderLayout(headerPosition: Int): Int {
		return R.layout.layout_teams_list_header
	}

	override fun bindHeaderData(header: View, headerPosition: Int) {
		val item = (getItems()[headerPosition] as HeaderItem)

		header.txvGroupHeaderName.text = item.name
		header.txvGroupHeaderItemCount.text = item.countOfBoards.toString()
	}
}