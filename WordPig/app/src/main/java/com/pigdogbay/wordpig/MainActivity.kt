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
import com.pigdogbay.lib.games.GameView
import com.pigdogbay.lib.games.GameView.Game
import com.pigdogbay.lib.utils.ObservableProperty
import com.pigdogbay.wordpig.model.ScreenState
import com.pigdogbay.wordpig.model.WordChecker

class MainActivity : Activity(), Game, ObservableProperty.PropertyChangedObserver<ScreenState> {
    private lateinit var gameView: GameView
    private lateinit var currentScreen: Game
    private val gameScreen: GameScreen
        get() = Injector.gameScreen
    private val homeScreen: HomeScreen
        get() = Injector.homeScreen
    private val gameOverScreen: GameOverScreen
        get() = Injector.gameOverScreen

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
        Assets.load(this.applicationContext)
        Injector.build(size)
        Injector.createScreens()
        gameView = GameView(this, this)
        gameView.setShowFPS(true)
        gameView.setOnTouchListener(Injector.touchHandler)
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
        Injector.model.screenStateObserver.addObserver(this)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
        Injector.model.screenStateObserver.removeObserver(this)
    }

    private fun showGame() {
        val board = Injector.board
        board.setTiles("streaming")
        board.wordChecker = WordChecker(Assets.wordList)
        Injector.touchHandler.clearTouchables()
        gameScreen.registerTouchables()
        currentScreen = gameScreen
    }

    private fun showHome() {
        Injector.touchHandler.clearTouchables()
        homeScreen.registerTouchables()
        currentScreen = homeScreen
    }

    private fun showGameOver() {
        Injector.touchHandler.clearTouchables()
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
