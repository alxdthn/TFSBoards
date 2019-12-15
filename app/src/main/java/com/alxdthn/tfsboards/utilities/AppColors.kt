package com.alxdthn.tfsboards.utilities

import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorInt
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.model.local.AppColor
import java.lang.IllegalArgumentException

object AppColors {
	private val colors = mutableMapOf<String, AppColor>()

	fun getColor(name: String): AppColor? {
		return colors[name]
	}

	fun getColorName(value: Int): String {
		return colors.getKey(value) ?: throw IllegalArgumentException("value: $value")
	}

	fun getRandColor(): AppColor {
		return colors.entries.shuffled().first().value
	}

	@ColorInt
	fun getDarkColor(@ColorInt color: Int, scale: Float): Int {
		return Color.HSVToColor(FloatArray(3).apply {
			Color.colorToHSV(color, this)
			this[2] *= scale
		})
	}

	fun initColors(resources: Resources) {
		val names = resources.getStringArray(R.array.color_keys)
		val normalValues = resources.getIntArray(R.array.colors_normal)
		val mediumValues = resources.getIntArray(R.array.colors_medium)
		val darkValues = resources.getIntArray(R.array.colors_dark)

		for ((i, name) in names.withIndex()) {
			colors[name] = AppColor(
				value = normalValues[i],
				mediumValue = mediumValues[i],
				darkValue = darkValues[i]
			)
		}
	}

	private fun MutableMap<String, AppColor>.getKey(value: Int): String? {
		this.forEach {
			if (it.value.value == value) return it.key
		}
		return null
	}
}