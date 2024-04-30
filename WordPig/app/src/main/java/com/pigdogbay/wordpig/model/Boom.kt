package com.pigdogbay.wordpig.model

/**
 * Created by Mark on 07/04/2015.
 */
class Boom {
    private val stack: MutableList<String>
    var count = 0
    var latestMessage = ""
    var isMessageAvailable = false

    init {
        stack = ArrayList()
    }

    fun addMessage(message: String) {
        stack.add(message)
    }

    fun update() {
        if (isMessageAvailable) {
            count++
            if (count > MAX_COUNT) {
                isMessageAvailable = false
            }
        } else if (stack.size > 0) {
            latestMessage = stack[0]
            stack.removeAt(0)
            count = 0
            isMessageAvailable = true
        }
    }

    companion object {
        const val MAX_COUNT = 25
    }
}
