package com.pigdogbay.library.games;

/**
 * Created by Mark on 08/04/2015.
 */
public class Timer
{
    private long start;
    private long duration;
    public Timer(int milliseconds)
    {
        reset(milliseconds);
}
    public boolean hasTimeElapsed()
    {
        long timeElapsed = System.nanoTime() - start;
        return timeElapsed>duration;
    }

    public void reset()
    {
        start =  System.nanoTime();
    }

    public void reset(long milliseconds)
    {
        //convert to nanoseconds
        duration = milliseconds*1000000L;
        start =  System.nanoTime();
    }
}
