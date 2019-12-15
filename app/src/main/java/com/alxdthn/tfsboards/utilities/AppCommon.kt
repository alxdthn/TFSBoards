package com.alxdthn.tfsboards.utilities

import android.util.Log
import android.widget.ImageView
import com.alxdthn.tfsboards.custom_view.CustomUserAvatar
import com.alxdthn.tfsboards.utilities.AppConstants.AVATAR_SIZE
import com.alxdthn.tfsboards.utilities.AppConstants.AVATAR_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

object AppCommon {

	private val units = arrayOf("B", "kB", "MB", "GB", "TB")

	fun getReadableFileSize(bytes: Long): String {
		if (bytes <= 0) return ""

		val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()

		return DecimalFormat("#,##0.#").format(
			bytes / 1024.0.pow(digitGroups.toDouble())
		).toString() + " " + units[digitGroups] + ", "
	}

	fun getReadableDateFromMillis(milliSeconds: Long, dateFormat: String): String {
		val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
		val calendar = Calendar.getInstance()

		calendar.timeInMillis = milliSeconds
		return formatter.format(calendar.time)
	}

	private fun timeStampToMillis(timeStamp: String): Long {
		val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
		val date = format.parse(timeStamp)

		return date?.time ?: throw Throwable()
	}

	fun getReadableTimeInterval(timeStamp: String): String {
		val rawOffset = TimeZone.getDefault().rawOffset
		val currentTime = Calendar.getInstance().timeInMillis - rawOffset
		val fromTimeStamp = timeStampToMillis(timeStamp)
		val diff = currentTime - fromTimeStamp

		return when {
			diff < MINUTE -> "Только что"
			diff < HOUR -> typeConverter(diff / MINUTE, "минуту", "минуты", "минут")
			diff < DAY -> typeConverter(diff / HOUR, "час", "часа", "часов")
			diff < MONTH -> typeConverter(diff / DAY, "день", "дня", "дней")
			diff < YEAR -> typeConverter(diff / MONTH, "месяц", "месяца", "месяцов")
			else -> "Больше года назад"
		}
	}

	private fun typeConverter(value: Long, a: String, b: String, c: String): String {
		val type = when {
			value % 10 == 1L && value / 10 % 10 != 1L -> a
			value % 10 in 2..4 -> b
			else -> c
		}
		return if (value == 1L && type == "день") "вчера" else "$value $type назад"
	}

	private const val YEAR = 31536000000L
	private const val MONTH = 2592000000L
	private const val DAY = 86400000L
	private const val HOUR = 3600000L
	private const val MINUTE = 60000L

	fun setImageBackground(view: ImageView, imageUrl: String?, color: Int = -1) {
		if (color != -1) {
			if (view is CustomUserAvatar) {
				view.setColor(color)
			} else {
				view.setBackgroundColor(color)
			}
		}
		if (imageUrl != null) {
			val type = imageUrl.substring(imageUrl.lastIndexOf("."))

			if (type == ".gif") {
				Glide.with(view.context)
					.asGif()
					.load(imageUrl).apply(RequestOptions().centerCrop())
					.into(view)
			} else {
				Glide.with(view.context)
					.load(imageUrl).apply(RequestOptions().centerCrop())
					.dontAnimate()
					.into(view)
			}
		}
	}

	fun setAvatarFromHash(view: ImageView, hash: String?, color: Int = -1) {
		val imageUrl = if (hash != null) "$AVATAR_URL/$hash/$AVATAR_SIZE.png" else null

		setImageBackground(view, imageUrl, color)
	}
}