package com.alxdthn.tfsboards.ui.teams.fragment

import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.utilities.extensions.observe
import kotlinx.android.synthetic.main.layout_teams_menu_header.view.*

class TeamsDataObserver {

	fun bind(main: TeamsFragment) {
		observeUserData(main)
		observeTeamsData(main)
		observeUserAvatar(main)
	}

	private fun observeUserData(main: TeamsFragment) {
		main.observe({ viewModel.userData }, { user ->
			headerView.txvUserName.text = getString(R.string.user_name, user.username)
			headerView.txvFullName.text = user.fullName
		})
	}

	private fun observeTeamsData(main: TeamsFragment) {
		main.observe({ viewModel.teamsData }, { teams ->
			itemsHandler render teams
		})
	}

	private fun observeUserAvatar(main: TeamsFragment) {
		main.observe({ viewModel.avatarBitmap }, { bitmap ->
			headerView.avUserAvatar.setImageBitmap(bitmap)
		})
	}
}