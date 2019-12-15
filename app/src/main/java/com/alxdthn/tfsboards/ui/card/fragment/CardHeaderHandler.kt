package com.alxdthn.tfsboards.ui.card.fragment

import android.content.res.Configuration
import android.util.TypedValue
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.utilities.AppAnimator
import com.alxdthn.tfsboards.utilities.extensions.getOrientation
import com.alxdthn.tfsboards.utilities.extensions.hide
import com.alxdthn.tfsboards.utilities.extensions.show
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.layout_card_content_header.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CardHeaderHandler(
	private val main: CardFragment
) : AppBarLayout.OnOffsetChangedListener {

	enum class Status{
		OPENED,
		CLOSED
	}

	private var scrollRange = -1
	private var status = Status.CLOSED
	var onEdit = false

	init {
		main.abCardFragmentAppBar.addOnOffsetChangedListener(this)
		initHeaderTextSize()
	}

	private fun initHeaderTextSize() {
		main.apply {
			val size = if (getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
				resources.getDimension(R.dimen.card_header_title_size_port)
			} else {
				resources.getDimension(R.dimen.card_header_title_size_land)
			}
			txvCardFragmentName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
		}
	}

	override fun onOffsetChanged(p0: AppBarLayout?, offset: Int) {
		if (onEdit) return
		if (scrollRange == -1) {
			scrollRange = main.abCardFragmentAppBar.totalScrollRange
		}

		val scale = 1 + offset / scrollRange.toFloat()

		main.apply {
			if (scale > 0.8 && status == Status.CLOSED) {
				AppAnimator show txvCardFragmentName
				AppAnimator show txvCardFragmentPosition
				AppAnimator hide txvToolbarTitle
				status = Status.OPENED
			} else if (scale < 0.8 && status == Status.OPENED) {
				AppAnimator hide txvCardFragmentName
				AppAnimator hide txvCardFragmentPosition
				AppAnimator show txvToolbarTitle
				status = Status.CLOSED
			}
		}
	}
}