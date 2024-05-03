package com.pigdogbay.wordpig.presenter

import android.graphics.Point
import com.pigdogbay.wordpig.Defines
import com.pigdogbay.wordpig.model.Model
import com.pigdogbay.wordpig.model.ScreenState
import com.pigdogbay.wordpig.model.Tile

interface IGameOverView
class GameOverPresenter(val model: Model, val view: IGameOverView) {
    val tiles = ArrayList<Tile>()

    fun goButtonClicked(){
        model.screenStateObserver.value = ScreenState.Home
    }

    fun addTiles(newLetters: String, y: Int) {
        val n = newLetters.length
        //center word
        var x =
            (Defines.BOARD_WIDTH - n * Defines.TILE_WIDTH - (n - 1) * Defines.TILE_POOL_X_SPACING) / 2
        for (c in newLetters.toCharArray()) {
            tiles.add(Tile(c.code, x, y))
            x += Defines.TILE_POOL_X_SPACING + Defines.TILE_WIDTH
        }
    }

    fun getTileAtlasCoords(p: Point, t: Tile) {
        var i = t.letter - 'a'.code
        p.y = 0
        if (i >= 13) {
            p.y = Defines.TILE_HEIGHT
            i -= 13
        }
        p.x = i * Defines.TILE_WIDTH
    }
}