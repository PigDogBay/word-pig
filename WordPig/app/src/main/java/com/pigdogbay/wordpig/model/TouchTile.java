package com.pigdogbay.wordpig.model;

import com.pigdogbay.library.games.ObjectTouchHandler;
import com.pigdogbay.wordpig.Defines;

/**
 * Created by Mark on 01/04/2015.
 */
public class TouchTile implements ObjectTouchHandler.ITouchable
{
    public Tile tile;

    public TouchTile(Tile tile)
    {
        this.tile = tile;
    }

    @Override
    public boolean contains(int x, int y) {
        int tx = tile.x;
        int ty = tile.y;
        if (x>tx && y>ty)
        {
            tx = tx + Defines.TILE_WIDTH;
            if (x<tx)
            {
                ty = ty+Defines.TILE_HEIGHT;
                return y<ty;
            }
        }
        return false;
    }

    @Override
    public void setTouchState(ObjectTouchHandler.TouchState state, int x, int y) {
        switch (state)
        {
            case None:
                break;
            case Down:
            case DragInside:
            case DragOutside:
            case UpInside:
            case UpOutside:
                tile.x = x-Defines.TILE_WIDTH/2;
                tile.y = y-Defines.TILE_HEIGHT/2;
                break;
            case Cancel:
                break;
        }
    }
}
