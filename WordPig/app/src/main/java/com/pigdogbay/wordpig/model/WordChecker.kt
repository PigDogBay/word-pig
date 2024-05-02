package com.pigdogbay.wordpig.model

class WordChecker(private val wordList: List<String>) {
    fun isWord(candidate: String): Boolean {
        for (word in wordList) {
            if (word == candidate) {
                return true
            }
        }
        return false
    }
}
