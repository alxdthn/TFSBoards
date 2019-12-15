package com.alxdthn.tfsboards.ui.card.fragment

import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import kotlinx.android.synthetic.main.layout_card_actions.*
import kotlinx.android.synthetic.main.layout_card_attachments.*
import kotlinx.android.synthetic.main.layout_card_content_header.*
import kotlinx.android.synthetic.main.layout_card_content_scrolling.*
import kotlinx.android.synthetic.main.layout_card_description.*
import kotlinx.android.synthetic.main.layout_card_members.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CardClickHandler(
	private val main: CardFragment
) : View.OnClickListener, MenuBuilder.Callback {

	init {
		initListeners()
	}

	override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem): Boolean {
		main.apply {
			when (item.itemId) {
				R.id.menuCardDelete -> startRemoveCardDialog()
				R.id.menuAttachmentShow -> viewModel.showAttachment(tmpItemId)
				R.id.menuAttachmentDelete -> viewModel.removeAttachment(tmpItemId)
				R.id.menuAttachmentDownload -> viewModel.downloadAttachment(tmpItemId)
			}
		}
		return true
	}

	override fun onMenuModeChange(menu: MenuBuilder?) = Unit

	override fun onClick(v: View) {
		main.apply {
			when (v.id) {
				R.id.btnToolbarBack -> GlobalEvent.set(Events.CLOSE)
				R.id.btnToolbarCancelInput -> viewModel.cancelInput()
				R.id.btnToolbarAcceptInput -> viewModel.acceptInput()
				R.id.btnToolbarDetails -> showCardMenu(v)
				R.id.txvCardFragmentDescription -> viewModel.startDescriptionEdit()
				R.id.txvCardFragmentName -> viewModel.startNameEdit()
				R.id.llCardFragmentMembersPart -> viewModel.showMembers()
				R.id.llCardFragmentAttachmentsTitle -> viewModel.showAttachments()
				R.id.llCardFragmentActionsLine -> viewModel.showActions()
			}
		}
	}

	private fun initListeners() {
		main.btnToolbarBack.setOnClickListener(this)
		main.btnToolbarAcceptInput.setOnClickListener(this)
		main.btnToolbarCancelInput.setOnClickListener(this)
		main.btnToolbarDetails.setOnClickListener(this)
		main.txvCardFragmentDescription.setOnClickListener(this)
		main.txvCardFragmentName.setOnClickListener(this)
		main.llCardFragmentMembersPart.setOnClickListener(this)
		main.llCardFragmentAttachmentsTitle.setOnClickListener(this)
		main.llCardFragmentActionsLine.setOnClickListener(this)
		main.rvCardFragmentMembers.setOnClickListener(this)
		main.srlCardFragmentRefresh.setOnRefreshListener {
			main.viewModel.downloadCard()
		}
	}
}