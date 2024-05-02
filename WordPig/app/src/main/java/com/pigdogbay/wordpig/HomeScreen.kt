package com.pigdogbay.wordpig

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import com.pigdogbay.lib.games.BitmapButton
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.wordpig.model.Screen
import com.pigdogbay.wordpig.model.Tile

/**
 * Created by Mark on 07/04/2015.
 */
class HomeScreen : Game, BitmapButton.OnClickListener {
    private val screen: Screen
        get() = Injector.screen
    private val touchHandler: ObjectTouchHandler
        get() = Injector.touchHandler
    private val tiles =  ArrayList<Tile>()
    private val goButton: BitmapButton =
        BitmapButton(Assets.goButton, Assets.goButtonPressed,Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y)
    private val buffer : FrameBuffer
        get() = Injector.buffer


    init {
        goButton.setOnClickListener(this)
        addTiles("word", Defines.HOME_TILES_LINE1_Y)
        addTiles("pig", Defines.HOME_TILES_LINE2_Y)
    }

    override fun update() {
        jiggle()
    }

    override fun render(c: Canvas?) {
        buffer.clear(Color.YELLOW)
        drawTiles()
        goButton.draw(buffer)
        if (c!=null) {
            buffer.scaleToFit(c)
        }
    }

    fun registerTouchables() {
        touchHandler.add(goButton)
    }

    override fun onClick(sender: Any?) {
        screen.screenStateObserver.setValue(Screen.ScreenState.Game)
    }

    private fun drawTiles() {
        val point = Point()
        for (t in tiles) {
            getTileAtlasCoords(point, t)
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

    private fun addTiles(newLetters: String, y: Int) {
        val n = newLetters.length
        //center word
        var x =
            (Defines.BOARD_WIDTH - n * Defines.TILE_WIDTH - (n - 1) * Defines.TILE_POOL_X_SPACING) / 2
        for (c in newLetters.toCharArray()) {
            tiles.add(Tile(c.code, x, y))
            x += Defines.TILE_POOL_X_SPACING + Defines.TILE_WIDTH
        }
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

    private var jiggleIndex = -1
    private var jiggleCount = 0
    private fun jiggle() {
        jiggleCount++
        if (jiggleCount < 5) {
            jiggleCount++
            return
        }
        jiggleCount = 0
        var offset = jiggleIndex * 5
        jiggleIndex *= -1
        for (t in tiles) {
            t.x += offset
            t.y += offset
            offset *= -1
        }
    }
}
