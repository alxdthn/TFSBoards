package com.alxdthn.tfsboards.ui.members.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.UserItem
import com.alxdthn.tfsboards.utilities.AppCommon
import com.alxdthn.tfsboards.utilities.extensions.gone
import com.alxdthn.tfsboards.utilities.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_members_list_item.view.*

class MembersViewHolder(v: View) : RecyclerView.ViewHolder(v) {
	fun bindTo(userItem: UserItem) {
		bindName(userItem)
		itemView.apply {
			txvBoardMembersUserLogin.text = context.getString(R.string.user_name, userItem.name)
			avBoardMembersUserAvatar.setLabel(userItem.fullName)
			AppCommon.setAvatarFromHash(avBoardMembersUserAvatar, userItem.avatarHash, userItem.color)
			btnBoardMembersChecker visibleIf userItem.checked
		}
	}

	fun bindTo(payloads: MutableList<Any?>) {
		payloads.forEach { checked ->
			if (checked is Boolean) {
				itemView.btnBoardMembersChecker visibleIf checked
			}
		}
	}

	private fun bindName(user: UserItem) {
		if (user.fullName != user.name) {
			itemView.txvBoardMembersUserFullName.text = user.fullName
		} else {
			itemView.txvBoardMembersUserFullName.gone()
		}
	}
}