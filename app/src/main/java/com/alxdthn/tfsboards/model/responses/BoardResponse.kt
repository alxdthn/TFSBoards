package com.alxdthn.tfsboards.model.responses

import com.google.gson.annotations.SerializedName

data class BoardResponse(
	val id: String,
	val name: String,
	@SerializedName("idOrganization")
	val idTeam: String?,
	val prefs: PrefsResponse
)