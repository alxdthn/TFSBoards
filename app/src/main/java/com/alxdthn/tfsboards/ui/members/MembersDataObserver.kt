package com.alxdthn.tfsboards.ui.members

import com.alxdthn.tfsboards.utilities.extensions.observe

class MembersDataObserver {

	fun bind(main: MembersFragment) {
		observeMembers(main)
	}

	private fun observeMembers(main: MembersFragment) {
		main.observe({ viewModel.resultMembers }) { resultMembers ->
			itemsHandler.render(resultMembers)
		}
	}
}
