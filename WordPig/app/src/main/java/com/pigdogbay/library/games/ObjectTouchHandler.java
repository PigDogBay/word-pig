package com.pigdogbay.library.games;


import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Maps touch events to registered listeners Useful for bitmap buttons
 *
 */
public class ObjectTouchHandler implements OnTouchListener {

    public enum TouchState {
        None, Down, DragInside, DragOutside, UpInside, UpOutside, Cancel
    }

    public interface ITouchable {

        /**
         * Checks to see if the event is inside the touch object Note -
         * pre-scale any bounds before hand
         *
         * @param x
         *            x co-ord of the touch screen event
         * @param y
         *            y co-ord of the touch screen event
         * @return true if the event is inside
         */
        boolean contains(int x, int y);

        /**
         * Use this method to listen for touch events, keep any processing very
         * lightweight e.g. just set flags
         *
         * @param state
         *            new state
         *
         * @param x
         *            x co-ord of the touch screen event
         * @param y
         *            y co-ord of the touch screen event
         */
        void setTouchState(TouchState state, int x, int y);

    }

    List<ITouchable> _Touchables;
    ITouchable _Selected = null;
    private float _XScale, _YScale;
    public int _X,_Y;

    public float getXScale() {
        return _XScale;
    }
    /**
     * The buffer may be a different size to the screen
     * xScale = Buffer width / screen width
     * @param xScale
     */
    public void setXScale(float xScale) {
        _XScale = xScale;
    }
    public float getYScale() {
        return _YScale;
    }
    /**
     * The buffer may be a different size to the screen
     * yScale = Buffer height / screen height
     * @param yScale
     */
    public void setYScale(float yScale) {
        _YScale = yScale;
    }

    public ObjectTouchHandler() {
        _Touchables = new ArrayList<ObjectTouchHandler.ITouchable>();
        _XScale = 1.0f;
        _YScale = 1.0f;
    }

    public void add(ITouchable touchable) {
        _Touchables.add(touchable);
    }

    public void remove(ITouchable touchable) {
        _Touchables.remove(touchable);
    }

    public void clearTouchables() {
        _Touchables.clear();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _X = (int) (event.getX() * _XScale);
        _Y = (int) (event.getY() * _YScale);

        if (event.getActionIndex() != 0) {
            // ignore multi-touch events
            return false;
        }
        TouchState state = TouchState.None;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                _Selected = findSelected();
                if (_Selected!=null)
                {
                    _Selected.setTouchState(TouchState.Down, _X, _Y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (_Selected != null) {
                    state = _Selected.contains(_X,_Y) ? TouchState.UpInside
                            : TouchState.UpOutside;
                    _Selected.setTouchState(state, _X, _Y);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (_Selected != null) {
                    _Selected.setTouchState(TouchState.Cancel, _X, _Y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (_Selected != null) {
                    state = _Selected.contains(_X,_Y) ? TouchState.DragInside
                            : TouchState.DragOutside;
                    _Selected.setTouchState(state, _X, _Y);
                }
                break;
        }
        // Selected null - return false - not handling event
        // Selected not null - return true - handling event
        return _Selected != null;
    }

    private ITouchable findSelected() {
        for (ITouchable t : _Touchables) {
            if (t.contains(_X,_Y)) {
                return t;
            }
        }
        return null;

    }
}
