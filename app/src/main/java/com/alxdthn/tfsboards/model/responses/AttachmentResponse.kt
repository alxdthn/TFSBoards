package com.alxdthn.tfsboards.model.responses

data class AttachmentResponse(
	val id: String,
	val bytes: Long,
	val date: String,
	val edgeColor: String,
	val idMember: String,
	val isUploaded: Boolean,
	val mimeType: String,
	val name: String,
	val url: String,
	val pos: Int,
	val previews: List<PreviewResponse>
)