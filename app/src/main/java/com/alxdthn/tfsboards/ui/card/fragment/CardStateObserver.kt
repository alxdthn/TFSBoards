package com.alxdthn.tfsboards.ui.card.fragment

import android.util.Log
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.utilities.extensions.*
import kotlinx.android.synthetic.main.layout_card_content_header.*
import kotlinx.android.synthetic.main.layout_card_content_scrolling.*
import kotlinx.android.synthetic.main.layout_card_actions.*
import kotlinx.android.synthetic.main.layout_card_attachments.*
import kotlinx.android.synthetic.main.layout_card_description.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CardStateObserver {

	fun bind (main: CardFragment) {
		onEdit(main)
		onEditDescription(main)
		onEditName(main)
		onUpdatingCard(main)
		onDownloadingActions(main)
		onAttachmentsOpen(main)
		onActionsOpen(main)
	}

	private fun onActionsOpen(main: CardFragment) {
		main.observeState({ viewModel.actionsOpen }) { open ->
			rvCardFragmentActions hereIf open
		}
	}

	private fun onAttachmentsOpen(main: CardFragment) {
		main.observeState({ viewModel.attachmentsOpen }) { open ->
			flCardFragmentAttachmentsContainer hereIf open
		}
	}

	private fun onUpdatingCard(main: CardFragment) {
		main.observeState({ viewModel.updatingCard }) { updatingCard ->
			srlCardFragmentRefresh.isRefreshing = updatingCard
		}
	}

	private fun onDownloadingActions(main: CardFragment) {
		main.observeState({ viewModel.downloadingActions }) { downloading ->
			pbCardFragmentActionsProgress hereIf downloading
		}
	}

	private fun onEditName(main: CardFragment) {
		main.observeState({ viewModel.editName }) { onEditName ->
			edxCardFragmentName visibleIf onEditName
			txvCardFragmentName invisibleIf onEditName
			txvToolbarTitle.text = if (onEditName) {
				selectedEditText = edxCardFragmentName
				getString(R.string.edit_card_name_toolbar_title)
			} else {
				viewModel.getCardData().name
			}
		}
	}

	private fun onEditDescription(main: CardFragment) {
		main.observeState({ viewModel.editDescription }) { onEditDescription ->
			txvCardFragmentDescription invisibleIf onEditDescription
			edxCardFragmentDescription visibleIf onEditDescription
			txvToolbarTitle.text = if (onEditDescription) {
				selectedEditText = edxCardFragmentDescription
				getString(R.string.edit_card_description_toolbar_title)
			} else {
				viewModel.getCardData().name
			}
		}
	}

	private fun onEdit(main: CardFragment) {
		main.observeState({ viewModel.onEdit }) { onEdit ->
			headerHandler.onEdit = onEdit
			txvToolbarTitle visibleIf onEdit
			btnToolbarBack invisibleIf onEdit
			btnToolbarDetails invisibleIf onEdit
			btnToolbarCancelInput visibleIf onEdit
			btnToolbarAcceptInput visibleIf onEdit
		}
	}
}