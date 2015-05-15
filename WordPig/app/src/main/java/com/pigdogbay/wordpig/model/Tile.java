package com.pigdogbay.wordpig.model;

/**
 * Created by Mark on 01/04/2015.
 */
public class Tile
{
    public int letter;
    public volatile int x,y;

    public Tile(int letter, int x, int y)
    {
        this.letter = letter;
        this.x = x;
        this.y = y;
    }
}
