package com.alxdthn.tfsboards.ui.filter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdthn.tfsboards.base.BaseViewModel
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.local.AppColor
import com.alxdthn.tfsboards.model.local.BoardData
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_KEY
import com.alxdthn.tfsboards.utilities.extensions.forEachCard
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel(), TextWatcher {

	private var cards: MutableList<Card> = mutableListOf()

	private val _colors = MutableLiveData<AppColor>()
	val colors: LiveData<AppColor> = _colors

	private val _filteredCards = MutableLiveData<List<Card>>()
	val filteredData: LiveData<List<Card>> = _filteredCards

	private val _input = MutableLiveData<CharSequence>()
	val input: LiveData<CharSequence> = _input

	override fun initializer() {
		val args = GlobalEvent.args()
		val boardData = args[BOARD_KEY] as BoardData

		boardData.columns.forEachCard {
			cards.add(it)
		}
		_colors.value = boardData.color!!
		_filteredCards.value = cards
	}

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
		_input.value = s
		_filteredCards.value = getFiltered(s ?: "")
	}

	private fun getFiltered(s: CharSequence): List<Card> {
		val res = cards.filter { it.name.contains(s, ignoreCase = true) }
		return res
	}

	override fun onCleared() {
		super.onCleared()
		GlobalEvent.set(Events.FILTER_CLOSE)
	}

	fun showCard(idCard: String) {
		GlobalEvent.set(Events.CLOSE)
		GlobalEvent.set(Events.FILTER_SHOW_CARD, mapOf(Pair(CARD_KEY, idCard)))
	}

	override fun afterTextChanged(s: Editable?) = Unit

	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

	companion object {
		fun start(boardData: BoardData) {
			val args = mapOf(
				Pair(BOARD_KEY, boardData)
			)

			GlobalEvent.set(Events.FILTER, args)
		}
	}
}