package com.alxdthn.tfsboards.ui.teams.fragment

import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.R
import kotlinx.android.synthetic.main.layout_teams_menu_header.view.*

class TeamsDataObserver {

	fun bind(main: TeamsFragment) {
		observeUserData(main)
		observeTeamsData(main)
		observeUserAvatar(main)
	}

	private fun observeUserData(main: TeamsFragment) = main.apply {
		viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
			headerView.txvUserName.text = getString(R.string.user_name, user.username)
			headerView.txvFullName.text = user.fullName
		})
	}

	private fun observeTeamsData(main: TeamsFragment) = main.apply {
		viewModel.teamsData.observe(viewLifecycleOwner, Observer { teams ->
			itemsHandler render teams
		})
	}

	private fun observeUserAvatar(main: TeamsFragment) = main.apply {
		viewModel.avatarBitmap.observe(viewLifecycleOwner, Observer { bitmap ->
			headerView.avUserAvatar.setImageBitmap(bitmap)
		})
	}
}