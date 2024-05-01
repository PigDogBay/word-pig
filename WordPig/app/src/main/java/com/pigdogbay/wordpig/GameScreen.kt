package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.Boom
import com.pigdogbay.wordpig.model.GameEvent.GameEventListener
import com.pigdogbay.wordpig.model.GameEvents
import com.pigdogbay.wordpig.model.Tile
import com.pigdogbay.wordpig.model.TouchTile

/**
 * Created by Mark on 01/04/2015.
 */
class GameScreen : Game, BitmapButton.OnClickListener, GameEventListener {
    private var _Buffer: FrameBuffer? = null
    private var _TouchHandler: ObjectTouchHandler? = null
    private var _Board: Board? = null
    private var _GoButton: BitmapButton? = null
    private var _ClearButton: BitmapButton? = null
    private var _TextPaint: Paint? = null
    private var _TimerOuterPaint: Paint? = null
    private var _TimerInnerPaint: Paint? = null
    private var _BoomPaint: Paint? = null
    private var _Assets: Assets? = null
    private var _Boom: Boom? = null
    fun setAssets(assets: Assets?) {
        _Assets = assets
    }

    fun setBoard(board: Board?) {
        _Board = board
        _Board!!.addEventListener(this)
    }

    fun setTouchHandler(touch: ObjectTouchHandler?) {
        _TouchHandler = touch
    }

    fun setBuffer(buffer: FrameBuffer?) {
        _Buffer = buffer
    }

    fun initialize() {
        _TextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        _TextPaint!!.color = Color.WHITE
        _TextPaint!!.textSize = 36f
        _TimerInnerPaint = Paint()
        _TimerInnerPaint!!.color = Color.WHITE
        _TimerOuterPaint = Paint()
        _TimerOuterPaint!!.color = Color.RED
        _BoomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        _BoomPaint!!.color = Color.RED
        _BoomPaint!!.textSize = 144f
        _GoButton = BitmapButton(_Assets!!.GoButton!!, _Assets!!.GoButtonPressed!!, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)
        _GoButton!!.setOnClickListener(this)
        _ClearButton = BitmapButton(_Assets!!.ClearButton!!,_Assets!!.ClearButtonPressed!!, Defines.CLEAR_BUTTON_X, Defines.CLEAR_BUTTON_Y)
        _ClearButton!!.setOnClickListener(this)
        _Boom = Boom()
    }

    fun registerTouchables() {
        _TouchHandler!!.add(_GoButton!!)
        _TouchHandler!!.add(_ClearButton!!)
        for (t in _Board!!.tiles) {
            val touchTile = TouchTile(t)
            _TouchHandler!!.add(touchTile)
        }
    }

    //IGame
    override fun update() {
        _Board!!.update()
        _Boom!!.update()
    }

    override fun render(c: Canvas?) {
        _Buffer!!.clear(0)
        val buffCanvas = _Buffer!!.canvas
        _Buffer!!.draw(_Assets!!.WordBench, Defines.WORD_BENCH_X, Defines.WORD_BENCH_Y)
        drawButtons()
        drawScore(buffCanvas)
        drawTimer(buffCanvas)
        drawTiles()
        drawBoom(buffCanvas)
        _Buffer!!.scaleToFit(c!!)
    }

    private fun drawBoom(buffCanvas: Canvas) {
        if (_Boom!!.isMessageAvailable) {
            val y = Defines.BOOM_Y - (_Boom!!.count * 10).toFloat()
            buffCanvas.drawText(_Boom!!.latestMessage, Defines.BOOM_X, y, _BoomPaint!!)
        }
    }

    private fun drawTiles() {
        val point = Point()
        for (t in _Board!!.tiles) {
            getTileAtlasCoords(point, t)
            _Buffer!!.draw(
                _Assets!!.TilesAtlas,
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
        _GoButton!!.draw(_Buffer!!)
        _ClearButton!!.draw(_Buffer!!)
    }

    private fun drawScore(buffCanvas: Canvas) {
        buffCanvas.drawText(
            "Level: " + _Board!!.level.toString(),
            Defines.LEVEL_X,
            Defines.LEVEL_Y,
            _TextPaint!!
        )
        buffCanvas.drawText(
            "Score: " + _Board!!.score.toString(),
            Defines.SCORE_X,
            Defines.SCORE_Y,
            _TextPaint!!
        )
    }

    private fun drawTimer(buffCanvas: Canvas) {
        buffCanvas.drawRect(Defines.TIMER_OUTER_RECT, _TimerOuterPaint!!)
        var len = Defines.TIMER_INNER_RIGHT - Defines.TIMER_INNER_LEFT
        len = Defines.TIMER_INNER_LEFT + len * _Board!!.time / Defines.TIMER_MAX_VAL
        buffCanvas.drawRect(
            Defines.TIMER_INNER_LEFT,
            Defines.TIMER_INNER_TOP,
            len,
            Defines.TIMER_INNER_BOTTOM,
            _TimerInnerPaint!!
        )
    }

    private fun getTileAtlasCoords(p: Point, t: Tile) {
        var i = t.letter - 'a'.code
        p.y = 0
        if (i >= 13) {
            p.y = Defines.TILE_HEIGHT
            i = i - 13
        }
        p.x = i * Defines.TILE_WIDTH
    }

    override fun onClick(sender: Any?) {
        if (sender === _GoButton) {
            _Board!!.go()
        } else if (sender === _ClearButton) {
            _Board!!.clear()
        }
    }

    override fun onGameEvent(sender: Any, id: Int) {
        when (id) {
            GameEvents.GAME_EVENT_WORD_OK -> {
                _Assets!!.SoundManager!!.play(R.raw.coin, 0.1f)
                _Boom!!.addMessage("+" + _Board!!.pointsScored.toString() + "pts")
            }

            GameEvents.GAME_EVENT_WORD_DOES_NOT_EXIST -> {
                _Assets!!.SoundManager!!.play(R.raw.laser, 0.2f)
                _Boom!!.addMessage(Defines.SCORE_NOT_A_WORD.toString() + "pts")
            }

            GameEvents.GAME_EVENT_WORD_ALREADY_USED -> {
                _Assets!!.SoundManager!!.play(R.raw.laser, 0.2f)
                _Boom!!.addMessage("Used")
            }

            GameEvents.GAME_EVENT_WORD_EMPTY -> {
                _Assets!!.SoundManager!!.play(R.raw.laser, 0.2f)
                _Boom!!.addMessage("eh?")
            }

            GameEvents.GAME_EVENT_GET_READY -> _Boom!!.addMessage("Get Ready!")
            GameEvents.GAME_EVENT_CLEAR -> _Boom!!.addMessage("CLEAR")
            GameEvents.GAME_EVENT_TIMES_UP -> _Boom!!.addMessage("Times Up!")
        }
    }
}
