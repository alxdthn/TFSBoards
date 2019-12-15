package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.base.Movable
import com.alxdthn.tfsboards.model.responses.BoardResponse
import com.alxdthn.tfsboards.utilities.AppColors

data class Board(
	var id: String = "?",
	var name: String = "?",
	var idTeam: String? = null,
	var color: AppColor? = null,
	var backgroundImage: String? = null,
	var backgroundImageScaled: Array<BackgroundImage>? = null
) : Movable() {
	fun fromResponse(boardResponse: BoardResponse): Board {
		val parser = android.graphics.Color::parseColor

		val newColor = AppColors.getColor(boardResponse.prefs.background)
			?: AppColor(
				value = parser(boardResponse.prefs.backgroundTopColor),
				mediumValue = parser(boardResponse.prefs.backgroundBottomColor),
				darkValue = AppColors.getDarkColor(parser(boardResponse.prefs.backgroundBottomColor), 0.7f)
			)

		return this.apply {
			id = boardResponse.id
			name = boardResponse.name
			idTeam = boardResponse.idTeam
			color = newColor
			backgroundImage = boardResponse.prefs.backgroundImage
			backgroundImageScaled = boardResponse.prefs.backgroundImageScaled?.copyOf()
		}
	}
}