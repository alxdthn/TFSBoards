package com.alxdthn.tfsboards.main

import android.app.Application
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.di.component.DaggerAppComponent
import com.alxdthn.tfsboards.utilities.AppActionFormatter
import com.alxdthn.tfsboards.utilities.AppColors

class App : Application() {

	private lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()

		appComponent = DaggerAppComponent
			.builder()
			.application(applicationContext)
			.build()

		AppColors.initColors(resources)
		AppActionFormatter.init(applicationContext)
	}

	fun getAppComponent(): AppComponent {
		return appComponent
	}
}