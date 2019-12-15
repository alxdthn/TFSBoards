package com.alxdthn.tfsboards.repositories

import com.alxdthn.tfsboards.base.BaseRepository
import com.alxdthn.tfsboards.model.local.Column
import com.alxdthn.tfsboards.model.responses.ItemResponse
import com.alxdthn.tfsboards.network.services.TrelloListService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListRepository @Inject constructor(
	private val listService: TrelloListService
) : BaseRepository() {

	fun archiveColumn(idColumn: String, value: Boolean): Single<ItemResponse> {
		return preparedSingle(listService.archiveColumn(
			id = idColumn,
			value = value.toString()
		))
	}

	fun updateColumn(column: Column): Single<ItemResponse> {
		return preparedSingle(listService.putColumnToApi(
			id = column.id,
			name = column.name
		))
	}

	fun addNewColumn(newColumn: Column): Single<Unit> {
		return preparedSingle(listService.postNewColumnToApi(
			name = newColumn.name,
			idBoard = newColumn.idBoard
		)).map { itemResponse -> prepareColumn(newColumn, itemResponse) }
	}

	private fun prepareColumn(column: Column, response: ItemResponse) {
		column.id = response.id
		column.pos = response.pos
	}
}