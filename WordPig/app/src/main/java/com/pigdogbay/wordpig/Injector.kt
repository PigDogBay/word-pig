package com.pigdogbay.wordpig

import android.graphics.Point
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.Screen

object Injector {
    val screen = Screen()
    val board = Board(screen)
    lateinit var gameScreen: GameScreen
    lateinit var homeScreen: HomeScreen
    lateinit var gameOverScreen: GameOverScreen
    lateinit var buffer: FrameBuffer
    lateinit var touchHandler: ObjectTouchHandler

    fun build(screenSize : Point) {
        touchHandler = ObjectTouchHandler()
        val xScale = Defines.BOARD_WIDTH.toFloat() / screenSize.x.toFloat()
        val yScale = Defines.BOARD_HEIGHT.toFloat() / screenSize.y.toFloat()
        touchHandler.xScale = xScale
        touchHandler.yScale = yScale
        buffer = FrameBuffer(Defines.BOARD_WIDTH, Defines.BOARD_HEIGHT)
    }

    fun createScreens() {
        gameScreen = GameScreen()
        homeScreen = HomeScreen()
        gameOverScreen = GameOverScreen()
    }

}