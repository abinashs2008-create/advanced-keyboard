package org.advancedkeyboard.app.cursor

import android.os.SystemClock
import android.view.KeyEvent
import android.view.inputmethod.InputConnection

class CursorController(
    private val selectionManager: SelectionManager
) {
    fun moveLeft(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_LEFT, meta)
    }

    fun moveRight(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_RIGHT, meta)
    }

    fun moveUp(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_UP, meta)
    }

    fun moveDown(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_DOWN, meta)
    }

    fun moveToWordLeft(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_CTRL_ON or KeyEvent.META_SHIFT_ON else KeyEvent.META_CTRL_ON
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_LEFT, meta)
    }

    fun moveToWordRight(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_CTRL_ON or KeyEvent.META_SHIFT_ON else KeyEvent.META_CTRL_ON
        sendModifierKey(ic, KeyEvent.KEYCODE_DPAD_RIGHT, meta)
    }

    fun moveToLineStart(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_MOVE_HOME, meta)
    }

    fun moveToLineEnd(ic: InputConnection?) {
        if (ic == null) return
        val meta = if (selectionManager.isSelectionMode) KeyEvent.META_SHIFT_ON else 0
        sendModifierKey(ic, KeyEvent.KEYCODE_MOVE_END, meta)
    }

    private fun sendModifierKey(ic: InputConnection, keyCode: Int, metaState: Int) {
        val now = SystemClock.uptimeMillis()
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, metaState))
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, metaState))
    }
}
