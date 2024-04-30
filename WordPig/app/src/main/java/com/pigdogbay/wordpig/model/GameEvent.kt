package com.pigdogbay.wordpig.model

/**
 * Created by Mark on 18/05/2015.
 */
class GameEvent {
    interface IGameEventListener {
        fun onGameEvent(sender: Any, id: Int)
    }

    var _Listeners: MutableList<IGameEventListener>

    init {
        _Listeners = ArrayList()
    }

    fun addListener(listener: IGameEventListener) {
        _Listeners.add(listener)
    }

    fun removeListener(listener: IGameEventListener) {
        _Listeners.remove(listener)
    }

    fun removeAll() {
        _Listeners.clear()
    }

    fun fire(sender: Any?, id: Int) {
        for (listener in _Listeners) {
            listener.onGameEvent(sender!!, id)
        }
    }
}
