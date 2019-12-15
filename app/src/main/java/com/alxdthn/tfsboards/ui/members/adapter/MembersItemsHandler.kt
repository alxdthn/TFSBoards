package com.alxdthn.tfsboards.ui.members.adapter

import android.view.View
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.UserItem
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.ui.members.MembersFragment
import com.alxdthn.tfsboards.utilities.AppDiffUtil

@Suppress("UNCHECKED_CAST")
class MembersItemsHandler(
	main: MembersFragment
) : BaseItemsHandler(main.getCompositeDisposable(), DiffCallback(), MembersAdapter()) {

	private val viewModel = main.viewModel

	init {
		(adapter as MembersAdapter).itemsHandler = this
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val boardMembers = data as List<User>

		return boardMembers.map { UserItem() render it }
	}

	override fun onResult(result: List<Item>) = Unit

	override fun onItemClick(item: Item, view: View) {
		viewModel.changeResult(item.id)
	}

	class DiffCallback : AppDiffUtil.BaseDiffCallback() {

		override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
			val oldItem = getOldItems()[oldPos] as UserItem
			val newItem = getNewItems()[newPos] as UserItem

			return if (oldItem.checked != newItem.checked) {
				newItem.checked
			} else null
		}
	}
}