package com.alxdthn.tfsboards.model.responses

import com.google.gson.annotations.SerializedName

data class TeamResponse(
	val id: String,
	@SerializedName("displayName")
	val name: String
)