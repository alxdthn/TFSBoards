package com.alxdthn.tfsboards.model.responses

import com.google.gson.annotations.SerializedName

data class BoardFullResponse(
	val id: String,
	val name: String,
	@SerializedName("idOrganization")
	val idTeam: String,
	val cards: Array<CardResponse>,
	val lists: Array<ListResponse>,
	val members: Array<UserResponse>,
	val prefs: PrefsResponse
)