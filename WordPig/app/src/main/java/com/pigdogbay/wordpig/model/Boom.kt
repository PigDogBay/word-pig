package com.pigdogbay.wordpig.model

/**
 * Created by Mark on 07/04/2015.
 */
class Boom {
    private val maxCount = 25
    private val stack = ArrayList<String>()
    var count = 0
    var latestMessage = ""
    var isMessageAvailable = false

    fun addMessage(message: String) {
        stack.add(message)
    }

    fun update() {
        if (isMessageAvailable) {
            count++
            if (count > maxCount) {
                isMessageAvailable = false
            }
        } else if (stack.size > 0) {
            latestMessage = stack[0]
            stack.removeAt(0)
            count = 0
            isMessageAvailable = true
        }
    }

}
