package com.alxdthn.tfsboards.ui.filter

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseFragment
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.model.Events
import com.alxdthn.tfsboards.model.GlobalEvent
import com.alxdthn.tfsboards.ui.filter.adapter.FilterItemsHandler
import com.alxdthn.tfsboards.utilities.AppAnimator
import com.alxdthn.tfsboards.utilities.extensions.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class FilterFragment : BaseFragment(R.layout.fragment_filter), Animation.AnimationListener {

	val viewModel by viewModels<FilterViewModel> { viewModelFactory }

	lateinit var itemsHandler: FilterItemsHandler

	private var enter = false

	override fun initViewModel() {
		viewModel.init()
	}

	override fun initializeUi() {
		itemsHandler = FilterItemsHandler(this)
		mainActivity.softInputAdjustResize()
		initToolbar()
		initRecycler()
		edxFilterInput.addTextChangedListener(viewModel)
	}

	override fun initializeObservers() {
		FilterDataObserver().bind(this)
		subscribe({ itemsHandler.onItemClick }) { viewModel.showCard(it.id) }
	}

	private fun initToolbar() {
		btnToolbarDetails.gone()
		txvToolbarTitle.text = getString(R.string.filter_toolbar_title)
		btnToolbarBack.setOnClickListener { GlobalEvent.set(Events.CLOSE) }
	}

	private fun initRecycler() {
		val orientation = GridLayoutManager.VERTICAL
		val spanCount: Int = if (orientationPortrait()) 2 else 4

		rvFilterFragmentCards.layoutManager = StaggeredGridLayoutManager(spanCount, orientation)
		rvFilterFragmentCards.adapter = itemsHandler.adapter
	}

	override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
		this.enter = enter
		if (nextAnim == 0) {
			rvFilterFragmentCards.visible()
			edxFilterInput.visible()
			return null
		}
		val anim = AnimationUtils.loadAnimation(activity, nextAnim)
		anim.setAnimationListener(this)
		return anim
	}

	override fun onAnimationEnd(animation: Animation?) {
		if (enter) {
			rvFilterFragmentCards.visible()
			edxFilterInput.visible()
			edxFilterInput.showKeyboard()
			edxFilterInput.requestFocus()
			AppAnimator show rvFilterFragmentCards
			AppAnimator show edxFilterInput
		}
	}

	override fun onAnimationStart(animation: Animation?) {
		if (!enter) {
			hideKeyboard()
			AppAnimator hide rvFilterFragmentCards
			AppAnimator hide edxFilterInput
		}
	}

	override fun onAnimationRepeat(animation: Animation?) = Unit

	override fun inject(injector: AppComponent) = injector.inject(this)
}