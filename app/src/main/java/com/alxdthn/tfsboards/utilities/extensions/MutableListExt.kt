package com.alxdthn.tfsboards.utilities.extensions

import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.model.local.Column

fun <T> MutableList<T>.removeFirstIf(filter: (T) -> Boolean): Boolean {
	val each = this.iterator()

	while (each.hasNext()) {
		if (filter(each.next())) {
			each.remove()
			return true
		}
	}
	return false
}

fun <T> MutableList<T>.replaceAll(elements: Collection<T>) {
	this.clear()
	this.addAll(elements)
}

infix fun MutableList<Column>.forEachCard(block: (Card) -> Unit) {
	forEach { column -> column.cards.forEach(block) }
}