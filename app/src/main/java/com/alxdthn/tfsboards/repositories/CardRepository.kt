package com.alxdthn.tfsboards.repositories

import com.alxdthn.tfsboards.base.BaseRepository
import com.alxdthn.tfsboards.model.local.Action
import com.alxdthn.tfsboards.model.local.Card
import com.alxdthn.tfsboards.model.responses.ActionResponse
import com.alxdthn.tfsboards.model.responses.CardResponse
import com.alxdthn.tfsboards.model.responses.ItemResponse
import com.alxdthn.tfsboards.network.services.TrelloCardService
import com.alxdthn.tfsboards.utilities.AppActionFormatter.ignoredActions
import io.reactivex.Single
import javax.inject.Inject


class CardRepository @Inject constructor(
	private val cardService: TrelloCardService
) : BaseRepository() {

	fun deleteAttachment(idCard: String, idAttachment: String): Single<ItemResponse> {
		return preparedSingle(cardService.deleteAttachment(idCard, idAttachment))
	}

	fun getCard(card: Card): Single<Unit> {
		return preparedSingle(cardService.getCard(card.id))
			.map { cardResponse -> prepareCard(card, cardResponse) }
	}

	fun getCardActions(card: Card): Single<List<Action>> {
		return preparedSingle(cardService.getActions(card.id))
			.map { actionsResponses -> prepareCard(card, actionsResponses) }
	}

	fun deleteCard(idCard: String): Single<ItemResponse> {
		return preparedSingle(cardService.deleteCardFromApi(idCard))
	}

	fun addNewCard(newCard: Card): Single<Unit> {
		return preparedSingle(cardService.postNewCardToApi(newCard.name, newCard.idColumn))
			.map { itemResponse -> prepareCard(newCard, itemResponse) }
	}

	fun updateCardMembers(card: Card): Single<ItemResponse> {
		return preparedSingle(cardService.updateCardMembers(
			id = card.id,
			idMembers = card.idMembers.joinToString(",")
		))
	}

	fun updateCard(card: Card): Single<ItemResponse> {
		return preparedSingle(cardService.putCardToApi(
			id = card.id,
			name = card.name,
			idList = card.idColumn,
			pos = card.pos.toString(),
			desc = card.desc
		))
	}

	private fun prepareCard(card: Card, response: CardResponse) {
		card.fromResponse(response)
	}

	private fun prepareCard(card: Card, response: ItemResponse) {
		card.id = response.id
		card.pos = response.pos
	}

	private fun prepareCard(card: Card, response: List<ActionResponse>): List<Action> {
		card.actions = mutableListOf()
		response.forEach { actionResponse ->
			if (!ignoredActions.contains(actionResponse.display.translationKey)) {
				card.actions?.add(Action().fromResponse(actionResponse))
			}
		}
		return card.actions as List<Action>
	}
}