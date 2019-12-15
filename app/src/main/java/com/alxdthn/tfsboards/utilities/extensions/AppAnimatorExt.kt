package com.alxdthn.tfsboards.utilities.extensions

import android.view.View
import com.alxdthn.tfsboards.utilities.AppAnimator

infix fun AppAnimator.show(view: View) {
	animateAlpha(view, SHOW)
}

infix fun AppAnimator.hide(view: View) {
	animateAlpha(view, HIDE)
}
