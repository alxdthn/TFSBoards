package com.alxdthn.tfsboards.ui.card.fragment.card_members_adapter

import android.view.View
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.UserAvatarItem
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.ui.card.fragment.CardFragment

@Suppress("UNCHECKED_CAST")
class CardMembersItemsHandler(
	main: CardFragment
) : BaseItemsHandler(
	main.compositeDisposable,
	CardMembersDiffCallback(),
	CardMembersAdapter()
) {

	init {
		(adapter as CardMembersAdapter).itemsHandler = this
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val items = mutableListOf<UserAvatarItem>()
		val cardMembers = data as List<User>

		cardMembers.forEach { member ->
			items.add(UserAvatarItem().render(member))
		}
		return items
	}

	override fun onItemClick(item: Item, view: View) = Unit

	override fun onResult(result: List<Item>) = Unit
}