package com.alxdthn.tfsboards.model.local

import java.lang.IllegalStateException

data class BoardData(
	var id: String = "?",
	var name: String = "?",
	var idTeam: String = "?",
	var color: AppColor? = null,
	var backgroundImage: String? = null,
	var columns: MutableList<Column> = mutableListOf(),
	var members: MutableList<User> = mutableListOf()
) {
	fun getColors(): AppColor {
		return color ?: throw IllegalStateException("Init colors please")
	}
}