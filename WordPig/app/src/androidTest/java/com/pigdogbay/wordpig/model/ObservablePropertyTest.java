package com.pigdogbay.wordpig.model;

import com.pigdogbay.library.utils.ObservableProperty;

import junit.framework.TestCase;


public class ObservablePropertyTest extends TestCase
{
    public enum Veg
    {
        carrots, peppers, onions, chillis, mushrooms
    }

    public class Listener implements ObservableProperty.PropertyChangedObserver<Veg>
    {
        public Veg newValue;

        @Override
        public void update(ObservableProperty<Veg> sender, Veg update) {
            newValue = update;
        }

    }
    public void testEnum1()
    {
        ObservableProperty<Veg> target = new ObservableProperty<ObservablePropertyTest.Veg>(Veg.onions);
        Listener listener = new Listener();
        target.addObserver(listener);
        target.setValue(Veg.chillis);
        assertEquals(Veg.chillis, listener.newValue);
    }

}
