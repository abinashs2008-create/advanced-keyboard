package org.advancedkeyboard.app.suggestions

class WordTrie {
    private class Node {
        val children = mutableMapOf<Char, Node>()
        var isWord: Boolean = false
        var frequency: Int = 0
    }
    private val root = Node()

    fun insert(word: String, frequency: Int = 1) {
        val clean = word.lowercase().trim()
        if (clean.isEmpty()) return
        var curr = root
        for (ch in clean) curr = curr.children.getOrPut(ch) { Node() }
        curr.isWord = true
        curr.frequency = frequency
    }

    fun findCandidates(prefix: String, maxCount: Int = 3): List<String> {
        val clean = prefix.lowercase().trim()
        if (clean.isEmpty()) return emptyList()
        var curr = root
        for (ch in clean) curr = curr.children[ch] ?: return emptyList()
        val results = mutableListOf<Pair<String, Int>>()
        collect(curr, StringBuilder(clean), results)
        return results.sortedByDescending { it.second }.take(maxCount).map { it.first }
    }

    private fun collect(node: Node, prefixBuilder: StringBuilder, results: MutableList<Pair<String, Int>>) {
        if (node.isWord) results.add(Pair(prefixBuilder.toString(), node.frequency))
        for ((ch, child) in node.children) {
            prefixBuilder.append(ch)
            collect(child, prefixBuilder, results)
            prefixBuilder.setLength(prefixBuilder.length - 1)
        }
    }
}
