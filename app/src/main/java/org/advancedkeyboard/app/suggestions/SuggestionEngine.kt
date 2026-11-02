package org.advancedkeyboard.app.suggestions

import android.view.inputmethod.InputConnection

class SuggestionEngine {
    private val trie = WordTrie()

    init {
        val commonWords = listOf(
            "the" to 100, "and" to 95, "that" to 90, "have" to 85, "for" to 80,
            "not" to 75, "with" to 70, "you" to 68, "this" to 65, "but" to 60,
            "keyboard" to 50, "android" to 50, "advanced" to 40
        )
        for ((word, freq) in commonWords) trie.insert(word, freq)
    }

    fun getSuggestions(ic: InputConnection?): List<String> {
        if (ic == null) return emptyList()
        val textBefore = ic.getTextBeforeCursor(32, 0)?.toString() ?: return emptyList()
        val currentWord = extractCurrentWord(textBefore)
        if (currentWord.isEmpty()) return emptyList()
        return trie.findCandidates(currentWord, 3)
    }

    fun commitSuggestion(ic: InputConnection?, candidate: String) {
        if (ic == null) return
        val textBefore = ic.getTextBeforeCursor(32, 0)?.toString() ?: ""
        val currentWord = extractCurrentWord(textBefore)
        if (currentWord.isNotEmpty()) ic.deleteSurroundingText(currentWord.length, 0)
        ic.commitText("$candidate ", 1)
    }

    private fun extractCurrentWord(text: String): String {
        var i = text.length - 1
        while (i >= 0 && Character.isLetterOrDigit(text[i])) i--
        return text.substring(i + 1)
    }
}
