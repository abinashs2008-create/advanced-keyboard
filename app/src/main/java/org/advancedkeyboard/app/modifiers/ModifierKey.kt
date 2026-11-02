package org.advancedkeyboard.app.modifiers

import android.view.KeyEvent

enum class ModifierKey(
    val label: String,
    val keyCode: Int,
    val metaFlag: Int,
    val canLock: Boolean
) {
    CTRL("Ctrl", KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.META_CTRL_ON or KeyEvent.META_CTRL_LEFT_ON, true),
    ALT("Alt", KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.META_ALT_ON or KeyEvent.META_ALT_LEFT_ON, true),
    SHIFT("Shift", KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.META_SHIFT_ON or KeyEvent.META_SHIFT_LEFT_ON, true),
    FN("Fn", KeyEvent.KEYCODE_FUNCTION, KeyEvent.META_FUNCTION_ON, false),
    CAPS("Caps", KeyEvent.KEYCODE_CAPS_LOCK, KeyEvent.META_CAPS_LOCK_ON, true),
    TAB("Tab", KeyEvent.KEYCODE_TAB, 0, false),
    ESC("Esc", KeyEvent.KEYCODE_ESCAPE, 0, false),
    HOME("Home", KeyEvent.KEYCODE_MOVE_HOME, 0, false),
    END("End", KeyEvent.KEYCODE_MOVE_END, 0, false),
    INSERT("Ins", KeyEvent.KEYCODE_INSERT, 0, false),
    DELETE("Del", KeyEvent.KEYCODE_FORWARD_DEL, 0, false)
}
