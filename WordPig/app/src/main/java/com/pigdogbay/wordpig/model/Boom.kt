package com.pigdogbay.wordpig.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 07/04/2015.
 */
public class Boom
{
    private List<String> stack;
    public int count;
    public String latestMessage;
    public boolean isMessageAvailable;

    public static final int MAX_COUNT = 25;

    public Boom()
    {
        stack = new ArrayList<String>();
        count = 0;
        latestMessage="";
        isMessageAvailable = false;
    }

    public void addMessage(String message)
    {
        stack.add(message);
    }

    public void update()
    {
        if (isMessageAvailable)
        {
            count++;
            if (count>MAX_COUNT)
            {
                isMessageAvailable = false;
            }
        }
        else if (stack.size()>0)
        {
            latestMessage = stack.get(0);
            stack.remove(0);
            count=0;
            isMessageAvailable = true;
        }
    }
}
