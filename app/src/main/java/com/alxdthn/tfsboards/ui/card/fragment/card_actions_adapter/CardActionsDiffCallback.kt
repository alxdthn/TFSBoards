package com.alxdthn.tfsboards.ui.card.fragment.card_actions_adapter

import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.utilities.AppDiffUtil

class CardActionsDiffCallback :
	AppDiffUtil.BaseDiffCallback() {

	@Suppress("UNCHECKED_CAST")
	override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
		val oldItem = getOldItems()[oldPos] as ActionItem
		val newItem = getNewItems()[newPos] as ActionItem

		return if (oldItem.avatarHash != newItem.avatarHash
			|| oldItem.avatarColor != newItem.avatarColor) {
			AvatarPayload(newItem.avatarHash, newItem.avatarColor)
		} else if (oldItem.description.toString() != newItem.description.toString()) {
			DescriptionPayload(newItem.description)
		} else if (oldItem.actionTime != newItem.actionTime) {
			TimePayload(newItem.actionTime)
		} else if (oldItem.previewUrl != newItem.previewUrl) {
			ImagePreviewPayload(newItem.previewUrl)
		} else null
	}
}