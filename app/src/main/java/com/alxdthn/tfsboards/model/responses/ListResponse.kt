package com.alxdthn.tfsboards.model.responses

data class ListResponse(
	val id: String,
	val name: String,
	val idBoard: String,
	val pos: Float,
	val closed: Boolean
)