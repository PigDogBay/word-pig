package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.games.ObjectTouchHandler.Touchable
import com.pigdogbay.lib.games.ObjectTouchHandler.TouchState
import com.pigdogbay.wordpig.Defines

/**
 * Created by Mark on 01/04/2015.
 */
class TouchTile(var tile: Tile) : Touchable {
    override fun contains(x: Int, y: Int): Boolean {
        var tx = tile.x
        var ty = tile.y
        if (x > tx && y > ty) {
            tx = tx + Defines.TILE_WIDTH
            if (x < tx) {
                ty = ty + Defines.TILE_HEIGHT
                return y < ty
            }
        }
        return false
    }

    override fun setTouchState(state: TouchState, x: Int, y: Int) {
        when (state) {
            TouchState.None -> {}
            TouchState.Down, TouchState.DragInside, TouchState.DragOutside, TouchState.UpInside, TouchState.UpOutside -> {
                tile.x = x - Defines.TILE_WIDTH / 2
                tile.y = y - Defines.TILE_HEIGHT / 2
            }

            TouchState.Cancel -> {}
        }
    }
}
