package com.alxdthn.tfsboards.model.responses

import com.google.gson.annotations.SerializedName

data class CardResponse(
	val id: String,
	val name: String,
	@SerializedName("idList")
	val idColumn: String,
	val pos: Float,
	val closed: Boolean,
	val desc: String,
	val idMembers: List<String>,
	val attachments: List<AttachmentResponse>,
	val actions: List<ActionResponse>
)