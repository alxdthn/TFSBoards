package com.alxdthn.tfsboards.utilities.extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun Activity.softInputAdjustNothing() {
	window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}

fun Activity.softInputAdjustResize() {
	window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

fun AppCompatActivity.printBackStack() {
	Log.d("bestTAG", "FRAGMENTS:\n" + supportFragmentManager
		.fragments
		.joinToString("\n") { fragment -> fragment.tag.toString() })
}
