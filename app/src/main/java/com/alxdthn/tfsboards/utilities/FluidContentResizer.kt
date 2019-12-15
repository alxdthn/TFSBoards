package com.alxdthn.tfsboards.utilities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

	/**
	 * 	Честно стырено с просторов сети :)
	 *
	 * 	This is free and unencumbered software released into the public domain.
	 *
	 * 	Anyone is free to copy, modify, publish, use, compile, sell, or
	 * 	distribute this software, either in source code form or as a compiled
	 * 	binary, for any purpose, commercial or non-commercial, and by any
	 * 	means.
	 *
	 * 	Full license can be read here: https://github.com/saket/FluidKeyboardResize/blob/master/LICENSE
	 */

data class KeyboardVisibilityChanged(
	val visible: Boolean,
	val contentHeight: Int,
	val contentHeightBeforeResize: Int
)

object KeyboardVisibilityDetector {

	fun listen(viewHolder: ActivityViewHolder, listener: (KeyboardVisibilityChanged) -> Unit) {
		val detector = Detector(viewHolder, listener)
		viewHolder.nonResizableLayout.viewTreeObserver.addOnPreDrawListener(detector)
		viewHolder.onDetach {
			viewHolder.nonResizableLayout.viewTreeObserver.removeOnPreDrawListener(detector)
		}
	}

	private class Detector(
		val viewHolder: ActivityViewHolder,
		val listener: (KeyboardVisibilityChanged) -> Unit
	) : ViewTreeObserver.OnPreDrawListener {

		private var previousHeight: Int = -1

		override fun onPreDraw(): Boolean {
			val detected = detect()

			// The layout flickers for a moment, usually on the first
			// animation. Intercepting this pre-draw seems to solve the problem.
			return detected.not()
		}

		private fun detect(): Boolean {
			val contentHeight = viewHolder.resizableLayout.height
			if (contentHeight == previousHeight) {
				return false
			}

			if (previousHeight != -1) {
				val statusBarHeight = viewHolder.resizableLayout.top
				val isKeyboardVisible = contentHeight < viewHolder.nonResizableLayout.height - statusBarHeight

				listener(KeyboardVisibilityChanged(
					visible = isKeyboardVisible,
					contentHeight = contentHeight,
					contentHeightBeforeResize = previousHeight))
			}

			previousHeight = contentHeight
			return true
		}
	}
}

data class ActivityViewHolder(
	val nonResizableLayout: ViewGroup,
	val resizableLayout: ViewGroup,
	val contentView: ViewGroup
) {

	companion object {

		/**
		 * The Activity View tree usually looks like this:
		 *
		 * DecorView <- does not get resized, contains space for system Ui bars.
		 * - LinearLayout
		 * -- FrameLayout <- gets resized
		 * --- LinearLayout
		 * ---- Activity content
		 */
		fun createFrom(activity: Activity): ActivityViewHolder {
			val decorView = activity.window.decorView as ViewGroup
			val contentView = decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
			val actionBarRootLayout = contentView.parent as ViewGroup
			val resizableLayout = actionBarRootLayout.parent as ViewGroup

			return ActivityViewHolder(
				nonResizableLayout = decorView,
				resizableLayout = resizableLayout,
				contentView = contentView)
		}
	}

	fun onDetach(onDetach: () -> Unit) {
		nonResizableLayout.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
			override fun onViewDetachedFromWindow(v: View?) {
				onDetach()
			}

			override fun onViewAttachedToWindow(v: View?) = Unit
		})
	}
}

object FluidContentResizer {

	private var heightAnimator: ValueAnimator = ObjectAnimator()

	fun listen(activity: Activity) {
		val viewHolder = ActivityViewHolder.createFrom(activity)

		KeyboardVisibilityDetector.listen(viewHolder) {
			animateHeight(viewHolder, it)
		}
		viewHolder.onDetach {
			heightAnimator.cancel()
			heightAnimator.removeAllUpdateListeners()
		}
	}

	private fun animateHeight(viewHolder: ActivityViewHolder, event: KeyboardVisibilityChanged) {
		val contentView = viewHolder.contentView
		contentView.setHeight(event.contentHeightBeforeResize)

		heightAnimator.cancel()

		// Warning: animating height might not be very performant. Try turning on
		// "Profile GPI rendering" in developer options and check if the bars stay
		// under 16ms in your app. Using Transitions API would be more efficient, but
		// for some reason it skips the first animation and I cannot figure out why.
		heightAnimator = ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight).apply {
			interpolator = FastOutSlowInInterpolator()
			duration = 300
		}
		heightAnimator.addUpdateListener { contentView.setHeight(it.animatedValue as Int) }
		heightAnimator.start()
	}

	private fun View.setHeight(height: Int) {
		val params = layoutParams
		params.height = height
		layoutParams = params
	}
}