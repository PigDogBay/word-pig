package com.pigdogbay.wordpig.presenter

import android.graphics.Point
import com.pigdogbay.lib.patterns.PropertyChangedObserver
import com.pigdogbay.wordpig.Assets
import com.pigdogbay.wordpig.Defines
import com.pigdogbay.wordpig.R
import com.pigdogbay.wordpig.model.Boom
import com.pigdogbay.wordpig.model.GameEvents
import com.pigdogbay.wordpig.model.Model
import com.pigdogbay.wordpig.model.Tile


interface IGameView {
    fun playSound(id : Int, volume : Float)
}

class GamePresenter(val model : Model, val view : IGameView) : PropertyChangedObserver<GameEvents> {
    val boom = Boom()
    val board = model.board

    fun initialize(){
        board.gameEventObserver.addObserver(this)
    }

    fun goClicked(){
        board.go()
    }

    fun clearClicked(){
        board.clear()
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

    override fun update(sender: Any, update: GameEvents) {
        when (update) {
            GameEvents.WordOk -> {
                view.playSound(R.raw.coin, 0.1f)
                boom.addMessage("+" + board.pointsScored.toString() + "pts")
            }

            GameEvents.WordDoesNotExist -> {
                view.playSound(R.raw.laser, 0.2f)
                Assets.soundManager.play(R.raw.laser, 0.2f)
                boom.addMessage(Defines.SCORE_NOT_A_WORD.toString() + "pts")
            }

            GameEvents.WordAlreadyUsed -> {
                view.playSound(R.raw.laser, 0.2f)
                boom.addMessage("Used")
            }

            GameEvents.WordEmpty -> {
                view.playSound(R.raw.laser, 0.2f)
                boom.addMessage("eh?")
            }

            GameEvents.GetReady -> boom.addMessage("Get Ready!")
            GameEvents.Clear -> boom.addMessage("CLEAR")
            GameEvents.TimesUp -> boom.addMessage("Times Up!")
        }
    }
}