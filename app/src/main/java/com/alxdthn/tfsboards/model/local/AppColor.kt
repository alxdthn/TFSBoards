package com.alxdthn.tfsboards.model.local

import java.io.Serializable

data class AppColor(
	val value: Int,
	val mediumValue: Int,
	val darkValue: Int
): Serializable