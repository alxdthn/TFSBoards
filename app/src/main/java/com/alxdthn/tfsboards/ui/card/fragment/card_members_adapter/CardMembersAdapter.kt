package com.alxdthn.tfsboards.ui.card.fragment.card_members_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.UserAvatarItem

@Suppress("UNCHECKED_CAST")
class CardMembersAdapter: BaseAdapter() {

	lateinit var itemsHandler: BaseItemsHandler

	override fun getItems() = itemsHandler.items as List<UserAvatarItem>

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return CardMemberViewHolder(
			LayoutInflater
				.from(parent.context)
				.inflate(R.layout.layout_card_members_list_item, parent, false)
		)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is CardMemberViewHolder) {
			holder.bind(getItems()[position])
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		if (holder is CardMemberViewHolder) {
			holder.bind(payloads)
		}
	}
}