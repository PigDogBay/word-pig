package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.IGame
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.wordpig.model.Screen
import com.pigdogbay.wordpig.model.Tile

/**
 * Created by Mark on 07/04/2015.
 */
class GameOverScreen : IGame, BitmapButton.OnClickListener {
    private var screen: Screen? = null
    private var buffer: FrameBuffer? = null
    private var touchHandler: ObjectTouchHandler? = null
    private var tiles: MutableList<Tile>? = null
    private var _GoButton: BitmapButton? = null
    private var _Assets: Assets? = null
    fun setAssets(assets: Assets?) {
        _Assets = assets
    }

    fun setTouchHandler(touch: ObjectTouchHandler?) {
        touchHandler = touch
    }

    fun setBuffer(buffer: FrameBuffer?) {
        this.buffer = buffer
    }

    fun setScreen(screen: Screen?) {
        this.screen = screen
    }

    fun initializie() {
        _GoButton = BitmapButton()
        _GoButton!!.setBitmaps(
            _Assets!!.GoButton,
            _Assets!!.GoButtonPressed,
            Defines.GO_BUTTON_X,
            Defines.GO_BUTTON_Y
        )
        _GoButton!!.setOnClickListener(this)
        tiles = ArrayList()
        addTiles("game", Defines.HOME_TILES_LINE1_Y)
        addTiles("over", Defines.HOME_TILES_LINE2_Y)
    }

    override fun Update() {}
    override fun Render(c: Canvas?) {
        buffer!!.clear(Color.BLUE)
        drawTiles()
        _GoButton!!.draw(buffer!!)
        buffer!!.scaleToFit(c!!)
    }

    fun registerTouchables() {
        touchHandler!!.add(_GoButton!!)
    }

    override fun onClick(sender: Any?) {
        screen!!.screenStateObserver.setValue(Screen.ScreenState.Home)
    }

    private fun drawTiles() {
        val point = Point()
        for (t in tiles!!) {
            getTileAtlasCoords(point, t)
            buffer!!.draw(
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

    private fun addTiles(newLetters: String, y: Int) {
        val n = newLetters.length
        //center word
        var x =
            (Defines.BOARD_WIDTH - n * Defines.TILE_WIDTH - (n - 1) * Defines.TILE_POOL_X_SPACING) / 2
        for (c in newLetters.toCharArray()) {
            tiles!!.add(Tile(c.code, x, y))
            x = x + Defines.TILE_POOL_X_SPACING + Defines.TILE_WIDTH
        }
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
}
