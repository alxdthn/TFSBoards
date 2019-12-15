package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.model.responses.ActionResponse
import com.alxdthn.tfsboards.utilities.AppActionFormatter.initArgs

data class Action(
	var id: String = "?",
	var type: String = "?",
	var date: String = "?",
	val memberCreator: User = User(),
	var args: Array<String?>? = null,
	var previewUrl: String? = null
) {

	fun fromResponse(actionResponse: ActionResponse): Action {
		val newAction = this.apply {
			id = actionResponse.id
			type = actionResponse.display.translationKey
			date = actionResponse.date
			memberCreator.id = actionResponse.memberCreator.id
			memberCreator.fullName = actionResponse.display.entities.memberCreator?.text ?: "?"
			memberCreator.avatarHash = actionResponse.memberCreator.avatarHash
			previewUrl = actionResponse.display.entities.attachment?.previewUrl
		}
		newAction.args = initArgs(newAction, actionResponse.display.entities)
		return newAction
	}
}