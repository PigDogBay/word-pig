package com.pigdogbay.wordpig;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import com.pigdogbay.wordpig.model.Screen;
import com.pigdogbay.wordpig.model.Tile;

import java.util.ArrayList;
import java.util.List;
import com.pigdogbay.lib.games.*;

/**
 * Created by Mark on 07/04/2015.
 */
public class GameOverScreen implements GameView.IGame, BitmapButton.OnClickListener {
    private Screen screen;
    private FrameBuffer buffer;
    private ObjectTouchHandler touchHandler;
    private List<Tile> tiles;
    private BitmapButton _GoButton;
    private Assets _Assets;

    public void setAssets(Assets assets){_Assets=assets;}


    public void setTouchHandler(ObjectTouchHandler touch) {
        this.touchHandler = touch;
    }
    public void setBuffer(FrameBuffer buffer) {
        this.buffer = buffer;
    }
    public void setScreen(Screen screen){this.screen = screen;}

    public GameOverScreen() {
    }
    public void initializie(){
        _GoButton = new BitmapButton();
        _GoButton.setBitmaps(_Assets.GoButton, _Assets.GoButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y);
        _GoButton.setOnClickListener(this);
        tiles = new ArrayList<Tile>();
        addTiles("game", Defines.HOME_TILES_LINE1_Y);
        addTiles("over", Defines.HOME_TILES_LINE2_Y);    }

    @Override
    public void Update() {

    }

    @Override
    public void Render(Canvas c) {
        buffer.clear(Color.BLUE);
        drawTiles();
        _GoButton.draw(buffer);
        buffer.scaleToFit(c);
    }

    public void registerTouchables() {
        touchHandler.add(_GoButton);
    }

    @Override
    public void onClick(Object sender) {
        screen.screenStateObserver.setValue(Screen.ScreenState.Home);
    }

    private void drawTiles()
    {
        Point point = new Point();
        for (Tile t : tiles) {
            getTileAtlasCoords(point,t);
            buffer.draw(_Assets.TilesAtlas, t.x, t.y, point.x, point.y, Defines.TILE_WIDTH, Defines.TILE_HEIGHT);
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
}
