package com.alxdthn.tfsboards.ui.members

import androidx.lifecycle.Observer

class MembersDataObserver {

	fun bind(main: MembersFragment) {
		observeMembers(main)
	}

	private fun observeMembers(main: MembersFragment) = main.apply {
		viewModel.resultMembers.observe(viewLifecycleOwner, Observer { resultMembers ->
			itemsHandler.render(resultMembers)
		})
	}
}
