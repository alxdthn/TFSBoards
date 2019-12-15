package com.alxdthn.tfsboards.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseRepository {

	private val subscribeThread = Schedulers.io()
	private val observeThread = AndroidSchedulers.mainThread()

	fun <T> preparedSingle(single: Single<T>): Single<T> {
		return single.subscribeOn(subscribeThread)
			.observeOn(observeThread)
			.cache()
	}
}