package com.alxdthn.tfsboards.custom_view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.ColumnItem
import com.alxdthn.tfsboards.ui.board.fragment.BoardClickHandler
import com.alxdthn.tfsboards.ui.board.fragment.BoardFragment
import com.alxdthn.tfsboards.ui.board.fragment.adapter.ColumnAdapter
import com.alxdthn.tfsboards.utilities.extensions.dp
import com.woxthebox.draglistview.BoardView
import kotlinx.android.synthetic.main.layout_board_add_column_item.view.*
import kotlinx.android.synthetic.main.layout_board_column_header.view.*

class CustomBoardView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : BoardView(context, attrs, defStyleAttr) {

	private val headerLayout = R.layout.layout_board_column_header
	private val footerLayout = R.layout.layout_board_column_footer
	private val addColumnButtonLayout = R.layout.layout_board_add_column_item

	private val columnBackgroundDrawable = R.drawable.column_backgroud

	fun initColumnView(column: ColumnItem, boardFragment: BoardFragment) {
		val adapter = ColumnAdapter(column.items, boardFragment)
		val header = View.inflate(context, headerLayout, null)

		header.txvColumnHeaderName.text = column.name
		header.setOnClickListener(boardFragment.clickHandler)
		header.setOnLongClickListener(boardFragment.clickHandler)
		addColumn(adapter, header, null, false, LinearLayoutManager(context))
		rebuildColumnView(header, true, boardFragment.clickHandler)
	}

	private fun rebuildColumnView(
		header: View,
		isColumn: Boolean,
		clickHandler: BoardClickHandler
	) {
		val recyclerView = getRecyclerView(getColumnOfHeader(header))
		val columnLayout = recyclerView.parent as ViewGroup

		if (isColumn) {
			rebuildRecycler(recyclerView)
		}
		rebuildColumnLayout(columnLayout, isColumn, clickHandler)
	}


	private fun rebuildRecycler(recyclerView: RecyclerView) {
		recyclerView.layoutParams = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
		)
		recyclerView.minimumHeight = dp(52f)
	}

	private fun rebuildColumnLayout(
		columnLayout: ViewGroup,
		isColumn: Boolean,
		clickHandler: BoardClickHandler
	) {
		val layoutParams = LinearLayout.LayoutParams(
			(columnLayout.layoutParams.width - columnLayout.layoutParams.width * 0.1).toInt(),
			LinearLayout.LayoutParams.WRAP_CONTENT
		)

		layoutParams.setMargins(dp(8f), 0, dp(8f), 0)
		columnLayout.layoutParams = layoutParams
		if (isColumn) {
			val footer = View.inflate(context, footerLayout, null)

			footer.setOnClickListener(clickHandler)
			columnLayout.clipToOutline = true
			columnLayout.background = ContextCompat.getDrawable(context, columnBackgroundDrawable)
			columnLayout.addView(footer)
		}
	}

	fun initColumnAddBtn(fragment: BoardFragment) {
		val adapter = ColumnAdapter(emptyList(), fragment)
		val header = View.inflate(context, addColumnButtonLayout, null)

		header.btnAddColumn.setOnClickListener(fragment.clickHandler)
		addColumn(adapter, header, null, false, LinearLayoutManager(context))
		rebuildColumnView(header, false, fragment.clickHandler)
	}
}