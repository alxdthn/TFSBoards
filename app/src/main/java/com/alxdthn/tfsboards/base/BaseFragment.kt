package com.alxdthn.tfsboards.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.main.App
import com.alxdthn.tfsboards.main.MainActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes private val layoutRes: Int) : Fragment(), CompositeHolder {

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	lateinit var mainActivity: MainActivity

	private val compositeDisposable = CompositeDisposable()

	abstract fun initViewModel()

	abstract fun initializeUi()

	abstract fun initializeObservers()

	override fun onAttach(context: Context) {
		super.onAttach(context)

		mainActivity = activity as MainActivity
		inject((mainActivity.application as App).getAppComponent())
		initViewModel()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(layoutRes, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		initializeUi()
		initializeObservers()
	}

	override fun onDestroy() {
		super.onDestroy()
		compositeDisposable.dispose()
	}

	infix fun handleError(error: Int) {
		mainActivity.handleError(error)
	}

	override fun getCompositeDisposable() = compositeDisposable

	abstract fun inject(injector: AppComponent)
}