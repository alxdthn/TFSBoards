package com.alxdthn.tfsboards.utilities

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView

interface TextInputHandler
	: View.OnFocusChangeListener,
	TextView.OnEditorActionListener {

	override fun onFocusChange(v: View?, hasFocus: Boolean)
	override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean

	fun initTextInput(input: EditText)
}