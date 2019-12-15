package com.alxdthn.tfsboards.network

import com.alxdthn.tfsboards.preferences.AppPreferences
import com.alxdthn.tfsboards.utilities.AppConstants.APP_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.TOKEN_PREF_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TrelloInterceptor @Inject constructor(
	private val preferences: AppPreferences
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		val newRequest = originalRequest.newBuilder()
		val token = preferences.getString(TOKEN_PREF_KEY)
		val url = originalRequest.url().newBuilder()
			.addQueryParameter("token", token)
			.addQueryParameter("key", APP_KEY)
			.build()

		newRequest.url(url)
		return chain.proceed(newRequest.build())
	}
}