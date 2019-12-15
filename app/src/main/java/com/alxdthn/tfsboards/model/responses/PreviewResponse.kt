package com.alxdthn.tfsboards.model.responses

data class PreviewResponse(
	val id: String,
	val scaled: Boolean,
	val url: String,
	val bytes: Long,
	val height: Int,
	val width: Int
)