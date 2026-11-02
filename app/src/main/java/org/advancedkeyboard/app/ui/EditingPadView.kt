package org.advancedkeyboard.app.ui

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import org.advancedkeyboard.app.cursor.CursorController
import org.advancedkeyboard.app.cursor.SelectionManager
import org.advancedkeyboard.app.editing.EditingShortcutDispatcher
import org.advancedkeyboard.app.themes.KeyboardTheme

class EditingPadView(
    context: Context,
    private val cursorController: CursorController,
    private val selectionManager: SelectionManager,
    private val shortcutDispatcher: EditingShortcutDispatcher,
    private val getInputConnection: () -> android.view.inputmethod.InputConnection?
) : LinearLayout(context) {

    private var currentTheme = KeyboardTheme.get(org.advancedkeyboard.app.themes.ThemeType.DARK_SLATE)

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        buildEditingLayout()
    }

    fun setTheme(theme: KeyboardTheme) {
        currentTheme = theme
        buildEditingLayout()
    }

    private fun buildEditingLayout() {
        removeAllViews()
        val rowHeight = 125

        val actionRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)
        }
        val actions = listOf(
            "Select All" to { shortcutDispatcher.selectAll(getInputConnection()) },
            "Cut" to { shortcutDispatcher.cut(getInputConnection()) },
            "Copy" to { shortcutDispatcher.copy(getInputConnection()) },
            "Paste" to { shortcutDispatcher.paste(getInputConnection()) },
            "Undo" to { shortcutDispatcher.undo(getInputConnection()) },
            "Redo" to { shortcutDispatcher.redo(getInputConnection()) }
        )
        for ((label, action) in actions) {
            actionRow.addView(KeyView(context, label, null, { action() }).apply {
                this.theme = currentTheme
                layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            })
        }
        addView(actionRow)

        val row2 = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)
        }
        row2.addView(KeyView(context, "⇤ Line", null, { cursorController.moveToLineStart(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        row2.addView(KeyView(context, "▲", null, { cursorController.moveUp(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.6f)
        })
        row2.addView(KeyView(context, "Line ⇥", null, { cursorController.moveToLineEnd(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        addView(row2)

        val row3 = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)
        }
        row3.addView(KeyView(context, "◀", null, { cursorController.moveLeft(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        row3.addView(KeyView(context, if (selectionManager.isSelectionMode) "SELECTING" else "SELECT", null, { keyView ->
            val active = selectionManager.toggleSelectionMode(0)
            keyView.primaryLabel = if (active) "SELECTING" else "SELECT"
            keyView.modifierState = if (active) org.advancedkeyboard.app.modifiers.ModifierState.ONE_SHOT else org.advancedkeyboard.app.modifiers.ModifierState.IDLE
            keyView.invalidate()
        }).apply {
            this.theme = currentTheme
            this.modifierState = if (selectionManager.isSelectionMode) org.advancedkeyboard.app.modifiers.ModifierState.ONE_SHOT else org.advancedkeyboard.app.modifiers.ModifierState.IDLE
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.6f)
        })
        row3.addView(KeyView(context, "▶", null, { cursorController.moveRight(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        addView(row3)

        val row4 = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)
        }
        row4.addView(KeyView(context, "◀ Word", null, { cursorController.moveToWordLeft(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        row4.addView(KeyView(context, "▼", null, { cursorController.moveDown(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.6f)
        })
        row4.addView(KeyView(context, "Word ▶", null, { cursorController.moveToWordRight(getInputConnection()) }).apply {
            this.theme = currentTheme
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.2f)
        })
        addView(row4)
    }
}
