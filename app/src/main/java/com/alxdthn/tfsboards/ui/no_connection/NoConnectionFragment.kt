package com.alxdthn.tfsboards.ui.no_connection

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.model.Events
import kotlinx.android.synthetic.main.fragment_no_connection.view.*

/**
 * 		Фрагмент - заглушка. Реализовывать для него MVVM думаю нет смысла.
 */

class NoConnectionFragment : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_no_connection, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		view.txvNoInternet.text = when (activity!!.resources.configuration.orientation) {
			Configuration.ORIENTATION_PORTRAIT -> getString(R.string.no_internet_title)
			else -> getString(R.string.no_internet_title).replace("\n", " ")
		}
		view.btnTryAgain.setOnClickListener {
			it.isEnabled = false
			Handler().postDelayed({
				it.isEnabled = true
				GlobalEvent.set(Events.TEAMS)
			}, 500)
		}
	}
}