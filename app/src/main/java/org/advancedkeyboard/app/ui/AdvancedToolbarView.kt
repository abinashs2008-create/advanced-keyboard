package org.advancedkeyboard.app.ui

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import org.advancedkeyboard.app.modifiers.ModifierKey
import org.advancedkeyboard.app.modifiers.ModifierState
import org.advancedkeyboard.app.modifiers.ModifierStateManager
import org.advancedkeyboard.app.themes.KeyboardTheme

class AdvancedToolbarView(
    context: Context,
    private val modifierManager: ModifierStateManager,
    private val onModifierClick: (ModifierKey) -> Unit,
    private val onSpecialKeyClick: (Int) -> Unit
) : LinearLayout(context) {

    private val keyViews = mutableMapOf<ModifierKey, KeyView>()
    private var currentTheme = KeyboardTheme.get(org.advancedkeyboard.app.themes.ThemeType.DARK_SLATE)

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        buildToolbar()
    }

    fun setTheme(theme: KeyboardTheme) {
        currentTheme = theme
        for ((_, view) in keyViews) view.theme = theme
    }

    fun updateModifierState(key: ModifierKey, state: ModifierState) {
        keyViews[key]?.modifierState = state
    }

    private fun buildToolbar() {
        removeAllViews()
        val keyWidth = 130
        val modifierKeys = listOf(
            ModifierKey.CTRL, ModifierKey.ALT, ModifierKey.SHIFT, ModifierKey.FN,
            ModifierKey.TAB, ModifierKey.ESC, ModifierKey.DEL, ModifierKey.HOME,
            ModifierKey.END, ModifierKey.INSERT
        )
        for (mod in modifierKeys) {
            val keyView = KeyView(context, mod.label, null, { onModifierClick(mod) }).apply {
                this.theme = currentTheme
                this.modifierState = modifierManager.getState(mod)
                layoutParams = LayoutParams(keyWidth, LayoutParams.MATCH_PARENT).apply { setMargins(3, 2, 3, 2) }
            }
            keyViews[mod] = keyView
            addView(keyView)
        }
    }
}
