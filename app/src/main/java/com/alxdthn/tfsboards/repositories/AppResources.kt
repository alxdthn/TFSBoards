package com.alxdthn.tfsboards.repositories

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import androidx.annotation.StringRes
import javax.inject.Inject

class AppResources @Inject constructor(
	private val appContext: Context
) {

	fun getString(@StringRes stringRes: Int): String {
		return appContext.getString(stringRes)
	}

	fun getString(@StringRes stringRes: Int, vararg args: String): String {
		return appContext.getString(stringRes, *args)
	}

	fun getDownloadManager(): DownloadManager {
		return appContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
	}
}