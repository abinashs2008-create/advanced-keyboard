package org.advancedkeyboard.app.cursor

import android.view.inputmethod.InputConnection

class SelectionManager {
    var isSelectionMode: Boolean = false
        private set
    private var anchorPosition: Int = -1

    fun toggleSelectionMode(currentPosition: Int): Boolean {
        isSelectionMode = !isSelectionMode
        anchorPosition = if (isSelectionMode) currentPosition else -1
        return isSelectionMode
    }

    fun reset() {
        isSelectionMode = false
        anchorPosition = -1
    }

    fun updateSelection(ic: InputConnection?, newPosition: Int) {
        if (ic == null || !isSelectionMode || anchorPosition < 0) return
        val start = minOf(anchorPosition, newPosition)
        val end = maxOf(anchorPosition, newPosition)
        ic.setSelection(start, end)
    }
}
