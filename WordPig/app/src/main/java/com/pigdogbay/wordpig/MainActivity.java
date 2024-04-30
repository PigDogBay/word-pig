package com.pigdogbay.wordpig;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pigdogbay.lib.utils.ObservableProperty;
import com.pigdogbay.library.games.FrameBuffer;
import com.pigdogbay.library.games.GameView;
import com.pigdogbay.library.games.ObjectTouchHandler;
import com.pigdogbay.wordpig.model.Board;
import com.pigdogbay.wordpig.model.Screen;
import com.pigdogbay.wordpig.model.WordChecker;


public class MainActivity extends Activity implements GameView.IGame, ObservableProperty.PropertyChangedObserver<Screen.ScreenState> {

    private GameView gameView;
    private GameView.IGame currentScreen;
    private GameScreen gameScreen;
    private HomeScreen homeScreen;
    private GameOverScreen gameOverScreen;

    private FrameBuffer buffer;
    private ObjectTouchHandler touchHandler;
    private Screen screen;
    private Board board;
    private Assets assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Set up touch handler
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        touchHandler = new ObjectTouchHandler();
        float xScale = ((float)Defines.BOARD_WIDTH)/((float)size.x);
        float yScale = ((float)Defines.BOARD_HEIGHT)/((float)size.y);
        touchHandler.setXScale(xScale);
        touchHandler.setYScale(yScale);

        //Load game
        this.assets = new Assets();
        this.assets.load(this);
        createModel();
        createScreens();

        gameView = new GameView(this, this);
        gameView.setShowFPS(true);
        gameView.setOnTouchListener(touchHandler);
        setContentView(gameView);
        showHome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    private void createModel()
    {
        screen = new Screen();
        screen.screenStateObserver.addObserver(this);
    }

    private void createScreens()
    {
        buffer = new FrameBuffer(Defines.BOARD_WIDTH, Defines.BOARD_HEIGHT);
        gameScreen = new GameScreen();
        gameScreen.setTouchHandler(touchHandler);
        gameScreen.setBuffer(buffer);
        gameScreen.setAssets(this.assets);
        gameScreen.initialize();

        homeScreen = new HomeScreen();
        homeScreen.setTouchHandler(touchHandler);
        homeScreen.setBuffer(buffer);
        homeScreen.setScreen(screen);
        homeScreen.setAssets(this.assets);
        homeScreen.initialize();

        gameOverScreen = new GameOverScreen();
        gameOverScreen.setTouchHandler(touchHandler);
        gameOverScreen.setBuffer(buffer);
        gameOverScreen.setScreen(screen);
        gameOverScreen.setAssets(this.assets);
        gameOverScreen.initializie();
    }

    private void showGame()
    {
        board = new Board(this.screen);
        board.setTiles("streaming");
        board.wordChecker = new WordChecker(this.assets.wordList);
        gameScreen.setBoard(board);
        touchHandler.clearTouchables();
        gameScreen.registerTouchables();
        currentScreen = gameScreen;
    }

    private void showHome()
    {
        touchHandler.clearTouchables();
        homeScreen.registerTouchables();
        currentScreen = homeScreen;
    }
    private void showGameOver()
    {
        touchHandler.clearTouchables();
        gameOverScreen.registerTouchables();
        currentScreen = gameOverScreen;
    }

    //IGame Implementation
    @Override
    public void Update() {
        currentScreen.Update();
    }

    @Override
    public void Render(Canvas c) {
        currentScreen.Render(c);
    }

    //Game state changes
    @Override
    public void update(ObservableProperty<Screen.ScreenState> sender, Screen.ScreenState update) {
        switch (update){
            case Home:
                showHome();
                break;
            case Game:
                showGame();
                break;
            case GameOver:
                showGameOver();
                break;
        }

    }
}
