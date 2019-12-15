package com.alxdthn.tfsboards.common

import android.text.SpannableString
import android.view.View
import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.utilities.extensions.hereIf
import kotlinx.android.synthetic.main.layout_board_card_item.view.*

class CardViewHolderBinder : (View, CardItem) -> Unit {

	override fun invoke(itemView: View, cardItem: CardItem) {
		bindName(itemView, cardItem)
		bindMembersInfo(itemView, cardItem.memberCount)
		bindAttachmentInfo(itemView, cardItem.attachmentCount)
		bindDescriptionInfo(itemView, cardItem.hasDescription)
	}

	operator fun invoke(itemView: View, payloads: List<Any?>) {
		payloads.forEach { string ->
			if (string is SpannableString) bindName(itemView, string)
		}
	}

	private fun bindMembersInfo(itemView: View, count: Int) {
		itemView.ivCardItemMembers.hereIf(count != 0)
		itemView.txvCardItemMembersCount.hereIf(count != 0)
		itemView.txvCardItemMembersCount.text = count.toString()
	}

	private fun bindAttachmentInfo(itemView: View, count: Int) {
		itemView.ivCardItemAttachments.hereIf(count != 0)
		itemView.txvCardItemAttachmentsCount.hereIf(count != 0)
		itemView.txvCardItemAttachmentsCount.text = count.toString()
	}

	private fun bindDescriptionInfo(itemView: View, hasDescription: Boolean) {
		itemView.ivCardItemDescription hereIf hasDescription
	}

	private fun bindName(itemView: View, spannableString: SpannableString) {
		itemView.txvCardName.text = spannableString
	}

	private fun bindName(itemView: View, cardItem: CardItem) {
		itemView.txvCardName.text = cardItem.spannedName ?: cardItem.name
	}
}