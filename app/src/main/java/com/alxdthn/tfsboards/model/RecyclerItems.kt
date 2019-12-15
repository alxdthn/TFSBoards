package com.alxdthn.tfsboards.model

import android.content.res.Configuration
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import com.alxdthn.tfsboards.model.local.*
import com.alxdthn.tfsboards.utilities.AppActionFormatter
import com.alxdthn.tfsboards.utilities.AppCommon
import kotlinx.coroutines.yield

sealed class Item(
	var id: String,
	var name: String
)

class HeaderItem(
	id: String = "?",
	name: String = "?",
	var countOfBoards: Int = 0
) : Item(id, name) {
	infix fun render(team: Team): HeaderItem {
		return this.apply {
			id = team.id ?: "ff"
			name = team.name
			countOfBoards = team.boards.size
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is HeaderItem
				&& this.id == other.id
				&& this.name == other.name
				&& this.countOfBoards == other.countOfBoards
	}
}

class BoardItem(
	id: String = "?",
	name: String = "?",
	var color: Int = 0,
	var idTeam: String? = null,
	var url: String? = null
) : Item(id, name) {
	fun render(board: Board, orientation: Int): BoardItem {
		val url = when (orientation) {
			Configuration.ORIENTATION_PORTRAIT -> board.backgroundImageScaled?.get(1)?.url
			else -> board.backgroundImageScaled?.get(2)?.url
		}

		return this.apply {
			id = board.id
			name = board.name
			color = board.color!!.value
			idTeam = board.idTeam
			this.url = url
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is BoardItem
				&& this.id == other.id
				&& this.name == other.name
				&& this.color == other.color
				&& this.color == other.color
				&& this.idTeam == other.idTeam
				&& this.url == other.url
	}
}

class ColumnItem(
	id: String = "?",
	name: String = "?",
	val items: MutableList<Item> = mutableListOf()
) : Item(id, name) {
	infix fun render(column: Column): ColumnItem {
		return this.apply {
			id = column.id
			name = column.name
		}
	}
}

class CardItem(
	id: String = "?",
	name: String = "?",
	var spannedName: SpannableString? = null,
	var hasDescription: Boolean = false,
	var attachmentCount: Int = 0,
	var memberCount: Int = 0
) : Item(id, name) {
	infix fun render(card: Card): CardItem {
		return this.apply {
			id = card.id
			name = card.name
			hasDescription = card.desc.isNotEmpty()
			attachmentCount = card.attachments.size
			memberCount = card.idMembers.size
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is CardItem
				&& id == other.id
				&& name == other.name
				&& spannedName == other.spannedName
				&& hasDescription == other.hasDescription
				&& attachmentCount == other.attachmentCount
				&& memberCount == other.memberCount
	}
}

class BtnCardItem(
	id: String = "",
	name: String = ""
) : Item(id, name)

class UserAvatarItem(
	id: String = "",
	name: String = "",
	var avatarHash: String? = null,
	var avatarColor: Int = -1
) : Item(id, name) {
	infix fun render(user: User): UserAvatarItem {
		return this.apply {
			id = user.id
			name = user.username
			avatarHash = user.avatarHash
			avatarColor = user.color
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is UserAvatarItem
				&& this.id == other.id
				&& this.name == other.name
				&& this.avatarHash == other.avatarHash
				&& this.avatarColor == other.avatarColor
	}
}

class UserItem(
	id: String = "",
	name: String = "",
	var color: Int = 0,
	var fullName: String = "?",
	var avatarHash: String? = null,
	var checked: Boolean = false
) : Item(id, name) {
	infix fun render(user: User): UserItem {
		return this.apply {
			id = user.id
			color = user.color
			name = user.username
			fullName = user.fullName
			avatarHash = user.avatarHash
			checked = user.checked
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is UserItem
				&& this.id == other.id
				&& this.name == other.name
				&& this.color == other.color
				&& this.checked == other.checked
				&& this.fullName == other.fullName
				&& this.avatarHash == other.avatarHash
	}
}

class AttachmentImageItem(
	id: String = "",
	name: String = "attachment_image",
	var url: String = ""
) : Item(id, name) {
	infix fun render(attachment: Attachment): AttachmentImageItem {
		return this.apply {
			id = attachment.id
			name = attachment.name
			url = attachment.previews[3].url
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is AttachmentImageItem
				&& this.id == other.id
				&& this.url == other.url
	}
}

class AttachmentFileItem(
	id: String = "",
	name: String = "",
	var info: String = ""
) : Item(id, name) {
	infix fun render(attachment: Attachment): AttachmentFileItem {
		return this.apply {
			id = attachment.id
			name = attachment.name
			info = AppCommon.getReadableFileSize(attachment.bytes) +
					AppCommon.getReadableTimeInterval(attachment.date)
		}
	}
	override fun equals(other: Any?): Boolean {
		return other is AttachmentFileItem
				&& this.id == other.id
				&& this.name == other.name
				&& this.info == other.info
	}
}

class ActionItem(
	id: String = "?",
	name: String = "action_item",
	var description: Spanned? = null,
	var avatarColor: Int = -1,
	var avatarHash: String? = null,
	var previewUrl: String? = null,
	var actionTime: String = "?"
) : Item(id, name) {
	infix fun render(action: Action): ActionItem {
		return this.apply {
			id = action.id
			description = AppActionFormatter.format(action)
			avatarHash = action.memberCreator.avatarHash
			previewUrl = action.previewUrl
			actionTime = AppCommon.getReadableTimeInterval(action.date)
		}
	}

	infix fun with(color: Int): ActionItem {
		return this.apply {
			avatarColor = color
		}
	}

	override fun equals(other: Any?): Boolean {
		return other is ActionItem
				&& this.id == other.id
				&& this.description.toString() == other.description.toString()
				&& this.avatarColor == other.avatarColor
				&& this.avatarHash == other.avatarHash
				&& this.previewUrl == other.previewUrl
				&& this.actionTime == other.actionTime
	}
}