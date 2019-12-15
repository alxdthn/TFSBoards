package com.alxdthn.tfsboards.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.utilities.AppDiffUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

abstract class BaseItemsHandler(
	private val compositeDisposable: CompositeDisposable,
	callback: AppDiffUtil.BaseDiffCallback,
	val adapter: BaseAdapter
) {

	var items = listOf<Item>()

	private val diffResult = PublishSubject.create<DiffUtil.DiffResult>()

	private val core = AppDiffUtil()
		.setCallback(callback)
		.dispatchTo(diffResult)

	init {
		diffResult.subscribe {
			it.dispatchUpdatesTo(adapter)
		}.addTo(compositeDisposable)
	}

	infix fun render(data: Iterable<Any>) {
		core.setSource(items)
			.setRenderer { renderer(data) }
			.onResult {
				items = it
				onResult(it)
			}
			.calculate(data)
			.addTo(compositeDisposable)
	}

	abstract fun renderer(data: Iterable<Any>): List<Item>

	abstract fun onResult(result: List<Item>)

	abstract fun onItemClick(item: Item, view: View)
}