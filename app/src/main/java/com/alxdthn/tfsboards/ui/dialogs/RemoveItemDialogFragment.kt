package com.alxdthn.tfsboards.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.main.MainActivity
import com.alxdthn.tfsboards.utilities.AppConstants.DIALOG_ITEM_ID_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.DIALOG_MESSAGE_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.FRAGMENT_TAG_KEY
import com.alxdthn.tfsboards.utilities.AppConstants.REQUEST_CODE_KEY
import com.alxdthn.tfsboards.utilities.DialogListener

class RemoveItemDialogFragment : DialogFragment() {

	private lateinit var listener: DialogListener
	private lateinit var fragmentTag: String
	private lateinit var message: String
	private val answer = Answer()

	class Answer(
		var accepted: Boolean = false,
		var idItem: String = "",
		var requestCode: Int = 0
	)

	@Suppress("UNCHECKED_CAST")
	override fun onAttach(context: Context) {
		super.onAttach(context)

		if (arguments != null) {
			message = arguments?.getString(DIALOG_MESSAGE_KEY) ?: ""
			fragmentTag = arguments?.getString(FRAGMENT_TAG_KEY) ?: ""
			answer.idItem = arguments?.getString(DIALOG_ITEM_ID_KEY) ?: ""
			answer.requestCode = arguments?.getInt(REQUEST_CODE_KEY) ?: 0
		}
		listener = (activity as MainActivity).getListener(fragmentTag)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(requireContext())

		builder.setMessage(message)
			.setPositiveButton(R.string.delete) { _, _ ->
				answer.accepted = true
				dismiss()
			}
			.setNegativeButton(R.string.dialog_dismiss_button_title) { _, _ ->
				answer.accepted = false
				dismiss()
			}
		return builder.create()
	}

	override fun onDestroy() {
		super.onDestroy()

		listener.onDialogAnswer(answer)
	}

	companion object {
		fun show(
			manager: FragmentManager,
			message: String,
			itemId: String,
			requestCode: Int,
			fragmentTag: String
		) {
			val removeItemDialogFragment = RemoveItemDialogFragment()
			val bundle = Bundle()

			bundle.putString(DIALOG_MESSAGE_KEY, message)
			bundle.putString(DIALOG_ITEM_ID_KEY, itemId)
			bundle.putString(FRAGMENT_TAG_KEY, fragmentTag)
			bundle.putInt(REQUEST_CODE_KEY, requestCode)
			removeItemDialogFragment.arguments = bundle
			removeItemDialogFragment.show(manager, null)
		}
	}
}