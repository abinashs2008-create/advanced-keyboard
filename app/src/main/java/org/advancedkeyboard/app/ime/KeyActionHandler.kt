package org.advancedkeyboard.app.ime

import android.os.SystemClock
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import org.advancedkeyboard.app.modifiers.ModifierStateManager

class KeyActionHandler(
    private val modifierManager: ModifierStateManager
) {
    fun commitText(ic: InputConnection?, text: String) {
        if (ic == null) return
        val meta = modifierManager.computeMetaState()
        if (meta != 0 && text.length == 1) {
            val char = text[0]
            val keyCode = charToKeyCode(char)
            if (keyCode != KeyEvent.KEYCODE_UNKNOWN) {
                sendKeyEventWithMeta(ic, keyCode, meta)
                modifierManager.onKeyDispatched()
                return
            }
        }
        ic.commitText(text, 1)
        modifierManager.onKeyDispatched()
    }

    fun handleDelete(ic: InputConnection?) {
        if (ic == null) return
        val meta = modifierManager.computeMetaState()
        if (meta != 0) {
            sendKeyEventWithMeta(ic, KeyEvent.KEYCODE_DEL, meta)
        } else {
            ic.deleteSurroundingText(1, 0)
        }
        modifierManager.onKeyDispatched()
    }

    fun handleEnter(ic: InputConnection?, editorInfo: EditorInfo?) {
        if (ic == null) return
        val imeOptions = editorInfo?.imeOptions ?: 0
        val actionId = imeOptions and EditorInfo.IME_MASK_ACTION
        if (actionId != EditorInfo.IME_ACTION_NONE && actionId != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(actionId)
        } else {
            commitText(ic, "\n")
        }
    }

    fun sendSpecialKey(ic: InputConnection?, keyCode: Int) {
        if (ic == null) return
        val meta = modifierManager.computeMetaState()
        sendKeyEventWithMeta(ic, keyCode, meta)
        modifierManager.onKeyDispatched()
    }

    private fun sendKeyEventWithMeta(ic: InputConnection, keyCode: Int, metaState: Int) {
        val now = SystemClock.uptimeMillis()
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, metaState))
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, metaState))
    }

    private fun charToKeyCode(c: Char): Int {
        return when (c.lowercaseChar()) {
            'a' -> KeyEvent.KEYCODE_A
            'b' -> KeyEvent.KEYCODE_B
            'c' -> KeyEvent.KEYCODE_C
            'd' -> KeyEvent.KEYCODE_D
            'e' -> KeyEvent.KEYCODE_E
            'f' -> KeyEvent.KEYCODE_F
            'g' -> KeyEvent.KEYCODE_G
            'h' -> KeyEvent.KEYCODE_H
            'i' -> KeyEvent.KEYCODE_I
            'j' -> KeyEvent.KEYCODE_J
            'k' -> KeyEvent.KEYCODE_K
            'l' -> KeyEvent.KEYCODE_L
            'm' -> KeyEvent.KEYCODE_M
            'n' -> KeyEvent.KEYCODE_N
            'o' -> KeyEvent.KEYCODE_O
            'p' -> KeyEvent.KEYCODE_P
            'q' -> KeyEvent.KEYCODE_Q
            'r' -> KeyEvent.KEYCODE_R
            's' -> KeyEvent.KEYCODE_S
            't' -> KeyEvent.KEYCODE_T
            'u' -> KeyEvent.KEYCODE_U
            'v' -> KeyEvent.KEYCODE_V
            'w' -> KeyEvent.KEYCODE_W
            'x' -> KeyEvent.KEYCODE_X
            'y' -> KeyEvent.KEYCODE_Y
            'z' -> KeyEvent.KEYCODE_Z
            '0' -> KeyEvent.KEYCODE_0
            '1' -> KeyEvent.KEYCODE_1
            '2' -> KeyEvent.KEYCODE_2
            '3' -> KeyEvent.KEYCODE_3
            '4' -> KeyEvent.KEYCODE_4
            '5' -> KeyEvent.KEYCODE_5
            '6' -> KeyEvent.KEYCODE_6
            '7' -> KeyEvent.KEYCODE_7
            '8' -> KeyEvent.KEYCODE_8
            '9' -> KeyEvent.KEYCODE_9
            ' ' -> KeyEvent.KEYCODE_SPACE
            '\t' -> KeyEvent.KEYCODE_TAB
            else -> KeyEvent.KEYCODE_UNKNOWN
        }
    }
}
