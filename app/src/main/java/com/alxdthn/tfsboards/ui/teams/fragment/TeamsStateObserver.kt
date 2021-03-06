package com.alxdthn.tfsboards.ui.teams.fragment

import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.utilities.extensions.*
import kotlinx.android.synthetic.main.fragment_teams.*
import kotlinx.android.synthetic.main.layout_teams_content.*
import kotlinx.android.synthetic.main.layout_teams_stub.*

class TeamsStateObserver {

	fun bind(main: TeamsFragment) {
		onUpdatingData(main)
		onDownloadingData(main)
	}

	private fun onUpdatingData(main: TeamsFragment) = main.apply {
		viewModel.updatingData.observe(viewLifecycleOwner, Observer { updating ->
			srlRefreshBoardsList.isRefreshing = updating
		})
	}

	private fun onDownloadingData(main: TeamsFragment) = main.apply {
		viewModel.downloadingTeams.observe(viewLifecycleOwner, Observer { downloading ->
			flBoardsStub.gone()
			rvBoards goneIf downloading
			pbTeamsProgressBar hereIf downloading
		})
	}
}