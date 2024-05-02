package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.patterns.ObservableProperty

enum class ScreenState { Home, Game, GameOver }

class Model {
    val board = Board(this)
    val screenStateObserver = ObservableProperty(this,ScreenState.Home)
}
