package com.alxdthn.tfsboards.ui.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdthn.tfsboards.base.BaseViewModel
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_MEMBERS_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_KEY
import com.alxdthn.tfsboards.utilities.extensions.replaceAll
import java.lang.IllegalStateException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MembersViewModel @Inject constructor(): BaseViewModel() {

	private val _resultMembers = MutableLiveData<List<User>>()
	val resultMembers: LiveData<List<User>> = _resultMembers

	private lateinit var boardMembers: List<User>
	private lateinit var card: Card

	override fun initializer() {
		val args = GlobalEvent.args()

		boardMembers = args[BOARD_MEMBERS_KEY] as List<User>
		card = args[CARD_KEY] as Card
		_resultMembers.value = boardMembers.map {
			val copy = it.copy()
			if (card.members!!.contains(it)) {
				copy.checked = true
			}
			copy
		}
	}

	fun changeResult(id: String) {
		val members = resultMembers.value ?: throw IllegalStateException()
		val member = members.find { it.id == id } ?: throw IllegalStateException()

		member.checked = !member.checked
		_resultMembers.value = members
	}

	private fun prepareCard() {
		val result = resultMembers.value?.filter { it.checked } ?: emptyList()
		val forCard = boardMembers.filter { currentMember ->
			result.find { resultMember -> resultMember.id == currentMember.id } != null
		}

		(card.members as MutableList<User>).replaceAll(forCard)
		card.idMembers.replaceAll(result.map { it.id })
	}

	override fun onCleared() {
		super.onCleared()
		try {
			prepareCard()
			GlobalEvent.set(Events.MEMBERS_CLOSE)
		} catch (e: Throwable) {
			e.printStackTrace()
		}
	}

	companion object {
		fun start(card: Card, boardMembers: List<User>) {
			val args = mapOf(
				Pair(CARD_KEY, card),
				Pair(BOARD_MEMBERS_KEY, boardMembers)
			)

			GlobalEvent.set(Events.MEMBERS, args)
		}
	}
}