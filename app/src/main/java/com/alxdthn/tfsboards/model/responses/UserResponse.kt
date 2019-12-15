package com.alxdthn.tfsboards.model.responses

data class UserResponse (
	val id: String = "?",
	val username: String = "?",
	val fullName: String = "?",
	val avatarHash: String? = null,
	val avatarUrl: String? = null
)