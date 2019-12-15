package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.utilities.AppDiffUtil

class TeamsDiffCallback :
	AppDiffUtil.BaseDiffCallback() {

	override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
		val oldItem = getOldItems()[oldPos]
		val newItem = getNewItems()[newPos]

		return if (oldItem is BoardItem && newItem is BoardItem) {
			getPayloadForBoard(oldItem, newItem)
		} else if (oldItem is HeaderItem && newItem is HeaderItem) {
			getPayloadForHeader(oldItem, newItem)
		} else null
	}

	private fun getPayloadForBoard(oldItem: BoardItem, newItem: BoardItem) : Any? {
		return if (newItem.color != oldItem.color || newItem.url != oldItem.url) {
			ImagePayload(newItem.url, newItem.color)
		} else if (newItem.name != oldItem.name) {
			NamePayload(newItem.name)
		} else null
	}

	private fun getPayloadForHeader(oldItem: HeaderItem, newItem: HeaderItem) : Any? {
		return if (newItem.countOfBoards != oldItem.countOfBoards) {
			InfoPayload(newItem.countOfBoards.toString())
		} else if (newItem.name != oldItem.name) {
			NamePayload(newItem.name)
		} else null
	}
}