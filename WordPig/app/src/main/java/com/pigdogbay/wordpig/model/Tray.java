package com.pigdogbay.wordpig.model;

import android.util.Log;

import com.pigdogbay.wordpig.Defines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mark on 07/04/2015.
 */
public class Tray
{
    private StringBuilder buffer;
    private List<Tile> trayTiles;

    public Tray()
    {
        trayTiles = new ArrayList<Tile>();
        buffer = new StringBuilder();
    }

    public void setTilesIfInTray(List<Tile> tiles)
    {
        trayTiles.clear();
        for (Tile t : tiles)
        {
            if (t.y> Defines.TRAY_MIN_Y && t.y<Defines.TRAY_MAX_Y){
                trayTiles.add(t);
            }
        }
    }

    public void sortTilesByPosition()
    {
        Collections.sort(trayTiles, new Comparator<Tile>() {
            @Override
            public int compare(Tile lhs, Tile rhs) {
                return lhs.x - rhs.x;
            }
        });
    }

    public int getScore()
    {
        int score = 0;
        for (Tile t : trayTiles)
        {
            int index = t.letter-'a';
            score = score + Defines.SCORE_LETTER[index];
        }
        return score;
    }

    @Override
    public String toString() {
        buffer.setLength(0);
        for (Tile t : trayTiles)
        {
            buffer.append((char) t.letter);
        }
        return buffer.toString();
    }
}
