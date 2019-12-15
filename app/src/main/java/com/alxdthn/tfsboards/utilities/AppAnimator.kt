package com.alxdthn.tfsboards.utilities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alxdthn.tfsboards.utilities.extensions.dp
import com.alxdthn.tfsboards.utilities.extensions.dpf
import com.alxdthn.tfsboards.utilities.extensions.invisible
import com.alxdthn.tfsboards.utilities.extensions.visible

object AppAnimator {

	const val HIDE = 1
	const val SHOW = 2

	fun animateTranslation(
		view: View,
		fromXDelta: Float,
		toXDelta: Float,
		fromYDelta: Float,
		toYDelta: Float
	) {
		val transitionAnimation = TranslateAnimation(
			view dpf fromXDelta, view dpf toXDelta, view dpf fromYDelta, view dpf toYDelta
		)
		transitionAnimation.duration = 250
		view.translationY += toYDelta
		view.translationX += toXDelta
		view.startAnimation(transitionAnimation)
	}

	fun animateStatusBarColor(activity: AppCompatActivity, colorTo: Int) {
		ValueAnimator.ofObject(
			ArgbEvaluator(), activity.window.statusBarColor, colorTo
		).apply {
			duration = 250
			addUpdateListener { activity.window.statusBarColor = it.animatedValue as Int }
			start()
		}
	}

	fun animateAlpha(view: View?, type: Int) {
		if (view != null) {
			val startValue: Float
			val endValue: Float

			when (type) {
				HIDE -> {
					startValue = 1f
					endValue = 0f
				}
				SHOW -> {
					startValue = 0f
					endValue = 1f
				}
				else -> {
					throw IllegalArgumentException("unknown type")
				}
			}

			val alpha = AlphaAnimation(startValue, endValue)

			alpha.duration = 300
			if (type == SHOW) view.visible() else view.invisible()
			view.startAnimation(alpha)
		}
	}
}