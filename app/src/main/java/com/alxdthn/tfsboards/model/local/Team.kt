package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.base.Movable
import com.alxdthn.tfsboards.model.responses.TeamResponse

data class Team(
	var id: String? = null,
	var name: String = "Персональные доски",
	val boards: MutableList<Board> = mutableListOf()
) : Movable() {
	fun fromResponse(teamResponse: TeamResponse): Team {
		return this.apply {
			id = teamResponse.id
			name = teamResponse.name
		}
	}
}