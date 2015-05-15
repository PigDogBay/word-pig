package com.pigdogbay.wordpig;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pigdogbay.library.games.AssetsReader;
import com.pigdogbay.library.games.FrameBuffer;
import com.pigdogbay.library.games.GameView;
import com.pigdogbay.library.games.ObjectTouchHandler;
import com.pigdogbay.library.games.SoundManager;
import com.pigdogbay.library.utils.LineReader;
import com.pigdogbay.library.utils.ObservableProperty;
import com.pigdogbay.wordpig.model.Board;
import com.pigdogbay.wordpig.model.Screen;
import com.pigdogbay.wordpig.model.WordChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;


public class MainActivity extends Activity implements GameView.IGame, ObservableProperty.PropertyChangedObserver<Screen.ScreenState> {

    private GameView gameView;
    private GameView.IGame currentScreen;
    private GameScreen gameScreen;
    private HomeScreen homeScreen;
    private GameOverScreen gameOverScreen;
    private SoundManager _SoundManager;

    private FrameBuffer buffer;
    private ObjectTouchHandler touchHandler;
    private Screen screen;
    private Board board;

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
        loadResources();
        createModel();
        createScreens();

        gameView = new GameView(this, this);
        gameView.setShowFPS(true);
        gameView.setOnTouchListener(touchHandler);
        setContentView(gameView);
        showGameOver();
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

    private void loadResources()
    {
        //Don't close the asset manager, as we will need if onCreate is called again
        AssetsReader assets = new AssetsReader(this);
        Assets.TilesAtlas =  assets.loadBitmap("tiles.png", Bitmap.Config.RGB_565);
        Assets.GoButton =  assets.loadBitmap("go_button.png", Bitmap.Config.RGB_565);
        Assets.GoButtonPressed =  assets.loadBitmap("go_button_pressed.png", Bitmap.Config.RGB_565);
        Assets.ClearButton =  assets.loadBitmap("clear_button.png", Bitmap.Config.RGB_565);
        Assets.ClearButtonPressed =  assets.loadBitmap("clear_button_pressed.png", Bitmap.Config.RGB_565);
        Assets.WordBench =  assets.loadBitmap("word_bench.png", Bitmap.Config.RGB_565);

        try {
            Assets.wordList =  LineReader.Read(this,R.raw.standard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        _SoundManager = new SoundManager();
        _SoundManager.initialize();
        ArrayList<Integer> sounds = new ArrayList<Integer>();
        sounds.add(R.raw.coin);
        sounds.add(R.raw.powerup);
        sounds.add(R.raw.laser);
        _SoundManager.loadSounds(this, sounds);
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
        gameScreen.setSoundManager(_SoundManager);

        homeScreen = new HomeScreen();
        homeScreen.setTouchHandler(touchHandler);
        homeScreen.setBuffer(buffer);
        homeScreen.setScreen(screen);

        gameOverScreen = new GameOverScreen();
        gameOverScreen.setTouchHandler(touchHandler);
        gameOverScreen.setBuffer(buffer);
        gameOverScreen.setScreen(screen);

    }

    private void showGame()
    {
        board = new Board(this.screen);
        board.setTiles("streaming");
        board.wordChecker = new WordChecker(Assets.wordList);
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
