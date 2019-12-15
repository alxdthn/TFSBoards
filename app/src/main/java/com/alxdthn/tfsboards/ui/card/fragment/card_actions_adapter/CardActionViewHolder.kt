package com.alxdthn.tfsboards.ui.card.fragment.card_actions_adapter

import android.text.Spanned
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.utilities.AppCommon
import kotlinx.android.synthetic.main.layout_card_actions_list_item.view.*

class CardActionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
	fun bind(actionItem: ActionItem) {
		actionItem.apply {
			bindDescription(description)
			bindTime(actionTime)
			bindUserAvatar(avatarHash, avatarColor)
			bindImagePreview(previewUrl)
		}
	}

	fun bind(payloads: MutableList<Any?>) {
		payloads.forEach { payload ->
			when (payload) {
				is AvatarPayload -> bindUserAvatar(payload.hash, payload.color)
				is DescriptionPayload -> bindDescription(payload.text)
				is TimePayload -> bindTime(payload.text)
				is ImagePreviewPayload -> bindImagePreview(payload.url)
			}
		}
	}

	private fun bindImagePreview(url: String?) {
		if (url != null) {
			itemView.cvCardActionPreview.visibility = View.VISIBLE
			AppCommon.setImageBackground(itemView.ivCardActionPreview, url)
		} else {
			itemView.cvCardActionPreview.visibility = View.GONE
		}
	}

	private fun bindTime(text: String) {
		itemView.txvCardFragmentActionTimeInfo.text = text
	}

	private fun bindDescription(text: Spanned?) {
		itemView.txvCardFragmentActionDescription.text = text
	}

	private fun bindUserAvatar(hash: String?, color: Int) {
		AppCommon.setAvatarFromHash(itemView.avCardFragmentActionsUserAvatar, hash, color)
	}
}