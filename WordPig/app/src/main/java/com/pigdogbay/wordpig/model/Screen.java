package com.pigdogbay.wordpig.model;

import com.pigdogbay.library.utils.ObservableProperty;

/**
 * Created by Mark on 07/04/2015.
 */
public class Screen
{
    public enum ScreenState
    {
        Home,
        Game,
        GameOver
    }

    public ObservableProperty<ScreenState> screenStateObserver;

    public Screen()
    {
        screenStateObserver = new ObservableProperty<ScreenState>(ScreenState.Home);
    }

}
