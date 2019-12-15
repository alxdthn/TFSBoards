package com.alxdthn.tfsboards.ui.teams.fragment.adapter.sticky_header

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_teams_list_header.view.*

class StickyHeader(
	private val listener: StickyHeaderInterface
) : RecyclerView.ItemDecoration() {

	private var stickyHeaderHeight = 0
	private var headerName: String? = null

	override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		super.onDrawOver(c, parent, state)

		val topChild = parent.getChildAt(0)
		val topChildPosition = parent.getChildAdapterPosition(topChild)

		if (topChildPosition == RecyclerView.NO_POSITION) {
			return
		}

		val currentHeader = getHeaderViewForItem(topChildPosition, parent)
		fixLayoutSize(parent, currentHeader)

		val contactPoint = currentHeader.bottom
		val childInContact = getChildInContact(parent, contactPoint)

		if (childInContact != null && listener.isHeader(
				parent.getChildAdapterPosition(
					childInContact
				)
			)
		) {
			moveHeader(c, currentHeader, childInContact)
		} else {
			drawHeader(c, currentHeader)
		}
	}

	private fun getHeaderViewForItem(topChildPosition: Int, parent: RecyclerView): View {
		val headerPosition = listener.getHeaderPositionForItem(topChildPosition)
		val layoutResId = listener.getHeaderLayout(headerPosition)
		val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)

		listener.bindHeaderData(header, headerPosition)
		headerName = header.txvGroupHeaderName.text.toString()
		return header
	}

	private fun fixLayoutSize(parent: ViewGroup, child: View) {
		val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
		val heightSpec =
			View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

		val childWidthSpec = ViewGroup.getChildMeasureSpec(
			widthSpec,
			parent.paddingLeft + parent.paddingRight,
			child.layoutParams.width
		)
		val childHeightSpec = ViewGroup.getChildMeasureSpec(
			heightSpec,
			parent.paddingTop + parent.paddingBottom,
			child.layoutParams.height
		)


		child.measure(childWidthSpec, childHeightSpec)
		stickyHeaderHeight = child.measuredHeight
		child.layout(0, 0, child.measuredWidth, stickyHeaderHeight)
	}

	private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
		for (i in 0 until parent.childCount) {
			val child = parent.getChildAt(i)
			if (child.bottom > contactPoint && child.top <= contactPoint) {
				return child
			}
		}
		return null
	}

	private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
		c.save()
		c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
		currentHeader.draw(c)
		c.restore()
	}

	private fun drawHeader(c: Canvas, header: View) {
		c.save()
		c.translate(0f, 0f)
		header.draw(c)
		c.restore()
	}
}