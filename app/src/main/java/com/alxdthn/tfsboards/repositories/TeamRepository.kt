package com.alxdthn.tfsboards.repositories

import com.alxdthn.tfsboards.base.BaseRepository
import com.alxdthn.tfsboards.model.local.Board
import com.alxdthn.tfsboards.model.local.Team
import com.alxdthn.tfsboards.model.responses.TeamsResponse
import com.alxdthn.tfsboards.network.services.TrelloUserService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TeamRepository @Inject constructor(
	private val userService: TrelloUserService
) : BaseRepository() {

	fun getTeams(): Single<List<Team>> {
		return preparedSingle(userService.getTeamsFromApi())
			.map { teamsResponse -> prepareTeams(teamsResponse) }
	}

	private fun prepareTeams(response: TeamsResponse): List<Team> {
		val teams = mutableListOf<Team>()
		val personalTeam = Team(name = "Персональные доски")

		teams.add(personalTeam)
		response.teams.forEach {
			teams.add(Team().fromResponse(it))
		}
		response.boards.sortBy { it.name }
		response.boards.forEach { board ->
			val team = teams.find { it.id == board.idTeam } ?: personalTeam
			val preparedBoard = Board().fromResponse(board)

			team.boards.add(preparedBoard)
		}
		return teams
	}
}