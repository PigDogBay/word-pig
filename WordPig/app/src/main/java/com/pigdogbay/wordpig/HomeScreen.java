package com.pigdogbay.wordpig;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import com.pigdogbay.library.games.BitmapButton;
import com.pigdogbay.library.games.FrameBuffer;
import com.pigdogbay.library.games.GameView;
import com.pigdogbay.library.games.ObjectTouchHandler;
import com.pigdogbay.wordpig.model.Screen;
import com.pigdogbay.wordpig.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 07/04/2015.
 */
public class HomeScreen implements GameView.IGame, BitmapButton.OnClickListener {
    private Screen screen;
    private FrameBuffer buffer;
    private ObjectTouchHandler touchHandler;
    private List<Tile> tiles;

    private BitmapButton _GoButton;
    private Assets assets;

    public void setAssets(Assets assets){this.assets=assets;}


    public void setTouchHandler(ObjectTouchHandler touch) {
        this.touchHandler = touch;
    }
    public void setBuffer(FrameBuffer buffer) {
        this.buffer = buffer;
    }
    public void setScreen(Screen screen){this.screen = screen;}

    public HomeScreen() {
    }
    public void initialize(){
        _GoButton = new BitmapButton();
        _GoButton.setBitmaps(assets.GoButton, assets.GoButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y);
        _GoButton.setOnClickListener(this);
        tiles = new ArrayList<Tile>();
        addTiles("word", Defines.HOME_TILES_LINE1_Y);
        addTiles("pig", Defines.HOME_TILES_LINE2_Y);
    }

    @Override
    public void Update() {
        jiggle();
    }


    @Override
    public void Render(Canvas c) {
        buffer.clear(Color.YELLOW);
        drawTiles();
        _GoButton.draw(buffer);
        buffer.scaleToFit(c);
    }

    public void registerTouchables() {
        touchHandler.add(_GoButton);
    }

    @Override
    public void onClick(Object sender) {
        screen.screenStateObserver.setValue(Screen.ScreenState.Game);
    }

    private void drawTiles()
    {
        Point point = new Point();
        for (Tile t : tiles) {
            getTileAtlasCoords(point,t);
            buffer.draw(assets.TilesAtlas, t.x, t.y, point.x, point.y, Defines.TILE_WIDTH, Defines.TILE_HEIGHT);
        }
    }

    private void addTiles(String newLetters, int y)
    {
        int n = newLetters.length();
        //center word
        int x = (Defines.BOARD_WIDTH - n*Defines.TILE_WIDTH - (n-1)*Defines.TILE_POOL_X_SPACING)/2;
        for (char c : newLetters.toCharArray())
        {
            tiles.add(new Tile(c,x,y));
            x = x +Defines.TILE_POOL_X_SPACING+Defines.TILE_WIDTH;
        }
    }

    private void getTileAtlasCoords(Point p,Tile t)
    {
        int i = t.letter-'a';
        p.y = 0;
        if (i>=13)
        {
            p.y = Defines.TILE_HEIGHT;
            i=i-13;
        }
        p.x = i*Defines.TILE_WIDTH;
    }
    int jiggleIndex = -1;
    int jiggleCount = 0;
    private void jiggle() {

        jiggleCount++;
        if (jiggleCount<5)
        {
            jiggleCount++;
            return;
        }
        jiggleCount=0;
        int offset = jiggleIndex*5;
        jiggleIndex = jiggleIndex*-1;
        for (Tile t : tiles)
        {
            t.x = t.x+offset;
            t.y = t.y+offset;
            offset = offset*-1;
        }
    }

}
