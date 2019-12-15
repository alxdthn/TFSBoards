package com.alxdthn.tfsboards.ui.card.fragment.card_members_adapter

import com.alxdthn.tfsboards.model.AvatarPayload
import com.alxdthn.tfsboards.model.NamePayload
import com.alxdthn.tfsboards.model.UserAvatarItem
import com.alxdthn.tfsboards.utilities.AppDiffUtil

class CardMembersDiffCallback :
	AppDiffUtil.BaseDiffCallback() {

	override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
		val oldItem = getOldItems()[oldPos] as UserAvatarItem
		val newItem = getNewItems()[newPos] as UserAvatarItem

		return if (oldItem.name != newItem.name) {
			NamePayload(newItem.name)
		} else if (oldItem.avatarHash != newItem.avatarHash) {
			AvatarPayload(newItem.avatarHash, newItem.avatarColor)
		} else null
	}
}