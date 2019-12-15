package com.alxdthn.tfsboards.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.alxdthn.tfsboards.R
import kotlin.math.min
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

class CustomUserAvatar @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

	companion object {
		private val SCALE_TYPE = ScaleType.CENTER_CROP
		private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.GRAY
		private const val DEFAULT_BORDER_COLOR = Color.BLACK
		private const val DEFAULT_LABEL_COLOR = Color.WHITE
		private const val DEFAULT_LABEL = "A"
		private const val DEFAULT_LABEL_TEXT_SCALE = 0.7f
		private const val DEFAULT_BORDER_WIDTH = 0f
	}

	private val labelBounds = Rect()
	private val drawableRect = RectF()
	private val borderRect = RectF()
	private val shaderMatrix = Matrix()
	private val bitmapPaint = Paint()
	private val borderPaint = Paint()
	private val labelPaint = Paint()
	private val circleBackgroundPaint = Paint()

	private var circleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR
	private var borderColor = DEFAULT_BORDER_COLOR
	private var label: String? = DEFAULT_LABEL
	private var borderWidth = DEFAULT_BORDER_WIDTH
	private var convertedLabel: String = DEFAULT_LABEL

	private var bitmap: Bitmap? = null
	private lateinit var bitmapShader: BitmapShader

	private var bitmapWidth = 0
	private var bitmapHeight = 0
	private var drawableRadius = 0f
	private var borderRadius = 0f

	private var ready = true
	private var setupPending = true

	init {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomUserAvatar, defStyle, 0)

		typedArray.apply {
			circleBackgroundColor = getColor(R.styleable.CustomUserAvatar_color, DEFAULT_CIRCLE_BACKGROUND_COLOR)
			borderWidth = getDimension(R.styleable.CustomUserAvatar_border, DEFAULT_BORDER_WIDTH)
			borderColor = getColor(R.styleable.CustomUserAvatar_border_color, DEFAULT_BORDER_COLOR)
			label = getString(R.styleable.CustomUserAvatar_label)
		}

		typedArray.recycle()
		init()
	}

	private fun init() {
		super.setScaleType(SCALE_TYPE)
		ready = true

		if (setupPending) {
			setup()
			setupPending = false
		}
	}

	private fun initializeBitmap() {
		bitmap = if (drawable is BitmapDrawable) {
			(drawable as BitmapDrawable).bitmap
		} else {
			null
		}
		setup()
	}

	private fun setup() {
		if (!ready) {
			setupPending = true
			return
		}
		else if (height == 0 && width == 0) {
			return
		}

		borderRect.set(calculateBounds())
		drawableRect.set(borderRect)
		drawableRadius = min(drawableRect.width() / 2f, drawableRect.height() / 2f)

		if (bitmap != null) {
			setupBitmap()
		} else {
			setupColorAndLabel()
		}
		if (borderWidth > 0) {
			setupBorder()
		}
		invalidate()
	}

	private fun setupBorder() {
		borderPaint.style = Paint.Style.STROKE
		borderPaint.isAntiAlias = true
		borderPaint.color = borderColor
		borderPaint.strokeWidth = borderWidth

		borderRadius = min(
			(borderRect.height() - borderWidth) / 2f,
			(borderRect.width() - borderWidth) / 2f
		)
	}

	private fun setupBitmap() {
		bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
		bitmapPaint.isAntiAlias = true
		bitmapPaint.shader = bitmapShader

		bitmapWidth = bitmap!!.width
		bitmapHeight = bitmap!!.height

		updateShaderMatrix()
	}

	private fun setupColorAndLabel() {
		convertedLabel = convertStringToLabel(label)

		circleBackgroundPaint.style = Paint.Style.FILL
		circleBackgroundPaint.isAntiAlias = true
		circleBackgroundPaint.color = circleBackgroundColor

		labelPaint.color = DEFAULT_LABEL_COLOR
		labelPaint.textSize = drawableRadius * DEFAULT_LABEL_TEXT_SCALE
		labelPaint.getTextBounds(convertedLabel, 0, convertedLabel.length, labelBounds)
	}

	@SuppressLint("DefaultLocale")
	private fun convertStringToLabel(input: String?): String {
		return if (input != null && input.isNotEmpty()) {
			input.toUpperCase()[0].toString()
		} else {
			DEFAULT_LABEL
		}
	}

	private fun calculateBounds(): RectF {
		val availableWidth = width - paddingLeft - paddingRight
		val availableHeight = height - paddingTop - paddingBottom

		val sideLength = min(availableWidth, availableHeight)

		val left = paddingLeft + (availableWidth - sideLength) / 2f
		val top = paddingTop + (availableHeight - sideLength) / 2f

		return RectF(left, top, left + sideLength, top + sideLength)
	}


	private fun updateShaderMatrix() {
		val scale: Float
		var dx = 0f
		var dy = 0f

		shaderMatrix.set(null)

		if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
			scale = drawableRect.height() / bitmapHeight.toFloat()
			dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
		} else {
			scale = drawableRect.width() / bitmapWidth.toFloat()
			dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
		}

		shaderMatrix.setScale(scale, scale)
		shaderMatrix.postTranslate(
			(dx + 0.5f).toInt() + drawableRect.left,
			(dy + 0.5f).toInt() + drawableRect.top
		)
		bitmapShader.setLocalMatrix(shaderMatrix)
	}


	override fun onDraw(canvas: Canvas) {
		if (bitmap == null) {
			drawBackground(canvas)
			drawLabel(canvas)
		} else {
			drawBitmap(canvas)
		}
		if (borderWidth > 0) {
			drawBorder(canvas)
		}
	}

	private fun drawBackground(canvas: Canvas) {
		canvas.drawCircle(
			drawableRect.centerX(),
			drawableRect.centerY(),
			drawableRadius,
			circleBackgroundPaint
		)
	}

	private fun drawLabel(canvas: Canvas) {
		canvas.drawText(
			convertedLabel,
			drawableRect.centerX() - labelBounds.exactCenterX(),
			drawableRect.centerY() - labelBounds.exactCenterY(),
			labelPaint
		)
	}

	private fun drawBitmap(canvas: Canvas) {
		canvas.drawCircle(
			drawableRect.centerX(),
			drawableRect.centerY(),
			drawableRadius,
			bitmapPaint
		)
	}

	private fun drawBorder(canvas: Canvas) {
		canvas.drawCircle(
			borderRect.centerX(),
			borderRect.centerY(),
			borderRadius,
			borderPaint
		)
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		setup()
	}

	override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
		super.setPadding(left, top, right, bottom)
		setup()
	}

	override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
		super.setPaddingRelative(start, top, end, bottom)
		setup()
	}

	override fun setImageDrawable(drawable: Drawable?) {
		super.setImageDrawable(drawable)
		initializeBitmap()
	}

	override fun setImageBitmap(bm: Bitmap?) {
		super.setImageBitmap(bm)
		initializeBitmap()
	}

	override fun setImageResource(resId: Int) {
		super.setImageResource(resId)
		initializeBitmap()
	}

	override fun setImageURI(uri: Uri?) {
		super.setImageURI(uri)
		initializeBitmap()
	}

	fun setLabel(newLabel: String) {
		label = newLabel
		setup()
	}

	fun setColor(newColor: Int) {
		circleBackgroundColor = newColor
		setup()
	}

	fun setBoardWidth(newWidth: Float) {
		borderWidth = newWidth
		setup()
	}

	fun setBorderColor(color: Int) {
		borderColor = color
		setup()
	}
}
