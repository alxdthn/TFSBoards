package com.alxdthn.tfsboards.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.alxdthn.tfsboards.base.LocalState
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.repositories.UserRepository
import com.alxdthn.tfsboards.utilities.AppConstants.APP_HOST
import javax.inject.Inject


class AuthViewModel @Inject constructor(
	private val repository: UserRepository
) : ViewModel() {

	val onLogin = LocalState().withLog("LOGIN")
	val accessGranted = LocalState().withLog("accessGranted")

	fun handleUrl(url: String): Boolean {
		val uri = Uri.parse(url)

		if (uri.host == APP_HOST) {
			val token = uri.fragment?.split("=")!![1]

			repository.saveToken(token)
			accessGranted.start()
			return GlobalEvent.set(Events.TEAMS)
		}
		return false
	}
}