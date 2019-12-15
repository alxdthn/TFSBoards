package com.alxdthn.tfsboards.model.responses

data class ActionResponse(
	val id: String,
	val date: String,
	val type: String,
	val memberCreator: UserResponse,
	val display: ActionDisplayResponse
)

data class ActionDisplayResponse(
	val translationKey: String,
	val entities: ActionEntitiesResponse
)

data class ActionEntitiesResponse(
	val card: ActionEntityResponse?,
	val list: ActionEntityResponse?,
	val name: ActionEntityResponse?,
	val member: ActionEntityResponse?,
	val attachment: ActionEntityResponse?,
	val listBefore: ActionEntityResponse?,
	val listAfter: ActionEntityResponse?,
	val memberCreator: ActionEntityResponse?,
	var comment: ActionEntityResponse?
)

data class ActionEntityResponse(
	val text: String?,
	val desc: String?,
	val previewUrl: String?
)