package com.pigdogbay.wordpig.model

import com.pigdogbay.wordpig.Defines

class Tray {
    private val buffer = StringBuilder()
    private val trayTiles = ArrayList<Tile>()

    fun setTilesIfInTray(tiles: List<Tile>) {
        trayTiles.clear()
        for (t in tiles) {
            if (t.y > Defines.TRAY_MIN_Y && t.y < Defines.TRAY_MAX_Y) {
                trayTiles.add(t)
            }
        }
    }

    fun sortTilesByPosition() {
        trayTiles.sortWith { lhs, rhs -> lhs.x - rhs.x }
    }

    val score: Int
        get() {
            var score = 0
            for (t in trayTiles) {
                val index = t.letter - 'a'.code
                score += Defines.SCORE_LETTER[index]
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
