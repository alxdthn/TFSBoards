package com.alxdthn.tfsboards.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.utilities.AppConstants.UNKNOWN_ERROR
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

abstract class BaseViewModel : ViewModel(), CompositeHolder {

	var errorCode = PublishSubject.create<Int>()

	override val compositeDisposable = CompositeDisposable()

	private var ready = false

	fun init() {
		if (!ready) {
			try {
				initializer()
			} catch (e: ClassCastException) {
				GlobalEvent.set(Events.RESTART)
			}
			ready = true
		}
	}

	abstract fun initializer()

	fun handleError(error: Throwable) {
		Log.d("bestTAG", "ERROR: ${error.message.toString()}")
		error.printStackTrace()
		if (error is HttpException) {
			errorCode.onNext(error.code())
		} else {
			errorCode.onNext(UNKNOWN_ERROR)
		}
	}

	override fun onCleared() {
		super.onCleared()

		compositeDisposable.dispose()
	}
}