package com.alxdthn.tfsboards.ui.card.fragment

import com.alxdthn.tfsboards.utilities.extensions.hereIf
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.layout_card_attachments.*
import kotlinx.android.synthetic.main.layout_card_members.*

class CardEventObserver {

	fun bind(main: CardFragment) {
		onError(main)
		onOpenAttachment(main)
		onAttachmentClick(main)
		onMembersChanged(main)
		onAttachmentsChanged(main)
		onActionsDownloaded(main)
	}

	private fun onAttachmentClick(main: CardFragment) = main.apply {
		attachmentsItemsHandler.onAttachmentClick.subscribe { pair ->
			showAttachmentMenu(pair.first.id, pair.second)
		}.addTo(compositeDisposable)
	}

	private fun onOpenAttachment(main: CardFragment) = main.apply {
		viewModel.openAttachment.subscribe { attachment ->
			showAttachment(attachment)
		}.addTo(compositeDisposable)
	}

	private fun onAttachmentsChanged(main: CardFragment) = main.apply {
		viewModel.attachmentsChanged.subscribe { attachments ->
			txvCardFragmentAttachmentsStub hereIf attachments.isNullOrEmpty()
			if (attachments != null) attachmentsItemsHandler render attachments
		}.addTo(compositeDisposable)
	}

	private fun onMembersChanged(main: CardFragment) = main.apply {
		viewModel.membersChanged.subscribe { idMembers ->
			txvCardFragmentMembersTitle hereIf idMembers.isEmpty()
			membersItemsHandler render idMembers
		}.addTo(compositeDisposable)
	}

	private fun onActionsDownloaded(main: CardFragment) = main.apply {
		viewModel.actionsDownloaded.subscribe { actions ->
			actionItemsHandler render actions
		}.addTo(compositeDisposable)
	}

	private fun onError(main: CardFragment) = main.apply {
		viewModel.errorCode.subscribe { error ->
			handleError(error)
		}.addTo(compositeDisposable)
	}
}