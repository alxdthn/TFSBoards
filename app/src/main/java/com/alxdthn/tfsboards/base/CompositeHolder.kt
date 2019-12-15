package com.alxdthn.tfsboards.base

import io.reactivex.disposables.CompositeDisposable

interface CompositeHolder {
	fun getCompositeDisposable(): CompositeDisposable
}