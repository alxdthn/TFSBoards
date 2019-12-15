package com.alxdthn.tfsboards.main

import androidx.fragment.app.FragmentTransaction
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.FragmentAnim
import com.alxdthn.tfsboards.ui.auth.AuthFragment
import com.alxdthn.tfsboards.ui.board.fragment.BoardFragment
import com.alxdthn.tfsboards.ui.card.fragment.CardFragment
import com.alxdthn.tfsboards.ui.filter.FilterFragment
import com.alxdthn.tfsboards.ui.members.MembersFragment
import com.alxdthn.tfsboards.ui.no_connection.NoConnectionFragment
import com.alxdthn.tfsboards.ui.teams.fragment.TeamsFragment
import com.alxdthn.tfsboards.utilities.AppConstants
import com.alxdthn.tfsboards.utilities.AppConstants.AUTH_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.BOARD_MEMBERS_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.CARD_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.FILTER_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.NO_CONNECTION_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.TEAMS_FRAGMENT
import javax.inject.Inject

class NavigationHandler(private val fragmentApi: FragmentApi) {

	private val verticalSlideAnim = FragmentAnim(
		R.anim.enter_vert,
		R.anim.exit_vert,
		R.anim.pop_enter_vert,
		R.anim.pop_exit_vert
	)

	private val horizontalSlideAnim = FragmentAnim(
		R.anim.enter,
		R.anim.exit,
		R.anim.pop_enter,
		R.anim.pop_exit
	)

	fun restart() {
		fragmentApi.clearBackStack()
	}

	fun close() {
		fragmentApi.popBackStack()
	}

	fun logout() {
		showAuthFragment()
	}

	fun showAuthFragment() {
		fragmentApi.newFragment(
			FragmentApi.REPLACE,
			AuthFragment(),
			AUTH_FRAGMENT,
			horizontalSlideAnim
		)
	}

	fun showNoConnectionFragment() {
		fragmentApi.newFragment(
			FragmentApi.REPLACE,
			NoConnectionFragment(),
			NO_CONNECTION_FRAGMENT,
			horizontalSlideAnim
		)
	}

	fun showTeamsFragment() {
		fragmentApi.newFragment(
			FragmentApi.REPLACE,
			TeamsFragment(),
			FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
			TEAMS_FRAGMENT
		)
	}

	fun showBoardFragment() {
		fragmentApi.newFragment(
			FragmentApi.ADD,
			BoardFragment(),
			BOARD_FRAGMENT,
			horizontalSlideAnim
		)
	}

	fun showCardFragment() {
		fragmentApi.newFragment(
			FragmentApi.ADD,
			CardFragment(),
			CARD_FRAGMENT,
			horizontalSlideAnim
		)
	}

	fun showBoardMembersFragment() {
		fragmentApi.newFragment(
			FragmentApi.ADD,
			MembersFragment(),
			FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
			BOARD_MEMBERS_FRAGMENT
		)
	}

	fun showFilterFragment() {
		fragmentApi.newFragment(
			FragmentApi.ADD,
			FilterFragment(),
			FILTER_FRAGMENT,
			verticalSlideAnim
		)
	}
}