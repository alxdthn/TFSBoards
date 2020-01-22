package com.alxdthn.tfsboards.ui.card.fragment.card_actions_adapter

import android.view.View
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.ActionItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.local.Action
import com.alxdthn.tfsboards.ui.card.fragment.CardFragment

@Suppress("UNCHECKED_CAST")
class CardActionsItemsHandler(
	main: CardFragment
) : BaseItemsHandler(
	main.compositeDisposable,
	CardActionsDiffCallback(),
	CardActionsAdapter()
) {

	private val viewModel = main.viewModel
	private val avatarColorCache = mutableMapOf<String, Int>()

	init {
		(adapter as CardActionsAdapter).itemsHandler = this
	}

	override fun onResult(result: List<Item>) {
		viewModel.downloadingActions.cancel()
		viewModel.actionsOpen.set(viewModel.getCardData().actionsOpen)
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val items = mutableListOf<ActionItem>()
		val actions = data as List<Action>

		actions.forEach { action ->
			val idUser = action.memberCreator.id

			if (!avatarColorCache.containsKey(idUser)) {
				avatarColorCache[idUser] = getUserAvatarColor(idUser)
			}
			val color = avatarColorCache[idUser] ?: 0
			val item = ActionItem() render action with color
			items.add(item)
		}
		return items
	}

	override fun onItemClick(item: Item, view: View) = Unit

	private fun getUserAvatarColor(id: String?): Int {
		if (id == null) return -1
		return viewModel.boardMembers.find { it.id == id }?.color ?: -1
	}
}