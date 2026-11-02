package org.advancedkeyboard.app.cursor

import android.view.MotionEvent
import android.view.inputmethod.InputConnection

class SpacebarTrackpadGesture(
    private val cursorController: CursorController,
    private val stepThresholdPx: Float = 30f
) {
    private var startX: Float = 0f
    private var accumulatedDelta: Float = 0f
    private var isDragging: Boolean = false

    fun onTouchEvent(event: MotionEvent, ic: InputConnection?): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                accumulatedDelta = 0f
                isDragging = false
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - startX
                accumulatedDelta += dx
                startX = event.x

                if (Math.abs(accumulatedDelta) >= stepThresholdPx) {
                    isDragging = true
                    val steps = (accumulatedDelta / stepThresholdPx).toInt()
                    if (steps > 0) {
                        for (i in 0 until steps) cursorController.moveRight(ic)
                    } else if (steps < 0) {
                        for (i in 0 until -steps) cursorController.moveLeft(ic)
                    }
                    accumulatedDelta -= (steps * stepThresholdPx)
                }
                return isDragging
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val wasDragging = isDragging
                isDragging = false
                accumulatedDelta = 0f
                return wasDragging
            }
        }
        return false
    }
}
