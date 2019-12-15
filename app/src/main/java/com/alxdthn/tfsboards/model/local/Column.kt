package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.model.responses.*

data class Column(
	var id: String = "?",
	var name: String = "?",
	var idBoard: String = "?",
	var pos: Float = 0f,
	val cards: MutableList<Card> = mutableListOf(),
	var closed: Boolean = false
) {
	fun fromResponse(listResponse: ListResponse): Column {
		return apply {
			id = listResponse.id
			name = listResponse.name
			idBoard = listResponse.idBoard
			pos = listResponse.pos
			closed = listResponse.closed
		}
	}
}