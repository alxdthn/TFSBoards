package com.alxdthn.tfsboards.di.module

import android.util.Log
import com.alxdthn.tfsboards.di.scope.AppScope
import com.alxdthn.tfsboards.network.services.TrelloBoardService
import com.alxdthn.tfsboards.network.services.TrelloCardService
import com.alxdthn.tfsboards.network.services.TrelloListService
import com.alxdthn.tfsboards.network.services.TrelloUserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TrelloServicesModule {

	@Provides
	@AppScope
	internal fun provideTrelloUserService(retrofit: Retrofit): TrelloUserService {
		return retrofit.create(TrelloUserService::class.java)
	}

	@Provides
	@AppScope
	internal fun provideTrelloBoardService(retrofit: Retrofit): TrelloBoardService {
		return retrofit.create(TrelloBoardService::class.java)
	}

	@Provides
	@AppScope
	internal fun provideTrelloListService(retrofit: Retrofit): TrelloListService {
		return retrofit.create(TrelloListService::class.java)
	}

	@Provides
	@AppScope
	internal fun provideTrelloCardService(retrofit: Retrofit): TrelloCardService {
		return retrofit.create(TrelloCardService::class.java)
	}
}