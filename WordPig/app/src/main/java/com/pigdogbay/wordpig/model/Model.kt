package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.utils.ObservableProperty

enum class ScreenState { Home, Game, GameOver }
class Model {
    val screenStateObserver = ObservableProperty(ScreenState.Home)
}
