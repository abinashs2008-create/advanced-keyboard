package org.advancedkeyboard.app.ui

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import org.advancedkeyboard.app.cursor.SpacebarTrackpadGesture
import org.advancedkeyboard.app.themes.KeyboardTheme

class NormalKeyboardView(
    context: Context,
    private val onCommitText: (String) -> Unit,
    private val onDelete: () -> Unit,
    private val onEnter: () -> Unit,
    private val onToggleShift: () -> Unit,
    private val onShowSymbols: (Boolean) -> Unit,
    private val trackpadGesture: SpacebarTrackpadGesture
) : LinearLayout(context) {

    private var isShifted = false
    private var isSymbolsMode = false
    private var showNumberRow = true
    private var showBorders = true
    private var fontSizeScale = 0.85f
    private var currentTheme = KeyboardTheme.get(org.advancedkeyboard.app.themes.ThemeType.DARK_SLATE)

    private val alphaKeys = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("z", "x", "c", "v", "b", "n", "m")
    )
    private val symbolKeys = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("@", "#", "$", "%", "&", "-", "+", "(", ")"),
        listOf("*", "\"", "'", ":", ";", "!", "?")
    )
    private val numberRowKeys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        rebuildLayout()
    }

    fun updateConfig(theme: KeyboardTheme, isShifted: Boolean, isSymbols: Boolean, showNumberRow: Boolean, showBorders: Boolean, fontSizeScale: Float = 0.85f) {
        this.currentTheme = theme
        this.isShifted = isShifted
        this.isSymbolsMode = isSymbols
        this.showNumberRow = showNumberRow
        this.showBorders = showBorders
        this.fontSizeScale = fontSizeScale
        rebuildLayout()
    }

    private fun rebuildLayout() {
        removeAllViews()
        val rowHeight = 135

        if (showNumberRow && !isSymbolsMode) {
            val numRow = createRow(rowHeight)
            for (num in numberRowKeys) {
                numRow.addView(KeyView(context, num, null, { onCommitText(num) }).apply {
                    this.theme = currentTheme
                    this.showBorders = showBorders
                    this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
                    layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                })
            }
            addView(numRow)
        }

        val rows = if (isSymbolsMode) symbolKeys else alphaKeys
        for (r in rows.take(2)) {
            val row = createRow(rowHeight)
            for (char in r) {
                val label = if (isShifted) char.uppercase() else char
                row.addView(KeyView(context, label, null, { onCommitText(label) }).apply {
                    this.theme = currentTheme
                    this.showBorders = showBorders
                    this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
                    layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                })
            }
            addView(row)
        }

        val row3 = createRow(rowHeight)
        row3.addView(KeyView(context, if (isSymbolsMode) "=/<" else (if (isShifted) "⇪" else "⇧"), null, { onToggleShift() }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.4f)
        })
        for (char in rows[2]) {
            val label = if (isShifted) char.uppercase() else char
            row3.addView(KeyView(context, label, null, { onCommitText(label) }).apply {
                this.theme = currentTheme
                this.showBorders = showBorders
                this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
                layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            })
        }
        row3.addView(KeyView(context, "⌫", null, { onDelete() }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.4f)
        })
        addView(row3)

        val row4 = createRow(rowHeight)
        row4.addView(KeyView(context, if (isSymbolsMode) "ABC" else "?123", null, { onShowSymbols(!isSymbolsMode) }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.3f)
        })
        row4.addView(KeyView(context, ",", null, { onCommitText(",") }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        })
        row4.addView(KeyView(context, "Space", null, { onCommitText(" ") }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 4.4f)
            setOnTouchListener { _, event -> trackpadGesture.onTouchEvent(event, null) }
        })
        row4.addView(KeyView(context, ".", null, { onCommitText(".") }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        })
        row4.addView(KeyView(context, "↵", null, { onEnter() }).apply {
            this.theme = currentTheme
            this.showBorders = showBorders
            this.fontSizeScale = this@NormalKeyboardView.fontSizeScale
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.3f)
        })
        addView(row4)
    }

    private fun createRow(rowHeight: Int): LinearLayout {
        return LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)
        }
    }
}
