package com.alxdthn.tfsboards.base

import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.alxdthn.tfsboards.utilities.TextInputHandler
import com.alxdthn.tfsboards.utilities.extensions.hideKeyboard
import com.alxdthn.tfsboards.utilities.extensions.showKeyboard

abstract class FragmentInputHandler(@LayoutRes layoutRes: Int)
	: BaseFragment(layoutRes),
	TextInputHandler {

	lateinit var viewModelInputHandler: ViewModelInputHandler

	var selectedEditText: EditText? = null
		set(value) {
			if (value != null) {
				initTextInput(value)
			}
			field = value
		}

	override fun initTextInput(input: EditText) {
		val text = viewModelInputHandler.getStartTextForInput()

		input.setText(text)
		input.setSelection(text.length)
		input.setOnEditorActionListener(this)
		input.addTextChangedListener(viewModelInputHandler as TextWatcher)
		input.onFocusChangeListener = this
		input.requestFocus()
	}

	override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
		val isDone = actionId == EditorInfo.IME_ACTION_DONE
		if (isDone) viewModelInputHandler.acceptInput()
		return isDone
	}

	override fun onFocusChange(v: View?, hasFocus: Boolean) {
		if (hasFocus) {
			v?.showKeyboard()
		} else {
			if (!mainActivity.isDestroyed) {
				hideKeyboard()
				viewModelInputHandler.cancelInput()
			}
		}
	}
}