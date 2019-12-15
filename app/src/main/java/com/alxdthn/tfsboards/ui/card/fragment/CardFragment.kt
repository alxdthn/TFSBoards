package com.alxdthn.tfsboards.ui.card.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.FragmentInputHandler
import com.alxdthn.tfsboards.custom_view.SpannableLayoutManager
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.local.Attachment
import com.alxdthn.tfsboards.ui.card.fragment.card_actions_adapter.CardActionsItemsHandler
import com.alxdthn.tfsboards.ui.card.fragment.card_attachments_adapter.CardAttachmentsItemsHandler
import com.alxdthn.tfsboards.ui.card.fragment.card_members_adapter.CardMembersItemsHandler
import com.alxdthn.tfsboards.ui.card.view_model.CardViewModel
import com.alxdthn.tfsboards.ui.dialogs.RemoveItemDialogFragment
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.DELETE_CARD_REQUEST
import com.alxdthn.tfsboards.utilities.DialogListener
import com.alxdthn.tfsboards.utilities.extensions.setAppColors
import com.alxdthn.tfsboards.utilities.extensions.showPopUpMenu
import com.alxdthn.tfsboards.utilities.extensions.showToastBy
import kotlinx.android.synthetic.main.layout_card_actions.*
import kotlinx.android.synthetic.main.layout_card_attachments.*
import kotlinx.android.synthetic.main.layout_card_content_scrolling.*
import kotlinx.android.synthetic.main.layout_card_members.*

class CardFragment : FragmentInputHandler(R.layout.fragment_card), DialogListener {

	lateinit var viewModel: CardViewModel

	lateinit var headerHandler: CardHeaderHandler
	private lateinit var clickHandler: CardClickHandler

	lateinit var actionItemsHandler: CardActionsItemsHandler
	lateinit var membersItemsHandler: CardMembersItemsHandler
	lateinit var attachmentsItemsHandler: CardAttachmentsItemsHandler

	var tmpItemId: String? = null

	override fun initViewModel() {
		viewModelInputHandler = ViewModelProviders.of(this, viewModelFactory)
			.get(CardViewModel::class.java)
		viewModel = viewModelInputHandler as CardViewModel
		viewModel.init()
	}

	override fun initializeUi() {
		initMembersRecycler()
		initAttachmentsRecycler()
		initActionsRecycler()
		srlCardFragmentRefresh.setAppColors()
		clickHandler = CardClickHandler(this)
		headerHandler = CardHeaderHandler(this)
	}

	override fun initializeObservers() {
		CardStateObserver().bind(this)
		CardDataObserver().bind(this)
		CardEventObserver().bind(this)
	}

	private fun initMembersRecycler() {
		membersItemsHandler = CardMembersItemsHandler(this)
		rvCardFragmentMembers.adapter = membersItemsHandler.adapter
	}

	private fun initAttachmentsRecycler() {
		attachmentsItemsHandler = CardAttachmentsItemsHandler(this)
		rvCardFragmentAttachments.adapter = attachmentsItemsHandler.adapter
		rvCardFragmentAttachments.layoutManager =
			SpannableLayoutManager(context!!, 3, attachmentsItemsHandler.adapter)
	}

	private fun initActionsRecycler() {
		actionItemsHandler = CardActionsItemsHandler(this)
		rvCardFragmentActions.adapter = actionItemsHandler.adapter
	}

	fun showCardMenu(view: View) {
		showPopUpMenu(view, R.menu.card_menu, clickHandler)
	}

	fun showAttachmentMenu(id: String, view: View) {
		tmpItemId = id
		showPopUpMenu(view, R.menu.attachment_menu, clickHandler)
	}

	fun showAttachment(attachment: Attachment) {
		val intent = Intent(Intent.ACTION_VIEW)

		intent.setDataAndType(Uri.parse(attachment.url), attachment.mimeType)
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			startActivity(intent)
		} catch (e: ActivityNotFoundException) {
			showToastBy(R.string.if_cant_open_file_message)
		}
	}

	fun startRemoveCardDialog() {
		val message = getString(R.string.remove_card_dialog_message)

		RemoveItemDialogFragment.show(
			childFragmentManager,
			message,
			viewModel.getCardData().idColumn,
			DELETE_CARD_REQUEST,
			CARD_FRAGMENT
		)
	}

	override fun onDialogAnswer(answer: Any) {
		when (answer) {
			is RemoveItemDialogFragment.Answer -> {
				if (answer.accepted) {
					viewModel.deleteCard()
					GlobalEvent.set(Events.CLOSE)
				}
			}
		}
	}

	override fun inject(injector: AppComponent) = injector.inject(this)
}