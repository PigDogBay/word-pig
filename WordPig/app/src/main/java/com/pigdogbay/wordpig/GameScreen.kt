package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.TouchTile
import com.pigdogbay.wordpig.presenter.GamePresenter
import com.pigdogbay.wordpig.presenter.IGameView

/**
 * Created by Mark on 01/04/2015.
 */
class GameScreen : Game, BitmapButton.OnClickListener, IGameView {
    val presenter = GamePresenter(Injector.model,this)

    private val goButton: BitmapButton
    private val clearButton: BitmapButton
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val timerOuterPaint: Paint
    private val timerInnerPaint: Paint
    private val boomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val board : Board
        get() = Injector.model.board
    private val buffer : FrameBuffer
        get() = Injector.buffer

    init {
        textPaint.color = Color.WHITE
        textPaint.textSize = 36f
        timerInnerPaint = Paint()
        timerInnerPaint.color = Color.WHITE
        timerOuterPaint = Paint()
        timerOuterPaint.color = Color.RED
        boomPaint.color = Color.RED
        boomPaint.textSize = 144f
        goButton = BitmapButton(Assets.goButton, Assets.goButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)
        goButton.setOnClickListener(this)
        clearButton = BitmapButton(Assets.clearButton,Assets.clearButtonPressed, Defines.CLEAR_BUTTON_X, Defines.CLEAR_BUTTON_Y)
        clearButton.setOnClickListener(this)
        presenter.initialize()
    }

    fun registerTouchables() {
        val touchHandler = Injector.touchHandler
        touchHandler.add(goButton)
        touchHandler.add(clearButton)
        for (t in board.tiles) {
            val touchTile = TouchTile(t)
            touchHandler.add(touchTile)
        }
    }

    //Game
    override fun update() {
        board.update()
        presenter.boom.update()
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
        if (presenter.boom.isMessageAvailable) {
            val y = Defines.BOOM_Y - (presenter.boom.count * 10).toFloat()
            buffCanvas.drawText(presenter.boom.latestMessage, Defines.BOOM_X, y, boomPaint)
        }
    }

    private fun drawTiles() {
        val point = Point()
        for (t in board.tiles) {
            presenter.getTileAtlasCoords(point, t)
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

    override fun onClick(sender: Any?) {
        if (sender === goButton) {
            presenter.goClicked()
        } else if (sender === clearButton) {
            presenter.clearClicked()
        }
    }

    override fun playSound(id: Int, volume: Float) {
        Assets.soundManager.play(id, volume)
    }
}
