package org.advancedkeyboard.app.editing

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardHelper(context: Context) {
    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    fun getClipboardText(): String? {
        val clip = clipboardManager?.primaryClip ?: return null
        if (clip.itemCount > 0) return clip.getItemAt(0).text?.toString()
        return null
    }

    fun setClipboardText(label: String, text: String) {
        val clip = ClipData.newPlainText(label, text)
        clipboardManager?.setPrimaryClip(clip)
    }
}
