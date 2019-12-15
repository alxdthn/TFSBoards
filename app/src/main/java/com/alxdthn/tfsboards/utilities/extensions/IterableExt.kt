package com.alxdthn.tfsboards.utilities.extensions

infix fun <T> Iterable<T>.indexOf(predicate: (T) -> Boolean): Int {
	for ((i, element) in this.withIndex()) {
		if (predicate(element)) return i
	}
	return -1
}

fun <T, R> Iterable<T>.filteredMap(predicate: (T) -> Boolean, transform: (T) -> R): List<R> {
	val iterator = this.iterator()
	val result = mutableListOf<R>()

	while (iterator.hasNext()) {
		val item = iterator.next()

		if (predicate(item)) {
			result.add(transform(item))
		}
	}
	return result
}