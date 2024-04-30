package com.pigdogbay.wordpig.model

import com.pigdogbay.wordpig.Defines
import java.util.Collections

/**
 * Created by Mark on 07/04/2015.
 */
class Tray {
    private val buffer: StringBuilder
    private val trayTiles: MutableList<Tile>

    init {
        trayTiles = ArrayList()
        buffer = StringBuilder()
    }

    fun setTilesIfInTray(tiles: List<Tile>) {
        trayTiles.clear()
        for (t in tiles) {
            if (t.y > Defines.TRAY_MIN_Y && t.y < Defines.TRAY_MAX_Y) {
                trayTiles.add(t)
            }
        }
    }

    fun sortTilesByPosition() {
        Collections.sort(trayTiles) { lhs, rhs -> lhs.x - rhs.x }
    }

    val score: Int
        get() {
            var score = 0
            for (t in trayTiles) {
                val index = t.letter - 'a'.code
                score = score + Defines.SCORE_LETTER[index]
            }
            return score
        }

    override fun toString(): String {
        buffer.setLength(0)
        for (t in trayTiles) {
            buffer.append(t.letter.toChar())
        }
        return buffer.toString()
    }
}
