package com.alxdthn.tfsboards.base

import io.reactivex.disposables.CompositeDisposable

interface CompositeHolder {
	val compositeDisposable: CompositeDisposable
}