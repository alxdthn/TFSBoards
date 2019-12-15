package com.alxdthn.tfsboards.custom_view

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max


class CustomRecyclerView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

	private var manager: GridLayoutManager? = null
	private var columnWidth = -1

	init {
		init(context, attrs)
	}

	private fun init(context: Context, attrs: AttributeSet?) {
		if (attrs != null) {
			val attrsArray = intArrayOf(R.attr.columnWidth)
			val array: TypedArray = context.obtainStyledAttributes(attrs, attrsArray)

			columnWidth = array.getDimensionPixelSize(0, -1)
			array.recycle()
		}
		manager = GridLayoutManager(getContext(), 1)
		layoutManager = manager
	}

	override fun onMeasure(widthSpec: Int, heightSpec: Int) {
		super.onMeasure(widthSpec, heightSpec)

		if (columnWidth > 0) {
			val spanCount = max(1, measuredWidth / columnWidth)

			manager!!.spanCount = spanCount
		}
	}
}