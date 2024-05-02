package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.utils.ObservableProperty

enum class ScreenState { Home, Game, GameOver }

interface GameEventListener {
    fun onGameEvent(sender: Any, id: Int)
}

class Model {
    val screenStateObserver = ObservableProperty(ScreenState.Home)
}