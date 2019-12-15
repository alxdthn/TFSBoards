package com.alxdthn.tfsboards.ui.filter.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpanned
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.CardItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.ui.filter.FilterFragment
import com.alxdthn.tfsboards.utilities.AppDiffUtil
import com.alxdthn.tfsboards.utilities.extensions.observe
import com.alxdthn.tfsboards.utilities.extensions.subscribe
import io.reactivex.subjects.PublishSubject

@Suppress("UNCHECKED_CAST")
class FilterItemsHandler(
	main: FilterFragment
) : BaseItemsHandler(main.getCompositeDisposable(), DiffCallback(), FilterCardAdapter()) {

	private var currentInput: CharSequence? = null
	private val color = ContextCompat.getColor(main.context!!, R.color.colorAccent)

	val onItemClick = PublishSubject.create<CardItem>()

	init {
		main.observe({ viewModel.input }) {
			currentInput = it
		}
		(adapter as FilterCardAdapter).itemsHandler = this
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val cards = data as List<Card>

		return cards.map { card ->
			val indexes = initIndexes(card.name)

			if (indexes.isNotEmpty()) {
				CardItem(spannedName = spanMatches(card.name, indexes)) render card
			} else CardItem() render card
		}
	}

	private fun spanMatches(name: String, indexes: List<Int>): SpannableString {
		val spannable = SpannableString(name)

		indexes.forEach { start ->
			val end = start + currentInput?.length!!
			spannable[start, end] = StyleSpan(Typeface.BOLD)
			spannable[start, end] = ForegroundColorSpan(color)
		}
		return spannable
	}

	private fun initIndexes(name: String): List<Int> {
		val result = mutableListOf<Int>()

		if (!currentInput.isNullOrEmpty()) {
			val len = name.length
			val inputLen = currentInput?.length ?: 0
			var index = 0

			while (index < len) {
				if (index + inputLen > len) return result
				val sub = name.substring(index, index + inputLen)

				if (sub.contains(currentInput!!, ignoreCase = true)) {
					result.add(index)
					index += inputLen
				} else {
					index++
				}
			}
		}
		return result
	}

	override fun onItemClick(item: Item, view: View) {
		onItemClick.onNext(item as CardItem)
	}

	override fun onResult(result: List<Item>) = Unit

	class DiffCallback : AppDiffUtil.BaseDiffCallback() {

		override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
			val oldItem = getOldItems()[oldPos] as CardItem
			val newItem = getNewItems()[newPos] as CardItem

			if (oldItem.spannedName.hashCode() != newItem.spannedName.hashCode()) {
				return newItem.spannedName ?: SpannableString(newItem.name)
			}
			return null
		}
	}
}