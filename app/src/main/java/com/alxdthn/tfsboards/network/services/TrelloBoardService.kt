package com.alxdthn.tfsboards.network.services

import com.alxdthn.tfsboards.model.responses.BoardFullResponse
import com.alxdthn.tfsboards.model.responses.ItemResponse
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_REQUEST_FIELDS
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_REQUEST_FIELDS
import com.alxdthn.tfsboards.utilities.AppConstants.LIST_REQUEST_FIELDS
import io.reactivex.Single
import retrofit2.http.*

interface TrelloBoardService {

	@GET("boards/{idBoard}")
	fun getBoardFromApi(
		@Path("idBoard") idBoard: String,
		@Query("members") members: String = "all",
		@Query("lists") lists: String = "all",
		@Query("cards") cards: String = "open",
		@Query("fields") fields: String = BOARD_REQUEST_FIELDS,
		@Query("list_fields") listFields: String = LIST_REQUEST_FIELDS,
		@Query("card_fields") cardFields: String = CARD_REQUEST_FIELDS,
		@Query("card_attachments") cardAttachments: Boolean = true
	): Single<BoardFullResponse>

	@POST("boards")
	fun postNewBoardToApi(
		@Query("name") name: String,
		@Query("prefs_background") prefsBackground: String,
		@Query("idOrganization") idOrganization: String?,
		@Query("defaultLists") defaultLists: String = "false"
	): Single<ItemResponse>

	@DELETE("boards/{id}")
	fun deleteBoardFromApi(
		@Path("id") idBoard: String
	): Single<ItemResponse>
}