package com.alxdthn.tfsboards.ui.members

import androidx.fragment.app.viewModels
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseFragment
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.ui.members.adapter.MembersItemsHandler
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.android.synthetic.main.layout_members_header.*

class MembersFragment : BaseFragment(R.layout.fragment_members) {

	val viewModel by viewModels<MembersViewModel> { viewModelFactory }

	lateinit var itemsHandler: MembersItemsHandler

	override fun initViewModel() {
		viewModel.init()
	}

	override fun initializeUi() {
		itemsHandler = MembersItemsHandler(this)
		rvBoardMembers.adapter = itemsHandler.adapter

		btnMembersFragmentClose.setOnClickListener { GlobalEvent.set(Events.CLOSE) }
		btnMembersFragmentAccept.setOnClickListener { GlobalEvent.set(Events.CLOSE) }
	}

	override fun initializeObservers() {
		MembersDataObserver().bind(this)
	}

	override fun inject(injector: AppComponent) = injector.inject(this)
}