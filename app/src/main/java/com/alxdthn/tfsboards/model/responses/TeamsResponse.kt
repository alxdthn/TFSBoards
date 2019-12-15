package com.alxdthn.tfsboards.model.responses

import com.google.gson.annotations.SerializedName

data class TeamsResponse(
	val id: String,
	@SerializedName("organizations")
	val teams: Array<TeamResponse>,
	val boards: Array<BoardResponse>
)