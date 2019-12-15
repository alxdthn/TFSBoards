package com.alxdthn.tfsboards.main

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alxdthn.tfsboards.model.FragmentAnim

class FragmentApi(
	private val manager: FragmentManager,
	@IdRes private val fragmentHolder: Int
) {

	companion object {
		const val ADD = 1
		const val REPLACE = 2
		private const val ERROR_TYPE = "Unknown type"
	}

	fun newFragment(
		type: Int,
		fragment: Fragment,
		tag: String? = null,
		anim: FragmentAnim? = null
	) {
		beginTransaction().apply {
			if (anim != null) {
				setCustomAnimations(
					anim.enter,
					anim.exit,
					anim.popEnter,
					anim.popExit
				)
			}
			when (type) {
				ADD -> add(fragmentHolder, fragment, tag)
				REPLACE -> replace(fragmentHolder, fragment, tag)
				else -> throw IllegalArgumentException(ERROR_TYPE)
			}
			addToBackStack(tag)
			commit()
		}
	}

	fun newFragment(
		type: Int,
		fragment: Fragment,
		transition: Int,
		tag: String? = null
	) {
		beginTransaction().apply {
			setTransition(transition)
			when (type) {
				ADD -> add(fragmentHolder, fragment, tag)
				REPLACE -> replace(fragmentHolder, fragment, tag)
				else -> throw IllegalArgumentException(ERROR_TYPE)
			}
			addToBackStack(tag)
			commit()
		}
	}

	fun removeFragment(fragment: Fragment) {
		beginTransaction()
			.remove(fragment)
			.commit()
	}

	fun clearBackStack() {
		val backStackCount = manager.backStackEntryCount
		for (x in 0..backStackCount) {
			manager.popBackStackImmediate()
		}
	}

	fun popBackStack() = manager.popBackStack()

	fun findFragmentByTag(tag: String) = manager.findFragmentByTag(tag)

	fun beginTransaction() = manager.beginTransaction()
}