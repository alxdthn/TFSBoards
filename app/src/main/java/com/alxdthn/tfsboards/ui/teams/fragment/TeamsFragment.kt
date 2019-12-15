package com.alxdthn.tfsboards.ui.teams.fragment

import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseFragment
import com.alxdthn.tfsboards.custom_view.SpannableLayoutManager
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.local.Board
import com.alxdthn.tfsboards.ui.dialogs.AddBoardDialogFragment
import com.alxdthn.tfsboards.ui.dialogs.RemoveItemDialogFragment
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.TeamsAdapter
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.TeamsAdapterObserver
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.TeamsItemsHandler
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.sticky_header.StickyHeader
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper.TouchHelperCallback
import com.alxdthn.tfsboards.ui.teams.view_model.TeamsViewModel
import com.alxdthn.tfsboards.utilities.AppConstants.TEAMS_FRAGMENT
import com.alxdthn.tfsboards.utilities.DialogListener
import com.alxdthn.tfsboards.utilities.extensions.orientationPortrait
import com.alxdthn.tfsboards.utilities.extensions.setAppColors
import kotlinx.android.synthetic.main.fragment_teams.*
import kotlinx.android.synthetic.main.layout_teams_content.*
import kotlinx.android.synthetic.main.layout_teams_stub.*
import kotlinx.android.synthetic.main.layout_teams_toolbar.*

class TeamsFragment : BaseFragment(R.layout.fragment_teams), DialogListener {

	val viewModel by viewModels<TeamsViewModel> { viewModelFactory }

	lateinit var itemsHandler: TeamsItemsHandler
	lateinit var headerView: View

	override fun initViewModel() {
		viewModel.init()
	}

	override fun initializeUi() {
		initializeMenu()
		initializeRecycler()
		initializeSwipeRefresh()
		btnAddNewBoard.setOnClickListener { startAddNewBoardDialog() }
	}

	override fun initializeObservers() {
		TeamsDataObserver().bind(this)
		TeamsStateObserver().bind(this)
		TeamsEventObserver().bind(this)
	}

	private fun initializeSwipeRefresh() {
		srlRefreshBoardsList.setOnRefreshListener { viewModel.updateTeams() }
		srlRefreshBoardsList.setAppColors()
	}

	private fun initializeMenu() {
		headerView = View.inflate(context, R.layout.layout_teams_menu_header, null)
		nvTeamsNavigation.addHeaderView(headerView)
		nvTeamsNavigation.menu.findItem(R.id.navLogout).setOnMenuItemClickListener {
			GlobalEvent.set(Events.LOGOUT)
		}
		btnTeamsOpenMenu.setOnClickListener {
			dlTeamsMenuDrawer.openDrawer(GravityCompat.START)
		}
	}

	private fun initializeRecycler() {
		itemsHandler = TeamsItemsHandler(this)
		val adapter = itemsHandler.adapter as TeamsAdapter
		rvBoards.adapter = adapter
		setLayoutManager()
		rvBoards.addItemDecoration(StickyHeader(adapter))
		ItemTouchHelper(TouchHelperCallback(adapter)).attachToRecyclerView(rvBoards)
		adapter.registerAdapterDataObserver(TeamsAdapterObserver(rvBoards, flBoardsStub))
	}

	private fun setLayoutManager() {
		rvBoards.layoutManager = if (orientationPortrait()) {
			LinearLayoutManager(context)
		} else {
			SpannableLayoutManager(context, 3, rvBoards.adapter)
		}
	}

	fun showRemoveDialog(board: Board) {
		val message = getString(R.string.remove_dialog_message, "доску", board.name)
		RemoveItemDialogFragment.show(
			childFragmentManager, message, board.id, 0, TEAMS_FRAGMENT
		)
	}

	private fun startAddNewBoardDialog() {
		val data = viewModel.teamsData.value
		AddBoardDialogFragment.show(childFragmentManager, data)
	}

	override fun onDialogAnswer(answer: Any) {
		when (answer) {
			is RemoveItemDialogFragment.Answer -> { when {
				answer.accepted -> viewModel.removeBoard(answer.idItem)
				else -> itemsHandler.notifyItemChanged(answer.idItem)
			} }
			is AddBoardDialogFragment.Answer -> { if (answer.accepted) {
				viewModel.uploadBoard(answer.boardName, answer.idTeam)
			} }
		}
	}

	override fun inject(injector: AppComponent) = injector.inject(this)
}