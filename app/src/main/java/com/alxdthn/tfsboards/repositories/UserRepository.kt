package com.alxdthn.tfsboards.repositories

import com.alxdthn.tfsboards.base.BaseRepository
import com.alxdthn.tfsboards.model.responses.UserResponse
import com.alxdthn.tfsboards.network.services.TrelloUserService
import com.alxdthn.tfsboards.preferences.AppPreferences
import com.alxdthn.tfsboards.utilities.AppConstants
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepository @Inject constructor(
	private val userService: TrelloUserService,
	private val preferences: AppPreferences
) : BaseRepository() {

	fun saveToken(token: String) {
		preferences.putString(AppConstants.TOKEN_PREF_KEY, token)
	}

	fun getUser(): Single<UserResponse> {
		return preparedSingle(userService.getUserFromApi())
	}
}