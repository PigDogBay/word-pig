package com.pigdogbay.library.diagnostics;
import android.util.Log;

public class Timing
{
    long _Start;
    public Timing()
    {
        _Start =  System.nanoTime();
    }
    public void LogDuration()
    {
        LogDuration("Timing");
    }
    public long getMilliseconds()
    {
        return (System.nanoTime()-_Start)/1000000L;
    }
    public void LogDuration(String tag)
    {
        long duration = (System.nanoTime()-_Start)/1000000L;
        StringBuilder sbuff  = new StringBuilder();
        sbuff.append(duration);
        sbuff.append("ms");
        Log.v(tag,sbuff.toString());
    }

}
