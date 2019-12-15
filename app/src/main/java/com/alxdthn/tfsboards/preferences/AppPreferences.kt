package com.alxdthn.tfsboards.preferences

import android.content.Context
import com.alxdthn.tfsboards.utilities.AppConstants.INTERNAL_PREFS

class AppPreferences(
	private val appContext: Context
) {

	fun putString(key: String, value: String) {
		preferences?.edit()
			?.putString(key, value)
			?.apply()
	}

	fun getString(key: String): String? {
		return preferences?.getString(key, null)
	}

	fun remove(key: String) {
		preferences?.edit()
			?.remove(key)
			?.apply()
	}

	private val preferences by lazy {
		appContext.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
	}
}