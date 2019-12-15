package com.alxdthn.tfsboards.ui.card.fragment.card_attachments_adapter

import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.utilities.AppDiffUtil

class CardAttachmentsDiffCallback : AppDiffUtil.BaseDiffCallback() {

	override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
		val oldItem = getOldItems()[oldPos]
		val newItem = getNewItems()[newPos]

		return if (oldItem is AttachmentImageItem && newItem is AttachmentImageItem) {
			getPayloadForImage(oldItem, newItem)
		} else if (oldItem is AttachmentFileItem && newItem is AttachmentFileItem) {
			getPayloadForFile(oldItem, newItem)
		} else null
	}

	private fun getPayloadForFile(oldItem: AttachmentFileItem, newItem: AttachmentFileItem): Any? {
		return if (oldItem.info != newItem.info) {
			InfoPayload(newItem.info)
		} else if (oldItem.name != newItem.name) {
			NamePayload(newItem.name)
		} else null
	}

	private fun getPayloadForImage(oldItem: AttachmentImageItem, newItem: AttachmentImageItem): Any? {
		return if (oldItem.url != newItem.url) {
			UrlPayload(newItem.url)
		} else null
	}
}