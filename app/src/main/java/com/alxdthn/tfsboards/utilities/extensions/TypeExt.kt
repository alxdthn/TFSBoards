package com.alxdthn.tfsboards.utilities.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.base.CompositeHolder
import com.alxdthn.tfsboards.base.LocalState
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

inline fun <T : CompositeHolder> T.subscribeToGlobal(crossinline block: T.(Events) -> Unit) {
	GlobalEvent.subscribe { block(it) }.addTo(getCompositeDisposable())
}

inline fun <T, K> T.observe(data: T.() -> LiveData<K>, crossinline block: T.(K) -> Unit) {
	try {
		data().observe(this as LifecycleOwner, Observer { block(it) })
	} catch (e: Throwable) {
		e.printStackTrace()
	}
}

inline fun <T> T.observeState(state: T.() -> LocalState, crossinline block: T.(Boolean) -> Unit) {
	state().observe(this as LifecycleOwner, Observer { block(it) })
}

inline fun <T : CompositeHolder, K> T.subscribe(
	ps: T.() -> PublishSubject<K>,
	crossinline block: T.(K) -> Unit
) {
	ps().subscribe { block(it) }.addTo(getCompositeDisposable())
}