package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.Boom
import com.pigdogbay.wordpig.model.GameEventListener
import com.pigdogbay.wordpig.model.GameEvents
import com.pigdogbay.wordpig.model.Tile
import com.pigdogbay.wordpig.model.TouchTile

/**
 * Created by Mark on 01/04/2015.
 */
class GameScreen : Game, BitmapButton.OnClickListener, GameEventListener {
    private val _GoButton: BitmapButton
    private val _ClearButton: BitmapButton
    private val _TextPaint: Paint
    private val _TimerOuterPaint: Paint
    private val _TimerInnerPaint: Paint
    private val _BoomPaint: Paint
    private val _Boom: Boom
    private val board : Board
        get() = Injector.board
    private val buffer : FrameBuffer
        get() = Injector.buffer

    init {
        board.addEventListener(this)
        _TextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        _TextPaint.color = Color.WHITE
        _TextPaint.textSize = 36f
        _TimerInnerPaint = Paint()
        _TimerInnerPaint.color = Color.WHITE
        _TimerOuterPaint = Paint()
        _TimerOuterPaint.color = Color.RED
        _BoomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        _BoomPaint.color = Color.RED
        _BoomPaint.textSize = 144f
        _GoButton = BitmapButton(Assets.goButton, Assets.goButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)
        _GoButton.setOnClickListener(this)
        _ClearButton = BitmapButton(Assets.clearButton,Assets.clearButtonPressed, Defines.CLEAR_BUTTON_X, Defines.CLEAR_BUTTON_Y)
        _ClearButton.setOnClickListener(this)
        _Boom = Boom()
    }

    fun registerTouchables() {
        val touchHandler = Injector.touchHandler
        touchHandler.add(_GoButton)
        touchHandler.add(_ClearButton)
        for (t in Injector.board.tiles) {
            val touchTile = TouchTile(t)
            touchHandler.add(touchTile)
        }
    }

    //Game
    override fun update() {
        Injector.board.update()
        _Boom.update()
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
        if (_Boom.isMessageAvailable) {
            val y = Defines.BOOM_Y - (_Boom.count * 10).toFloat()
            buffCanvas.drawText(_Boom.latestMessage, Defines.BOOM_X, y, _BoomPaint)
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
        _GoButton.draw(buffer)
        _ClearButton.draw(buffer)
    }

    private fun drawScore(buffCanvas: Canvas) {
        buffCanvas.drawText(
            "Level: " + board.level.toString(),
            Defines.LEVEL_X,
            Defines.LEVEL_Y,
            _TextPaint
        )
        buffCanvas.drawText(
            "Score: " + board.score.toString(),
            Defines.SCORE_X,
            Defines.SCORE_Y,
            _TextPaint
        )
    }

    private fun drawTimer(buffCanvas: Canvas) {
        buffCanvas.drawRect(Defines.TIMER_OUTER_RECT, _TimerOuterPaint)
        var len = Defines.TIMER_INNER_RIGHT - Defines.TIMER_INNER_LEFT
        len = Defines.TIMER_INNER_LEFT + len * board.time / Defines.TIMER_MAX_VAL
        buffCanvas.drawRect(
            Defines.TIMER_INNER_LEFT,
            Defines.TIMER_INNER_TOP,
            len,
            Defines.TIMER_INNER_BOTTOM,
            _TimerInnerPaint
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
        if (sender === _GoButton) {
            Injector.board.go()
        } else if (sender === _ClearButton) {
            board.clear()
        }
    }

    override fun onGameEvent(sender: Any, id: Int) {
        when (id) {
            GameEvents.GAME_EVENT_WORD_OK -> {
                Assets.soundManager.play(R.raw.coin, 0.1f)
                _Boom.addMessage("+" + board.pointsScored.toString() + "pts")
            }

            GameEvents.GAME_EVENT_WORD_DOES_NOT_EXIST -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                _Boom.addMessage(Defines.SCORE_NOT_A_WORD.toString() + "pts")
            }

            GameEvents.GAME_EVENT_WORD_ALREADY_USED -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                _Boom.addMessage("Used")
            }

            GameEvents.GAME_EVENT_WORD_EMPTY -> {
                Assets.soundManager.play(R.raw.laser, 0.2f)
                _Boom.addMessage("eh?")
            }

            GameEvents.GAME_EVENT_GET_READY -> _Boom.addMessage("Get Ready!")
            GameEvents.GAME_EVENT_CLEAR -> _Boom.addMessage("CLEAR")
            GameEvents.GAME_EVENT_TIMES_UP -> _Boom.addMessage("Times Up!")
        }
    }
}
