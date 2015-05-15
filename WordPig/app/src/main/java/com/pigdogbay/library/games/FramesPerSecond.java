package com.pigdogbay.library.games;
public class FramesPerSecond {
    long _FrameCount=0L,_StartTime=0L;
    String _FPS="";

    public void CalculateFPS()
    {
        _FrameCount++;
        long currentTime = System.currentTimeMillis();
        if ((currentTime-_StartTime)>1000L)
        {
            _StartTime=currentTime;
            _FPS = Long.toString(_FrameCount);
            _FrameCount=0;
        }

    }

    public String getFPS(){
        return _FPS;
    }
}
