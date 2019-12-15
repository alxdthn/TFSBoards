package com.alxdthn.tfsboards.ui.card.fragment.card_attachments_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.utilities.AppCommon
import kotlinx.android.synthetic.main.layout_card_attachment_list_file_item.view.*
import kotlinx.android.synthetic.main.layout_card_attachment_list_image_item.view.*
import java.util.jar.Attributes

class CardAttachmentsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
	fun bind(attachmentFileItem: AttachmentFileItem) {
		bindName(attachmentFileItem.name)
		bindInfo(attachmentFileItem.info)
	}

	fun bind(attachmentImageItem: AttachmentImageItem) {
		bindImage(attachmentImageItem.url)
	}

	fun bind(payloads: List<Any?>) {
		payloads.forEach { payload ->
			when (payload) {
				is UrlPayload -> bindImage(payload.url)
				is InfoPayload -> bindInfo(payload.text)
				is NamePayload -> bindName(payload.name)
			}
		}
	}

	private fun bindInfo(info: String) {
		itemView.txvAttachmentFileInfo.text = info
	}

	private fun bindName(name: String) {
		itemView.txvAttachmentFileName.text = name
	}

	private fun bindImage(url: String) {
		AppCommon.setImageBackground(itemView.ivAttachmentItem, url)
	}
}