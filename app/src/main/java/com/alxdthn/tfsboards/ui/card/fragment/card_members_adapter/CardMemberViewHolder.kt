package com.alxdthn.tfsboards.ui.card.fragment.card_members_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.model.AvatarPayload
import com.alxdthn.tfsboards.model.NamePayload
import com.alxdthn.tfsboards.model.UserAvatarItem
import com.alxdthn.tfsboards.utilities.AppCommon
import kotlinx.android.synthetic.main.layout_card_members_list_item.view.*

class CardMemberViewHolder(v: View) : RecyclerView.ViewHolder(v) {
	fun bind(userItem: UserAvatarItem) {
		bindLabel(userItem.name)
		bindImage(userItem.avatarHash, userItem.avatarColor)
	}

	fun bind(payloads: List<Any?>) {
		payloads.forEach { payload ->
			when (payload) {
				is NamePayload -> bindLabel(payload.name)
				is AvatarPayload -> bindImage(payload.hash, payload.color)
			}
		}
	}

	private fun bindImage(hash: String?, color: Int) {
		AppCommon.setAvatarFromHash(itemView.avCardFragmentMemberAvatar, hash, color)
	}

	private fun bindLabel(name: String) {
		itemView.avCardFragmentMemberAvatar.setLabel(name)
	}
}