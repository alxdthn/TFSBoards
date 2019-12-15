package com.alxdthn.tfsboards.base

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.subjects.PublishSubject

abstract class ViewModelInputHandler : BaseViewModel(), TextWatcher {

	var bufferedInput: CharSequence? = null

	var isCanAccept = LocalState()
	val onEdit = LocalState()

	open fun cancelInput() {
		bufferedInput = null
		onEdit.cancel()
	}

	abstract fun acceptInput()

	abstract fun getStartTextForInput(): String

	fun getBufferedInput(): String {
		return bufferedInput?.toString() ?: ""
	}

	override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
		isCanAccept.set(s.isNotEmpty())
		bufferedInput = s
	}

	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

	override fun afterTextChanged(s: Editable) = Unit
}