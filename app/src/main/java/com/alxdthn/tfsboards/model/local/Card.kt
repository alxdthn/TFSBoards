package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.model.responses.CardResponse
import com.alxdthn.tfsboards.utilities.AppActionFormatter.ignoredActions
import com.alxdthn.tfsboards.utilities.extensions.filteredMap
import com.alxdthn.tfsboards.utilities.extensions.replaceAll

data class Card(
	var id: String = "?",
	var name: String = "?",
	var idColumn: String = "?",
	var pos: Float = 0f,
	var closed: Boolean = false,
	var desc: String = "",
	var removed: Boolean = false,
	var members: List<User>? = null,
	var idMembers: MutableList<String> = mutableListOf(),
	var attachments: MutableList<Attachment> = mutableListOf(),
	var actions: MutableList<Action>? = null,
	var attachmentsOpen: Boolean = false,
	var actionsOpen: Boolean = false
) {

	fun fromResponse(cardResponse: CardResponse): Card {
		return apply {
			id = cardResponse.id
			name = cardResponse.name
			idColumn = cardResponse.idColumn
			pos = cardResponse.pos
			closed = cardResponse.closed
			desc = cardResponse.desc
			idMembers.replaceAll(cardResponse.idMembers)
			attachments.replaceAll(
				cardResponse.attachments.map {
					Attachment().fromResponse(it)
				}.sortedBy {
					it.previews.isEmpty()
				}
			)
			actions?.replaceAll(
				cardResponse.actions.filteredMap(
					{ !ignoredActions.contains(it.display.translationKey) },
					{ Action().fromResponse(it) })
			)
		}
	}
}