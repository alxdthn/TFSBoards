package com.alxdthn.tfsboards.ui.card.fragment

import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.local.Action
import com.alxdthn.tfsboards.model.local.Attachment
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.utilities.extensions.goneIf
import com.alxdthn.tfsboards.utilities.extensions.observe
import kotlinx.android.synthetic.main.layout_card_attachments.*
import kotlinx.android.synthetic.main.layout_card_content_header.*
import kotlinx.android.synthetic.main.layout_card_description.*
import kotlinx.android.synthetic.main.layout_card_members.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CardDataObserver {

	fun bind(main: CardFragment) {
		observeCardData(main)
		observeCardPosition(main)
		observeToolbarColor(main)
	}

	private fun observeCardPosition(main: CardFragment) {
		main.observe({ viewModel.position }) { positionDescription ->
			txvCardFragmentPosition.text = positionDescription
		}
	}

	private fun observeToolbarColor(main: CardFragment) {
		main.observe({ viewModel.toolbarColor }) { color ->
			clToolbar.setBackgroundColor(color)
			clToolbar.outlineProvider = null
			llCardFragmentHeader.setCardBackgroundColor(color)
		}
	}

	private fun observeCardData(main: CardFragment) {
		main.observe({ viewModel.cardData }) { card ->
			setHeaderTitle(main, card.name)
			setCardName(main, card.name)
			setCardDescription(main, card.desc)
			setMembers(main, card.members)
			setAttachments(main, card.attachments)
			setActions(main, card.actions)
		}
	}

	private fun setActions(main: CardFragment, actions: List<Action>?) {
		val hasActions = !actions.isNullOrEmpty()
		if (hasActions) main.actionItemsHandler render actions!!
	}

	private fun setAttachments(main: CardFragment, attachments: List<Attachment>?) {
		val hasAttachments = !attachments.isNullOrEmpty()
		main.txvCardFragmentAttachmentsStub goneIf hasAttachments
		if (hasAttachments) main.attachmentsItemsHandler render attachments!!
	}

	private fun setMembers(main: CardFragment, members: List<User>?) {
		val hasMembers = !members.isNullOrEmpty()
		main.txvCardFragmentMembersTitle goneIf hasMembers
		if (hasMembers) main.membersItemsHandler render members!!
	}

	private fun setCardDescription(main: CardFragment, desc: String) {
		main.txvCardFragmentDescription.text = if (desc.isNotEmpty()) {
			desc
		} else {
			main.getString(R.string.card_description)
		}
	}

	private fun setHeaderTitle(main: CardFragment, title: String) {
		if (!main.viewModel.onEdit.isActive()) {
			main.txvToolbarTitle.text = title
		}
	}

	private fun setCardName(main: CardFragment, name: String) {
		main.txvCardFragmentName.text = name
	}
}