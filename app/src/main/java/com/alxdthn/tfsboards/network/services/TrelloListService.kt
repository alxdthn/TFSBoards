package com.alxdthn.tfsboards.network.services

import com.alxdthn.tfsboards.model.responses.ItemResponse
import io.reactivex.Single
import retrofit2.http.*

interface TrelloListService {

	@POST("lists")
	fun postNewColumnToApi(
		@Query("name") name: String,
		@Query("idBoard") idBoard: String,
		@Query("pos") pos: String = "bottom"
	): Single<ItemResponse>

	@PUT("lists/{id}")
	fun putColumnToApi(
		@Path("id") id: String,
		@Query("name") name: String
	): Single<ItemResponse>

	@PUT("lists/{id}/closed")
	fun archiveColumn(
		@Path("id") id: String,
		@Query("value") value: String
	): Single<ItemResponse>
}