package com.alxdthn.tfsboards.model

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

object GlobalEvent {

	private var event: Events =
		Events.NOTHING
		set(value) {
			field = value
			eventSubject.onNext(value)
		}

	private var data: Map<String, Any?>? = null
		get() {
			val res = field
			field = null
			return res
		}

	private val eventSubject = BehaviorSubject.create<Events>()

	fun set(new: Events): Boolean {
		data = emptyMap()
		event = new
		return true
	}

	fun set(new: Events, data: Map<String, Any?>): Boolean {
		GlobalEvent.data = data
		event = new
		return true
	}

	fun subscribe(block: (Events) -> Unit): Disposable {
		return eventSubject.subscribe(block)
	}

	fun now(check: Events): Boolean {
		return event == check
	}

	fun args(): Map<String, Any?> {
		return data ?: emptyMap()
	}
}