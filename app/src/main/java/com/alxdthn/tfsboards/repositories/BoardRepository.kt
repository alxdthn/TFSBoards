package com.alxdthn.tfsboards.repositories

import com.alxdthn.tfsboards.base.BaseRepository
import com.alxdthn.tfsboards.model.local.*
import com.alxdthn.tfsboards.model.responses.BoardFullResponse
import com.alxdthn.tfsboards.model.responses.ItemResponse
import com.alxdthn.tfsboards.network.services.TrelloBoardService
import com.alxdthn.tfsboards.utilities.AppColors
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BoardRepository @Inject constructor(
	private val boardService: TrelloBoardService
) : BaseRepository() {

	fun getBoard(board: BoardData): Single<Unit> {
		return preparedSingle(boardService.getBoardFromApi(board.id))
			.map { boardResponse -> prepareBoard(board, boardResponse) }
	}

	fun addBoard(newBoard: Board): Single<ItemResponse> {
		return preparedSingle(boardService.postNewBoardToApi(
			name = newBoard.name,
			prefsBackground = AppColors.getColorName(newBoard.color!!.value),
			idOrganization = newBoard.idTeam
		))
	}

	fun removeBoard(idBoard: String): Single<ItemResponse> {
		return preparedSingle(boardService.deleteBoardFromApi(idBoard))
	}

	private fun prepareBoard(board: BoardData, response: BoardFullResponse) {
		response.lists.forEach {
			if (!it.closed) {
				board.columns.add(Column().fromResponse(it))
			}
		}
		response.cards.forEach { card ->
			val column = board.columns.find { column -> column.id == card.idColumn }

			column?.cards?.add(Card().fromResponse(card))
		}
		response.members.forEach { member ->
			board.members.add(User().fromResponse(member))
		}
	}
}