package com.alxdthn.tfsboards.ui.card.fragment.card_actions_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.ActionItem

@Suppress("UNCHECKED_CAST")
class CardActionsAdapter: BaseAdapter() {

	lateinit var itemsHandler: CardActionsItemsHandler

	override fun getItems() = itemsHandler.items as List<ActionItem>

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return CardActionViewHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.layout_card_actions_list_item, parent, false
			)
		)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is CardActionViewHolder) {
			val item = getItems()[position]

			holder.itemView.setOnClickListener { view ->
				itemsHandler.onItemClick(item, view)
			}
			holder.bind(item)
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		if (holder is CardActionViewHolder) {
			holder.bind(payloads)
		}
	}
}