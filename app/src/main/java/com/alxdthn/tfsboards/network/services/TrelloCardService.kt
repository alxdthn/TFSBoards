package com.alxdthn.tfsboards.network.services

import com.alxdthn.tfsboards.model.responses.ActionResponse
import com.alxdthn.tfsboards.model.responses.AttachmentResponse
import com.alxdthn.tfsboards.model.responses.CardResponse
import com.alxdthn.tfsboards.model.responses.ItemResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface TrelloCardService {

	@Multipart
	@POST("cards/{id}/attachments")
	fun postAttachment(
		@Part("description") description: RequestBody?,
		@Part file: MultipartBody.Part?
	): Single<AttachmentResponse>

	@DELETE("cards/{id}/attachments/{idAttachment}")
	fun deleteAttachment(
		@Path("id") id: String,
		@Path("idAttachment") idAttachment: String
	): Single<ItemResponse>

	@GET("cards/{id}")
	fun getCard(
		@Path("id") id: String,
		@Query("attachments") attachments: Boolean = true,
		@Query("actions") actions: String = "all",
		@Query("action_fields") actionFields: String = "id,type,data,date,memberCreator",
		@Query("actions_display") actionsDisplay: String = "true",
		@Query("actions_limit") actionsLimit: Int = 1000
	): Single<CardResponse>

	@GET("cards/{id}/actions")
	fun getActions(
		@Path("id") id: String,
		@Query("limit") limit: Int = 1000,
		@Query("filter") actions: String = "all",
		@Query("fields") fields: String = "id,date",
		@Query("display") display: Boolean = true,
		@Query("member") member: Boolean = false,
		@Query("memberCreator_fields") memberCreatorFields: String = "username,avatarHash,FullName"
	): Single<List<ActionResponse>>

	@POST("cards")
	fun postNewCardToApi(
		@Query("name") name: String,
		@Query("idList") idList: String,
		@Query("keepFromSource") keepFromSource: String = "all"
	): Single<ItemResponse>

	@DELETE("cards/{id}")
	fun deleteCardFromApi(
		@Path("id") idCard: String
	): Single<ItemResponse>

	@PUT("cards/{id}")
	fun putCardToApi(
		@Path("id") id: String,
		@Query("name") name: String,
		@Query("idList") idList: String,
		@Query("pos") pos: String,
		@Query("desc") desc: String
	): Single<ItemResponse>

	@PUT("cards/{id}")
	fun updateCardMembers(
		@Path("id") id: String,
		@Query("idMembers") idMembers: String
	): Single<ItemResponse>
}