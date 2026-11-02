package org.advancedkeyboard.app.editing

import android.os.SystemClock
import android.view.KeyEvent
import android.view.inputmethod.InputConnection

class EditingShortcutDispatcher {
    fun selectAll(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.selectAll)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_A, KeyEvent.META_CTRL_ON)
        }
    }

    fun copy(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.copy)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_C, KeyEvent.META_CTRL_ON)
        }
    }

    fun cut(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.cut)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_X, KeyEvent.META_CTRL_ON)
        }
    }

    fun paste(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.paste)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_V, KeyEvent.META_CTRL_ON)
        }
    }

    fun undo(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.undo)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_ON)
        }
    }

    fun redo(ic: InputConnection?) {
        if (ic == null) return
        if (!ic.performContextMenuAction(android.R.id.redo)) {
            sendModifierKey(ic, KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_ON or KeyEvent.META_SHIFT_ON)
        }
    }

    private fun sendModifierKey(ic: InputConnection, keyCode: Int, metaState: Int) {
        val now = SystemClock.uptimeMillis()
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, metaState))
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, metaState))
    }
}
