package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.utils.ObservableProperty

/**
 * Created by Mark on 07/04/2015.
 */
class Screen {
    enum class ScreenState {
        Home,
        Game,
        GameOver
    }

    var screenStateObserver: ObservableProperty<ScreenState?>

    init {
        screenStateObserver = ObservableProperty(ScreenState.Home)
    }
}
