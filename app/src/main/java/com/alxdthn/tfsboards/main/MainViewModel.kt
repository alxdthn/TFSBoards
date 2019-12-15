package com.alxdthn.tfsboards.main

import com.alxdthn.tfsboards.base.BaseViewModel
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.preferences.AppPreferences
import com.alxdthn.tfsboards.utilities.AppConstants.TOKEN_PREF_KEY
import com.alxdthn.tfsboards.utilities.extensions.subscribeToGlobal
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainViewModel @Inject constructor(
	private val preferences: AppPreferences
) : BaseViewModel() {

	var current = Events.NOTHING

	init {
		subscribeToGlobal { state ->
			when (state) {
				Events.LOGOUT -> preferences.remove(TOKEN_PREF_KEY)
				else -> Unit
			}
		}
	}

	override fun initializer() = Unit

	fun onCreate() {
		val userToken = preferences.getString(TOKEN_PREF_KEY)

		if (userToken == null) {
			GlobalEvent.set(Events.AUTH)
		} else {
			GlobalEvent.set(Events.TEAMS)
		}
	}
}