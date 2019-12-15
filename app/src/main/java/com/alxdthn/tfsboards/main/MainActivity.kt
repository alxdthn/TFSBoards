package com.alxdthn.tfsboards.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alxdthn.tfsboards.*
import com.alxdthn.tfsboards.base.CompositeHolder
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.utilities.AppConstants.UNAUTHORIZED
import com.alxdthn.tfsboards.utilities.DialogListener
import com.alxdthn.tfsboards.utilities.extensions.subscribeToGlobal
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CompositeHolder {

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private val viewModel by viewModels<MainViewModel> { viewModelFactory }

	private val fragmentApi = FragmentApi(supportFragmentManager, R.id.flFragmentHolder)
	private val navigationHandler = NavigationHandler(fragmentApi)

	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		inject()
		init()
		if (savedInstanceState == null) {
			viewModel.onCreate()
		}
	}

	private fun init() {
		subscribeToGlobal { event ->
			navigationHandler.apply {
				if (event == viewModel.current) return@apply
				when (event) {
					Events.RESTART -> restart()
					Events.CLOSE -> close()
					Events.LOGOUT -> logout()
					Events.AUTH -> showAuthFragment()
					Events.ERROR -> showNoConnectionFragment()
					Events.TEAMS -> showTeamsFragment()
					Events.BOARD -> showBoardFragment()
					Events.CARD -> showCardFragment()
					Events.MEMBERS -> showBoardMembersFragment()
					Events.FILTER -> showFilterFragment()
					else -> Unit
				}
				viewModel.current = event
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()

		compositeDisposable.dispose()
	}

	fun handleError(errorCode: Int) {
		when (errorCode) {
			UNAUTHORIZED -> GlobalEvent.set(Events.AUTH)
			else -> GlobalEvent.set(Events.ERROR)
		}
	}

	fun getListener(tag: String): DialogListener {
		val fragment = supportFragmentManager.findFragmentByTag(tag)

		try {
			return fragment as DialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException(
				fragment.toString() + "must implement DialogListener"
			)
		}
	}

	private fun inject() {
		val injector: AppComponent = (application as App).getAppComponent()

		injector.inject(this)
	}

	override fun getCompositeDisposable() = compositeDisposable
}
