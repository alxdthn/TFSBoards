package com.alxdthn.tfsboards.di.module

import com.alxdthn.tfsboards.di.scope.AppScope
import com.alxdthn.tfsboards.network.TrelloInterceptor
import com.alxdthn.tfsboards.utilities.AppConstants.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

	@Provides
	@AppScope
	internal fun provideRetrofit(client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(client)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@AppScope
	internal fun provideOkHttpClient(interceptor: TrelloInterceptor): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(interceptor)
			.callTimeout(100, TimeUnit.SECONDS)
			.connectTimeout(100, TimeUnit.SECONDS)
			.readTimeout(100, TimeUnit.SECONDS)
			.writeTimeout(100, TimeUnit.SECONDS)
			.build()
	}
}