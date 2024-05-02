package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.wordpig.presenter.GameOverPresenter
import com.pigdogbay.wordpig.presenter.IGameOverView

class GameOverScreen : Game, BitmapButton.OnClickListener, IGameOverView {
    val presenter = GameOverPresenter(Injector.model,this)

    private val touchHandler: ObjectTouchHandler
        get() = Injector.touchHandler
    private val buffer : FrameBuffer
        get() = Injector.buffer

    private val goButton: BitmapButton = BitmapButton(Assets.goButton,Assets.goButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)

    init{
        goButton.setOnClickListener(this)
        presenter.addTiles("game", Defines.HOME_TILES_LINE1_Y)
        presenter.addTiles("over", Defines.HOME_TILES_LINE2_Y)
    }

    override fun update() {}
    override fun render(c: Canvas?) {
        buffer.clear(Color.BLUE)
        drawTiles()
        goButton.draw(buffer)
        buffer.scaleToFit(c!!)
    }

    fun registerTouchables() {
        touchHandler.add(goButton)
    }

    override fun onClick(sender: Any?) {
        presenter.goButtonClicked()
    }

    private fun drawTiles() {
        val point = Point()
        for (t in presenter.tiles) {
            presenter.getTileAtlasCoords(point, t)
            buffer.draw(
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
}
