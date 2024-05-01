package com.pigdogbay.wordpig.model

import com.pigdogbay.lib.games.Timer
import com.pigdogbay.wordpig.Defines
import com.pigdogbay.wordpig.model.GameEvent.IGameEventListener
import com.pigdogbay.wordpig.model.Screen.ScreenState

/**
 * Created by Mark on 01/04/2015.
 */
class Board(var screen: Screen) {
    enum class GameState {
        Initialize,
        FirstWord,
        InPlay,
        TimesUp,
        Exit
    }

    val tiles = ArrayList<Tile>()
    var score = 0
    var level = 42
    var time = Defines.TIMER_MAX_VAL
    var wordChecker: WordChecker? = null
    var pointsScored = 0
    private val tray = Tray()
    private val usedWords = ArrayList<String>()
    private var gameState = GameState.Initialize
    private val timer = Timer(0)
    private val gameEvent = GameEvent()

    fun addEventListener(listener: IGameEventListener) {
        gameEvent.addListener(listener)
    }

    fun update() {
        when (gameState) {
            GameState.Initialize -> {
                gameState = GameState.FirstWord
                gameEvent.fire(this, GameEvents.GAME_EVENT_GET_READY)
            }

            GameState.FirstWord -> if (usedWords.size > 0) {
                gameState = GameState.InPlay
            }

            GameState.InPlay -> {
                time--
                if (time == 0f) {
                    timer.reset(Defines.TIMES_UP_DURATION)
                    gameState = GameState.TimesUp
                    gameEvent.fire(this, GameEvents.GAME_EVENT_TIMES_UP)
                }
            }

            GameState.TimesUp -> if (timer.hasTimeElapsed()) {
                gameState = GameState.Exit
                gameOver()
            }

            GameState.Exit -> {}
        }
    }

    fun setTiles(newLetters: String) {
        tiles.clear()
        var x = Defines.TILE_POOL_X
        for (c in newLetters.toCharArray()) {
            tiles.add(Tile(c.code, x, Defines.TILE_POOL_Y))
            x += Defines.TILE_POOL_X_SPACING + Defines.TILE_WIDTH
        }
    }

    fun go() {
        if (GameState.InPlay != gameState && GameState.FirstWord != gameState) {
            //ignore
            return
        }
        val word = word
        if ("" == word) {
            gameEvent.fire(this, GameEvents.GAME_EVENT_WORD_EMPTY)
            return
        }
        if (usedWords.contains(word)) {
            gameEvent.fire(this, GameEvents.GAME_EVENT_WORD_ALREADY_USED)
            return
        }
        if (wordChecker!!.isWord(word)) {
            pointsScored = tray.score
            score += pointsScored
            gameEvent.fire(this, GameEvents.GAME_EVENT_WORD_OK)
        } else {
            score += Defines.SCORE_NOT_A_WORD
            gameEvent.fire(this, GameEvents.GAME_EVENT_WORD_DOES_NOT_EXIST)
        }
        usedWords.add(word)
    }

    fun clear() {
        var x = Defines.TILE_POOL_X
        for (t in tiles) {
            t.x = x
            t.y = Defines.TILE_POOL_Y
            x += Defines.TILE_POOL_X_SPACING + Defines.TILE_WIDTH
        }
        gameEvent.fire(this, GameEvents.GAME_EVENT_CLEAR)
    }

    private val word: String
        get() {
            tray.setTilesIfInTray(tiles)
            tray.sortTilesByPosition()
            return tray.toString()
        }

    private fun gameOver() {
        screen.screenStateObserver.setValue(ScreenState.GameOver)
    }
}
