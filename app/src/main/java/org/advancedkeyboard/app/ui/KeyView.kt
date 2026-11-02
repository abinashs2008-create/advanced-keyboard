package org.advancedkeyboard.app.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import org.advancedkeyboard.app.modifiers.ModifierState
import org.advancedkeyboard.app.themes.KeyboardTheme

class KeyView(
    context: Context,
    var primaryLabel: String,
    var secondaryLabel: String? = null,
    private val onClick: (KeyView) -> Unit
) : View(context) {

    var modifierState: ModifierState = ModifierState.IDLE
        set(value) { field = value; invalidate() }

    var theme: KeyboardTheme = KeyboardTheme.get(org.advancedkeyboard.app.themes.ThemeType.DARK_SLATE)
        set(value) { field = value; invalidate() }

    var showBorders: Boolean = true
        set(value) { field = value; invalidate() }

    private var isPressedState = false
    private val bgRect = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }

    init {
        contentDescription = primaryLabel
        isFocusable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bgRect.set(4f, 4f, w - 4f, h - 4f)
        textPaint.textSize = h * 0.42f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bgColor = when {
            modifierState == ModifierState.LOCKED -> theme.accentLockedColor
            modifierState == ModifierState.ONE_SHOT -> theme.accentColor
            isPressedState -> theme.keyPressedColor
            else -> theme.keyColor
        }
        val textColor = when {
            modifierState != ModifierState.IDLE -> 0xFFFFFFFF.toInt()
            isPressedState -> theme.accentColor
            else -> theme.textPrimaryColor
        }

        bgPaint.color = bgColor
        canvas.drawRoundRect(bgRect, 10f, 10f, bgPaint)
        if (showBorders) {
            borderPaint.color = theme.borderColor
            canvas.drawRoundRect(bgRect, 10f, 10f, borderPaint)
        }
        textPaint.color = textColor
        val baseline = (height / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)
        canvas.drawText(primaryLabel, width / 2f, baseline, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> { isPressedState = true; invalidate(); return true }
            MotionEvent.ACTION_UP -> {
                if (isPressedState) onClick(this)
                isPressedState = false
                invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> { isPressedState = false; invalidate(); return true }
        }
        return super.onTouchEvent(event)
    }
}
