package com.alxdthn.tfsboards.network.services

import com.alxdthn.tfsboards.model.responses.TeamsResponse
import com.alxdthn.tfsboards.model.responses.UserResponse
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_REQUEST_FIELDS
import com.alxdthn.tfsboards.utilities.AppConstants.TEAM_REQUEST_FIELDS
import com.alxdthn.tfsboards.utilities.AppConstants.USER_REQUEST_FIELDS
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TrelloUserService {

    @GET("members/me")
    fun getUserFromApi(
        @Query("fields") fields: String = USER_REQUEST_FIELDS
    ): Single<UserResponse>

    @GET("members/me")
    fun getTeamsFromApi(
        @Query("fields") fields: String = "id",
        @Query("organizations") organizations: String = "all",
        @Query("organization_fields") organization_fields: String = TEAM_REQUEST_FIELDS,
        @Query("boards") boards: String = "all",
        @Query("board_fields") board_fields: String = BOARD_REQUEST_FIELDS
    ): Single<TeamsResponse>
}