package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.model.responses.UserResponse
import com.alxdthn.tfsboards.utilities.AppColors

data class User(
	var id: String = "?",
	var username: String = "?",
	var color: Int = 0,
	var fullName: String = "?",
	var avatarHash: String? = null,
	var avatarUrl: String? = null,
	var checked: Boolean = false
) {
	fun fromResponse(userResponse: UserResponse): User {
		return this.apply {
			id = userResponse.id
			username = userResponse.username
			fullName = userResponse.fullName
			avatarHash = userResponse.avatarHash
			avatarUrl = userResponse.avatarUrl
			color = AppColors.getRandColor().value
		}
	}
}