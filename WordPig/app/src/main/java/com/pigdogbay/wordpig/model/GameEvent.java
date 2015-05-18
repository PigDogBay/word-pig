package com.pigdogbay.wordpig.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 18/05/2015.
 */
public class GameEvent
{
    public interface IGameEventListener
    {
        void onGameEvent(Object sender, int id);
    }

    List<IGameEventListener> _Listeners;

    public GameEvent(){
        _Listeners = new ArrayList<IGameEventListener>();
    }

    public void addListener(IGameEventListener listener)
    {
        _Listeners.add(listener);
    }
    public void removeListener(IGameEventListener listener)
    {
        _Listeners.remove(listener);
    }

    public void removeAll()
    {
        _Listeners.clear();
    }

    public void fire(Object sender, int id)
    {
        for (IGameEventListener listener : _Listeners){
            listener.onGameEvent(sender,id);
        }
    }

}
