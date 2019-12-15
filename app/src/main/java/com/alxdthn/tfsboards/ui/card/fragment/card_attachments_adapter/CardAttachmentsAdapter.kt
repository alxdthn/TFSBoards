package com.alxdthn.tfsboards.ui.card.fragment.card_attachments_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.model.AttachmentFileItem
import com.alxdthn.tfsboards.model.AttachmentImageItem

class CardAttachmentsAdapter: BaseAdapter() {

	lateinit var itemsHandler: CardAttachmentsItemsHandler

	companion object {
		const val FILE = 3
		const val IMAGE = 1
	}

	override fun getItems() = itemsHandler.items

	override fun getItemViewType(position: Int): Int {
		return when (getItems()[position]) {
			is AttachmentFileItem -> FILE
			is AttachmentImageItem -> IMAGE
			else -> throw IllegalArgumentException()
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return CardAttachmentsViewHolder(
			LayoutInflater.from(parent.context).inflate(
				if (viewType == FILE) R.layout.layout_card_attachment_list_file_item
				else R.layout.layout_card_attachment_list_image_item,
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is CardAttachmentsViewHolder) {
			val item = getItems()[position]

			when (item) {
				is AttachmentImageItem -> holder.bind(item)
				is AttachmentFileItem -> holder.bind(item)
			}
			holder.itemView.setOnClickListener { view ->
				itemsHandler.onItemClick(item, view)
			}
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		if (holder is CardAttachmentsViewHolder) {
			holder.bind(payloads)
		}
	}
}