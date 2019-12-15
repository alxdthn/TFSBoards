package com.alxdthn.tfsboards.ui.card.fragment

import com.alxdthn.tfsboards.utilities.extensions.hereIf
import com.alxdthn.tfsboards.utilities.extensions.subscribe
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

	private fun onAttachmentClick(main: CardFragment) {
		main.subscribe({ attachmentsItemsHandler.onAttachmentClick }) { pair ->
			showAttachmentMenu(pair.first.id, pair.second)
		}
	}

	private fun onOpenAttachment(main: CardFragment) {
		main.subscribe({ viewModel.openAttachment }) { attachment ->
			showAttachment(attachment)
		}
	}

	private fun onAttachmentsChanged(main: CardFragment) {
		main.subscribe({ viewModel.attachmentsChanged }) { attachments ->
			txvCardFragmentAttachmentsStub hereIf attachments.isNullOrEmpty()
			if (attachments != null) attachmentsItemsHandler render attachments
		}
	}

	private fun onMembersChanged(main: CardFragment) {
		main.subscribe({ viewModel.membersChanged }) { idMembers ->
			txvCardFragmentMembersTitle hereIf idMembers.isEmpty()
			membersItemsHandler render idMembers
		}
	}

	private fun onActionsDownloaded(main: CardFragment) {
		main.subscribe({ viewModel.actionsDownloaded }) { actions ->
			actionItemsHandler render actions
		}
	}

	private fun onError(main: CardFragment) {
		main.subscribe({ viewModel.errorCode }) { error ->
			handleError(error)
		}
	}
}