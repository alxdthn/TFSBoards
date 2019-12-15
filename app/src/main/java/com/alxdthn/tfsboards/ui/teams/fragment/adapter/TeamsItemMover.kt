package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import com.alxdthn.tfsboards.base.Movable
import com.alxdthn.tfsboards.model.local.Board
import com.alxdthn.tfsboards.model.local.Team

class TeamsItemMover(private val data: MutableList<Team>,
					 idBoard: String) {

	private val payload = mutableListOf<Movable>()
	private var index = 0

	private lateinit var board: Board

	init {
		initializePayload(idBoard)
	}

	private fun initializePayload(idBoard: String) {
		var tmpIndex = 0

		data.forEach { team ->
			if (team.boards.isNotEmpty()) {
				payload.add(team)
				tmpIndex++
			}
			team.boards.forEach { board ->
				if (board.id == idBoard) {
					this.board = board
					index = tmpIndex
				}
				payload.add(board)
				tmpIndex++
			}
		}
	}

	fun move(dir: Int) {
		val item = payload[index]

		payload.removeAt(index)
		index += dir
		payload.add(index, item)
	}

	fun acceptMove() {
		val emptyTeams = data.filter { it.boards.isEmpty() }
		var currentTeam :Team? = null

		data.forEach { team ->
			team.boards.clear()
		}
		data.clear()
		data.addAll(emptyTeams)
		payload.forEach { item ->
			when (item) {
				is Team -> {
					currentTeam = item
					data.add(item)
				}
				is Board -> {
					if (item == board) {
						board.idTeam = currentTeam?.id
					}
					currentTeam?.boards?.add(item)
				}
			}
		}
	}
}