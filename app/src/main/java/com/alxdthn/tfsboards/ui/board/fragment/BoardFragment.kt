package com.alxdthn.tfsboards.ui.board.fragment

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.FragmentInputHandler
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.ui.board.fragment.adapter.BoardItemsHandler
import com.alxdthn.tfsboards.ui.board.fragment.adapter.ColumnAdapter
import com.alxdthn.tfsboards.ui.board.fragment.adapter.CustomDragItem
import com.alxdthn.tfsboards.ui.board.view_model.BoardViewModel
import com.alxdthn.tfsboards.ui.dialogs.RemoveItemDialogFragment
import com.alxdthn.tfsboards.utilities.AppCommon
import com.alxdthn.tfsboards.utilities.AppConstants
import com.alxdthn.tfsboards.utilities.DialogListener
import com.woxthebox.draglistview.BoardView
import kotlinx.android.synthetic.main.fragment_board.*

class BoardFragment : FragmentInputHandler(R.layout.fragment_board), DialogListener {

	lateinit var viewModel: BoardViewModel

	private lateinit var layoutHandler: BoardLayoutHandler
	lateinit var itemsHandler: BoardItemsHandler
	lateinit var clickHandler: BoardClickHandler

	var selectedColumnHeader: View? = null
	var selectedColumnFooter: View? = null
	var selectedAdapter: ColumnAdapter? = null
	var selectedRecycler: RecyclerView? = null

	override fun initViewModel() {
		viewModelInputHandler = ViewModelProviders.of(this, viewModelFactory)
			.get(BoardViewModel::class.java)
		viewModel = viewModelInputHandler as BoardViewModel
		viewModel.init()
	}

	override fun initializeUi() {
		itemsHandler = BoardItemsHandler(viewModel)
		clickHandler = BoardClickHandler(this)
		layoutHandler = BoardLayoutHandler(this)
		svBackgroundHolder.isEnabled = false
		createBoardView()
	}

	override fun initializeObservers() {
		BoardEventObserver().bind(this)
		BoardDataObserver().bind(this)
		BoardStateObserver().bind(this)
	}

	override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
		if (nextAnim != 0) {
			val anim = AnimationUtils.loadAnimation(activity, nextAnim)

			anim.setAnimationListener(object : Animation.AnimationListener {
				override fun onAnimationEnd(animation: Animation?) {
					if (bvBoardView != null) {
						viewModel.initBoard()
					}
				}

				override fun onAnimationStart(animation: Animation?) = Unit
				override fun onAnimationRepeat(animation: Animation?) = Unit
			})
			return anim
		}
		return super.onCreateAnimation(transit, enter, nextAnim)
	}

	override fun onStart() {
		super.onStart()

		bvBoardView.scrollToColumn(viewModel.focusedColumn, true)
	}

	override fun onStop() {
		super.onStop()

		viewModel.saveFocus(bvBoardView.focusedColumn)
	}

	private fun createBoardView() {
		bvBoardView.apply {
			setCustomDragItem(CustomDragItem(mainActivity, R.layout.layout_board_card_item))
			setSnapToColumnWhenDragging(false)
			setSnapToColumnsWhenScrolling(true)
			setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER)
			setSnapDragItemToTouch(false)
			setBoardCallback(itemsHandler)
			setBoardListener(itemsHandler)
			addOnLayoutChangeListener(layoutHandler)
		}
	}

	fun updateBoardView() {
		if (bvBoardView != null) {
			bvBoardView.clearBoard()
			itemsHandler.renderColumns()
			bvBoardView.initColumnAddBtn(this)
		}
	}

	fun loadImageBackground(view: ImageView, imageUrl: String?, color: Int) {
		llBoardContainer.setBackgroundColor(color)
		AppCommon.setImageBackground(view, imageUrl, color)
	}

	override fun onDialogAnswer(answer: Any) {
		if (answer is RemoveItemDialogFragment.Answer && answer.accepted) {
			when (answer.requestCode) {
				AppConstants.DELETE_CARD_REQUEST -> viewModel.deleteCard()
				AppConstants.DELETE_COLUMN_REQUEST -> viewModel.archiveColumn()
			}
		}
	}

	override fun inject(injector: AppComponent) = injector.inject(this)
}