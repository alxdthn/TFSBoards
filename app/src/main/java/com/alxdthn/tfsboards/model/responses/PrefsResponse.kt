package com.alxdthn.tfsboards.model.responses

import com.alxdthn.tfsboards.model.local.BackgroundImage

data class PrefsResponse(
	val background: String,
	val backgroundImage: String,
	val backgroundImageScaled: Array<BackgroundImage>?,
	val backgroundBottomColor: String,
	val backgroundTopColor: String
)