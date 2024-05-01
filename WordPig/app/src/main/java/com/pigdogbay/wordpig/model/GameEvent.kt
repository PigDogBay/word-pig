package com.pigdogbay.wordpig.model

/**
 * Created by Mark on 18/05/2015.
 */
class GameEvent {
    interface GameEventListener {
        fun onGameEvent(sender: Any, id: Int)
    }

    var listeners = ArrayList<GameEventListener>()

    fun addListener(listener: GameEventListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GameEventListener) {
        listeners.remove(listener)
    }

    fun removeAll() {
        listeners.clear()
    }

    fun fire(sender: Any, id: Int) {
        for (listener in listeners) {
            listener.onGameEvent(sender, id)
        }
    }
}
