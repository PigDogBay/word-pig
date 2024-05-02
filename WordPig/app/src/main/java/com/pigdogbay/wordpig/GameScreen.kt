package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.patterns.PropertyChangedObserver
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.Boom
import com.pigdogbay.wordpig.model.GameEvents
import com.pigdogbay.wordpig.model.Tile
import com.pigdogbay.wordpig.model.TouchTile

/**
 * Created by Mark on 01/04/2015.
 */
class GameScreen : Game, BitmapButton.OnClickListener, PropertyChangedObserver<GameEvents> {
    private val goButton: BitmapButton
    private val clearButton: BitmapButton
    private val textPaint: Paint
    private val timerOuterPaint: Paint
    private val timerInnerPaint: Paint
    private val boomPaint: Paint
    private val boom: Boom
    private val board : Board
        get() = Injector.board
    private val buffer : FrameBuffer
        get() = Injector.buffer

    init {
        board.gameEventObserver.addObserver(this)
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.WHITE
        textPaint.textSize = 36f
        timerInnerPaint = Paint()
        timerInnerPaint.color = Color.WHITE
        timerOuterPaint = Paint()
        timerOuterPaint.color = Color.RED
        boomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        boomPaint.color = Color.RED
        boomPaint.textSize = 144f
        goButton = BitmapButton(Assets.goButton, Assets.goButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)
        goButton.setOnClickListener(this)
        clearButton = BitmapButton(Assets.clearButton,Assets.clearButtonPressed, Defines.CLEAR_BUTTON_X, Defines.CLEAR_BUTTON_Y)
        clearButton.setOnClickListener(this)
        boom = Boom()
    }

    fun registerTouchables() {
        val touchHandler = Injector.touchHandler
        touchHandler.add(goButton)
        touchHandler.add(clearButton)
        for (t in Injector.board.tiles) {
            val touchTile = TouchTile(t)
            touchHandler.add(touchTile)
        }
    }

    //Game
    override fun update() {
        Injector.board.update()
        boom.update()
    }

    override fun render(c: Canvas?) {
        buffer.clear(0)
        val buffCanvas = buffer.canvas
        buffer.draw(Assets.wordBench, Defines.WORD_BENCH_X, Defines.WORD_BENCH_Y)
        drawButtons()
        drawScore(buffCanvas)
        drawTimer(buffCanvas)
        drawTiles()
        drawBoom(buffCanvas)
        buffer.scaleToFit(c!!)
    }

    private fun drawBoom(buffCanvas: Canvas) {
        if (boom.isMessageAvailable) {
            val y = Defines.BOOM_Y - (boom.count * 10).toFloat()
            buffCanvas.drawText(boom.latestMessage, Defines.BOOM_X, y, boomPaint)
        }
    }

    private fun drawTiles() {
        val point = Point()
        for (t in board.tiles) {
            getTileAtlasCoords(point, t)
            Injector.buffer.draw(
                Assets.tilesAtlas,
                t.x,
                t.y,
                point.x,
                point.y,
                Defines.TILE_WIDTH,
                Defines.TILE_HEIGHT
            )
        }
    }

    private fun drawButtons() {
        goButton.draw(buffer)
        clearButton.draw(buffer)
    }

    private fun drawScore(buffCanvas: Canvas) {
        buffCanvas.drawText(
            "Level: " + board.level.toString(),
            Defines.LEVEL_X,
            Defines.LEVEL_Y,
            textPaint
        )
        buffCanvas.drawText(
            "Score: " + board.score.toString(),
            Defines.SCORE_X,
            Defines.SCORE_Y,
            textPaint
        )
    }

    private fun drawTimer(buffCanvas: Canvas) {
        buffCanvas.drawRect(Defines.TIMER_OUTER_RECT, timerOuterPaint)
        var len = Defines.TIMER_INNER_RIGHT - Defines.TIMER_INNER_LEFT
        len = Defines.TIMER_INNER_LEFT + len * board.time / Defines.TIMER_MAX_VAL
        buffCanvas.drawRect(
            Defines.TIMER_INNER_LEFT,
            Defines.TIMER_INNER_TOP,
            len,
            Defines.TIMER_INNER_BOTTOM,
            timerInnerPaint
        )
    }

    private fun getTileAtlasCoords(p: Point, t: Tile) {
        var i = t.letter - 'a'.code
        p.y = 0
        if (i >= 13) {
            p.y = Defines.TILE_HEIGHT
            i -= 13
        }
        p.x = i * Defines.TILE_WIDTH
    }

    override fun onClick(sender: Any?) {
        if (sender === goButton) {
            Injector.board.go()
        } else if (sender === clearButton) {
            board.clear()
        }
    }

    override fun update(sender: Any, update: GameEvents) {
        when (update) {
            GameEvents.WordOk -> {
                Assets.soundManager.play(R.raw.coin, 0.1f)
                boom.addMessage("+" + board.pointsScored.toString() + "pts")
            }

            GameEvents.WordDoesNotExist -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                boom.addMessage(Defines.SCORE_NOT_A_WORD.toString() + "pts")
            }

            GameEvents.WordAlreadyUsed -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                boom.addMessage("Used")
            }

            GameEvents.WordEmpty -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                boom.addMessage("eh?")
            }

            GameEvents.GetReady -> boom.addMessage("Get Ready!")
            GameEvents.Clear -> boom.addMessage("CLEAR")
            GameEvents.TimesUp -> boom.addMessage("Times Up!")
        }
    }

}
