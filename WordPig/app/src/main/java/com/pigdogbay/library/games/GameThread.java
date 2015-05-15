package com.pigdogbay.library.games;

public class GameThread extends Thread
{
    public interface GameThreadClient{
        void Update();
        void Render();
    }
    //Number of frames with a delay of 0ms before the
    //animation thread yields to other running threads
    private static final int NO_DELAYS_PER_YIELD=16;
    //number of frames that can be skipped to keep up with pace of game
    //(The game is updated but not rendered)
    private static final int MAX_FRAME_SKIPS=5;
    private final static long FPS = 30L;
    //period between frames in nanoseconds
    private static final long PERIOD = 1000000000L/FPS;

    private GameThreadClient _Client;
    private boolean _Running=false;

    public GameThread(GameThreadClient client)
    {
        _Client = client;
    }
    public void pause()
    {

    }
    public void setRunning(boolean b)
    {
        _Running=b;
    }
    @Override
    public void run()
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime=0L;
        long excessTime=0L;
        int noDelays=0;
        beforeTime = System.nanoTime();

        _Running=true;
        while(_Running)
        {
            _Client.Update();
            _Client.Render();

            //measure time to update,render and paint
            afterTime = System.nanoTime();
            timeDiff = afterTime-beforeTime;
            //calculate the amount of time to sleep to give steady UPS
            sleepTime = (PERIOD-timeDiff)-overSleepTime;
            if(sleepTime>0)
            {
                try
                {
                    //nano->ms
                    Thread.sleep(sleepTime/1000000L);
                } catch(InterruptedException e){}
                //sleep() is inaccurate, calculate the amount of time it over slept
                overSleepTime = (System.nanoTime() -afterTime)-sleepTime;
            }
            else
            {
                //Missed a frame update
                //accumulate the the excess time
                excessTime -= sleepTime;
                overSleepTime=0L;
                if (++noDelays>=NO_DELAYS_PER_YIELD)
                {
                    //allow another thread to run
                    Thread.yield();
                    noDelays=0;
                }
            }
            beforeTime = System.nanoTime();

            int skips=0;
            while((excessTime>PERIOD)&&(skips<MAX_FRAME_SKIPS))
            {
                //decrease excess
                excessTime-=PERIOD;
                //update state but don't render
                _Client.Update();
                skips++;
            }
        }


    }


}
