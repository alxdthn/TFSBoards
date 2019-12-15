package com.alxdthn.tfsboards.utilities

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.annotation.StringRes
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.local.Action
import com.alxdthn.tfsboards.model.responses.ActionEntitiesResponse

object AppActionFormatter {

	private val formatMap = mapOf(
		Pair("create_card", R.string.action_create_card),
		Pair("renamed_card", R.string.action_renamed_card),
		Pair("archived_card", R.string.action_archived_card),
		Pair("comment_on_card", R.string.action_comment_on_card),
		Pair("member_left_card", R.string.action_member_left_card),
		Pair("member_joined_card", R.string.action_member_joined_card),
		Pair("sent_card_to_board", R.string.action_sent_card_to_board),
		Pair("added_member_to_card", R.string.action_added_member_to_card),
		Pair("add_attachment_to_card", R.string.action_add_attachment_to_card),
		Pair("removed_member_from_card", R.string.action_removed_member_from_card),
		Pair("delete_attachment_from_card", R.string.action_delete_attachment_from_card),
		Pair("changed_description_of_card", R.string.action_changed_description_of_card),
		Pair("move_card_from_list_to_list", R.string.action_move_card_from_list_to_list)
	)

	val	ignoredActions = arrayOf(
		"unknown",
		"action_moved_card_lower",
		"action_moved_card_higher"
	)


	private lateinit var appContext: Context

	fun init(appContext: Context) {
		this.appContext = appContext
	}

	fun format(action: Action): Spanned {
		val stringRes = formatMap[action.type]
		val user = action.memberCreator.fullName
		val args = action.args ?: arrayOf()

		return if (stringRes != null) {
			from(stringRes, user, *args)
		} else {
			SpannableStringBuilder.valueOf(action.type)
		}
	}

	fun from(@StringRes stringRes: Int, vararg formatArgs: Any?): Spanned {
		return Html.fromHtml(appContext.getString(stringRes, *formatArgs))
	}

	fun initArgs(action: Action, data: ActionEntitiesResponse?): Array<String?>? {
		action.type = action.type.replace("action_", "")
		return when (action.type) {
			"move_card_from_list_to_list" -> arrayOf(data?.listBefore?.text, data?.listAfter?.text)
			"delete_attachment_from_card" -> arrayOf(data?.attachment?.text)
			"renamed_card" -> arrayOf(data?.name?.text, data?.card?.text)
			"add_attachment_to_card" -> arrayOf(data?.attachment?.text)
			"removed_member_from_card" -> arrayOf(data?.member?.text)
			"added_member_to_card" -> arrayOf(data?.member?.text)
			"create_card" -> arrayOf(data?.list?.text)
			"comment_on_card" -> arrayOf(data?.comment?.text)
			"changed_description_of_card" -> arrayOf(data?.card?.desc)
			else -> null
		}
	}
}