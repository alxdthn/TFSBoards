package com.alxdthn.tfsboards.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.local.Team
import com.alxdthn.tfsboards.main.MainActivity
import com.alxdthn.tfsboards.utilities.AppConstants.TEAMS_FRAGMENT
import com.alxdthn.tfsboards.utilities.AppConstants.TEAMS_KEY
import com.alxdthn.tfsboards.utilities.DialogListener
import com.alxdthn.tfsboards.utilities.extensions.hideKeyboard
import com.alxdthn.tfsboards.utilities.extensions.showKeyboard
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_board_dialog.view.*
import java.io.Serializable

/**
 * 		Я уже не знаю как заставить эту несчастную клавиаутуру
 * 		скрываться при окончании диалога. Куда попало понатыкал метод, который
 * 		должен скрывать ее. Вроде работает.
 *
 * 		Спустя день клавиатура опять перестала скрываться.
 *
 *
 * 		Думаю нужна отдельная лекция про софт инпут)
 */

class AddBoardDialogFragment : DialogFragment(),
	View.OnClickListener,
	TextView.OnEditorActionListener {

	private lateinit var inputMethodManager: InputMethodManager
	private lateinit var listener: DialogListener
	private lateinit var dialogView: View
	private var teams: MutableList<Team>? = null
	private val answer = Answer()

	class Answer(
		var accepted: Boolean = false,
		var boardName: String = "",
		var idTeam: String? = null
	)

	@Suppress("UNCHECKED_CAST")
	override fun onAttach(context: Context) {
		super.onAttach(context)

		listener = (activity as MainActivity).getListener(TEAMS_FRAGMENT)
		inputMethodManager = (activity as MainActivity)
			.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		if (arguments != null) {
			teams = arguments?.getSerializable(TEAMS_KEY) as MutableList<Team>
		}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(requireContext())
		val dialog = builder.create()

		dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_add_board_dialog, null)
		handleSpinner(dialogView)
		dialogView.btnAddBoardAccept.setOnClickListener(this)
		dialogView.btnAddBoardDismiss.setOnClickListener(this)
		dialogView.edxAddBoardDialogInput.setOnEditorActionListener(this)
	//	dialogView.edxAddBoardDialogInput.onFocusChangeListener = this
		dialog.setView(dialogView)
		return dialog
	}

	override fun onStart() {
		super.onStart()

		dialogView.edxAddBoardDialogInput.requestFocus()
		dialogView.edxAddBoardDialogInput.showKeyboard()
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)

		hideKeyboard()
		listener.onDialogAnswer(answer)
	}

	private fun handleSpinner(dialogView: View) {
		val items = teams

		if (items != null && items.find { it.id == null } == null) {
			items.add(Team())
		}
		if (items != null && items.size > 1) {
			val spinnerItems = items.map { it.name }
			val adapter =
				ArrayAdapter(context!!, android.R.layout.simple_spinner_item, spinnerItems)

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			dialogView.spnAddBoardDialogSpinner.adapter = adapter
		} else {
			dialogView.spnAddBoardDialogSpinner.visibility = View.GONE
		}
	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.btnAddBoardAccept -> addNewBoard()
			R.id.btnAddBoardDismiss -> dismiss()
		}
	}

	override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
		return if (actionId == EditorInfo.IME_ACTION_DONE) {
			addNewBoard()
			true
		} else {
			false
		}
	}

	private fun addNewBoard() {
		val name = dialogView.edxAddBoardDialogInput.text.toString()
		val teams = teams!!
		val idTeam = when {
			teams.size > 1 -> teams[dialogView.spnAddBoardDialogSpinner.selectedItemPosition].id
			teams.isNotEmpty() -> teams.first().id
			else -> null
		}
		when {
			name.isNotEmpty() -> {
				answer.boardName = name
				answer.idTeam = idTeam
				answer.accepted = true

				dismiss()
			}
			else -> showSnackbar(dialogView, getString(R.string.enter_name))
		}
	}

	private fun showSnackbar(view: View, text: String) {
		Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
	}

	companion object {
		fun show(manager: FragmentManager, teams: List<Team>?) {
			val addBoardDialogFragment = AddBoardDialogFragment()
			val bundle = Bundle()

			bundle.putSerializable(TEAMS_KEY, teams as Serializable)
			addBoardDialogFragment.arguments = bundle
			addBoardDialogFragment.show(manager, null)
		}
	}
}