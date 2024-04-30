package com.pigdogbay.wordpig.model

/**
 * Created by Mark on 02/04/2015.
 */
class WordChecker(var wordList: List<String>) {
    fun isWord(candidate: String): Boolean {
//        int len = candidate.length();
        for (word in wordList) {
            if (word == candidate) {
                return true
            }
        }
        return false
    }
}
