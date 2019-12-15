package com.alxdthn.tfsboards.ui.members.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.model.UserItem
import com.alxdthn.tfsboards.utilities.AppCommon
import com.alxdthn.tfsboards.utilities.extensions.gone
import com.alxdthn.tfsboards.utilities.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_members_list_item.view.*

@Suppress("UNCHECKED_CAST")
class MembersAdapter : BaseAdapter() {

	lateinit var itemsHandler: MembersItemsHandler

	override fun getItems() = itemsHandler.items as List<UserItem>

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)

		return MembersViewHolder(
			layoutInflater.inflate(
				R.layout.layout_members_list_item,
				parent,
				false
			)
		)
	}

	override fun getItemCount(): Int {
		return getItems().size
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is MembersViewHolder) {
			val item = getItems()[position]

			holder.bindTo(item)
			holder.itemView.setOnClickListener { view ->
				itemsHandler.onItemClick(item, view)
			}
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		if (holder is MembersViewHolder) {
			holder.bindTo(payloads)
		}
	}
}