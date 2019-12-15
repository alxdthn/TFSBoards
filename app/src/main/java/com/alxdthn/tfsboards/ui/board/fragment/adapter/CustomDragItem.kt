package com.alxdthn.tfsboards.ui.board.fragment.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.woxthebox.draglistview.DragItem
import kotlinx.android.synthetic.main.layout_board_card_item.view.*

class CustomDragItem(context: Context, layoutId: Int) : DragItem(context, layoutId) {

	private val upElevation = 20f
	private val downElevation = 6f

	override fun onBindDragView(clickedView: View?, dragView: View?) {
		if (clickedView != null && dragView != null) {
			val dragCard = dragView.cvBoardColumnDragItem
			val clickedCard = clickedView.cvBoardColumnDragItem

			dragView.apply {
				txvCardName.text = clickedView.txvCardName.text
				ivCardItemDescription.visibility = clickedView.ivCardItemDescription.visibility

				ivCardItemAttachments.visibility = clickedView.ivCardItemAttachments.visibility
				txvCardItemAttachmentsCount.visibility =
					clickedView.txvCardItemAttachmentsCount.visibility
				txvCardItemAttachmentsCount.text = clickedView.txvCardItemAttachmentsCount.text

				ivCardItemMembers.visibility = clickedView.ivCardItemMembers.visibility
				txvCardItemMembersCount.visibility = clickedView.txvCardItemMembersCount.visibility
				txvCardItemMembersCount.text = clickedView.txvCardItemMembersCount.text
			}
			if (dragCard != null && clickedCard != null) {
				dragCard.maxCardElevation = upElevation
				dragCard.cardElevation = clickedCard.cardElevation
			}
		}
	}

	override fun onMeasureDragView(clickedView: View?, dragView: View?) {
		if (clickedView != null && dragView != null) {
			val dragCard = dragView.cvBoardColumnDragItem
			val clickedCard = clickedView.cvBoardColumnDragItem
			val widthDiff = dragCard.paddingLeft - clickedCard.paddingLeft +
					dragCard.paddingRight - clickedCard.paddingRight
			val heightDiff = dragCard.paddingTop - clickedCard.paddingTop +
					dragCard.paddingBottom - clickedCard.paddingBottom
			val width = clickedView.measuredWidth + widthDiff
			val height = clickedView.measuredHeight + heightDiff

			dragView.layoutParams = FrameLayout.LayoutParams(width, height)

			val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
			val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

			dragView.measure(widthSpec, heightSpec)
		}
	}

	override fun onStartDragAnimation(dragView: View?) {
		if (dragView != null) {
			animatedChangeElevation(dragView, upElevation)
		}
	}

	override fun onEndDragAnimation(dragView: View?) {
		if (dragView != null) {
			animatedChangeElevation(dragView, downElevation)
		}
	}

	private fun animatedChangeElevation(dragView: View, value: Float) {
		val dragCard = dragView.cvBoardColumnDragItem

		if (dragCard != null) {
			val anim = ObjectAnimator.ofFloat(
				dragCard,
				"CardElevation", dragCard.cardElevation, value
			)
			anim.interpolator = DecelerateInterpolator()
			anim.duration = ANIMATION_DURATION.toLong()
			anim.start()
		}
	}
}