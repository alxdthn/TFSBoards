package com.alxdthn.tfsboards.di.module

import android.content.Context
import com.alxdthn.tfsboards.di.scope.AppScope
import com.alxdthn.tfsboards.preferences.AppPreferences
import dagger.Module
import dagger.Provides

@Module
object StorageModule {

	@Provides
	@AppScope
	internal fun provideSharedPreferences(appContext: Context): AppPreferences {
		return AppPreferences(appContext)
	}
}