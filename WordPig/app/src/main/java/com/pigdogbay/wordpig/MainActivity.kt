package com.pigdogbay.wordpig

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.pigdogbay.lib.games.FrameBuffer
import com.pigdogbay.lib.games.GameView
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.games.ObjectTouchHandler
import com.pigdogbay.lib.utils.ObservableProperty
import com.pigdogbay.wordpig.model.Board
import com.pigdogbay.wordpig.model.Screen
import com.pigdogbay.wordpig.model.Screen.ScreenState
import com.pigdogbay.wordpig.model.WordChecker

class MainActivity : Activity(), Game, ObservableProperty.PropertyChangedObserver<ScreenState> {
    private lateinit var gameView: GameView
    private lateinit var currentScreen: Game
    private lateinit var gameScreen: GameScreen
    private lateinit var homeScreen: HomeScreen
    private lateinit var gameOverScreen: GameOverScreen
    private lateinit var buffer: FrameBuffer
    private lateinit var touchHandler: ObjectTouchHandler
    private lateinit var screen: Screen
    private lateinit var board: Board
    private lateinit var assets: Assets

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            fullScreen()
        }

        //Set up touch handler
        val size = Point()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            //On the Samsung this version gives 2132x1080
            this.windowManager.defaultDisplay.getSize(size)
        } else {
            //On the Samsung this version gives 2340x1080
            val bounds = this.windowManager.currentWindowMetrics.bounds
            size.x = bounds.width()
            size.y = bounds.height()
        }
        touchHandler = ObjectTouchHandler()
        val xScale = Defines.BOARD_WIDTH.toFloat() / size.x.toFloat()
        val yScale = Defines.BOARD_HEIGHT.toFloat() / size.y.toFloat()
        touchHandler.xScale = xScale
        touchHandler.yScale = yScale

        //Load game
        assets = Assets
        assets.load(this)
        createModel()
        createScreens()
        gameView = GameView(this, this)
        gameView.setShowFPS(true)
        gameView.setOnTouchListener(touchHandler)
        setContentView(gameView)
        showHome()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //Full screen for Android R+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @Suppress("DEPRECATION")
    private fun fullScreen() {
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    private fun createModel() {
        screen = Screen()
        screen.screenStateObserver.addObserver(this)
    }

    private fun createScreens() {
        buffer = FrameBuffer(Defines.BOARD_WIDTH, Defines.BOARD_HEIGHT)
        gameScreen = GameScreen()
        gameScreen.setTouchHandler(touchHandler)
        gameScreen.setBuffer(buffer)
        gameScreen.setAssets(assets)
        gameScreen.initialize()
        homeScreen = HomeScreen()
        homeScreen.setTouchHandler(touchHandler)
        homeScreen.setBuffer(buffer)
        homeScreen.setScreen(screen)
        homeScreen.setAssets(assets)
        homeScreen.initialize()
        gameOverScreen = GameOverScreen()
        gameOverScreen.setTouchHandler(touchHandler)
        gameOverScreen.setBuffer(buffer)
        gameOverScreen.setScreen(screen)
        gameOverScreen.setAssets(assets)
        gameOverScreen.initializie()
    }

    private fun showGame() {
        board = Board(screen)
        board.setTiles("streaming")
        board.wordChecker = WordChecker(assets.wordList)
        gameScreen.setBoard(board)
        touchHandler.clearTouchables()
        gameScreen.registerTouchables()
        currentScreen = gameScreen
    }

    private fun showHome() {
        touchHandler.clearTouchables()
        homeScreen.registerTouchables()
        currentScreen = homeScreen
    }

    private fun showGameOver() {
        touchHandler.clearTouchables()
        gameOverScreen.registerTouchables()
        currentScreen = gameOverScreen
    }

    //Game Implementation
    override fun update() {
        currentScreen.update()
    }

    override fun render(c: Canvas?) {
        currentScreen.render(c)
    }

    //Game state changes
    override fun update(sender: ObservableProperty<ScreenState?>?, update: ScreenState?) {
        when (update!!) {
            ScreenState.Home -> showHome()
            ScreenState.Game -> showGame()
            ScreenState.GameOver -> showGameOver()
        }
    }
}
