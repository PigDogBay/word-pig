package com.pigdogbay.wordpig;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.pigdogbay.library.games.BitmapButton;
import com.pigdogbay.library.games.FrameBuffer;
import com.pigdogbay.library.games.GameView;
import com.pigdogbay.library.games.ObjectTouchHandler;
import com.pigdogbay.wordpig.model.Board;
import com.pigdogbay.wordpig.model.GameEvent;
import com.pigdogbay.wordpig.model.GameEvents;
import com.pigdogbay.wordpig.model.Tile;
import com.pigdogbay.wordpig.model.TouchTile;

/**
 * Created by Mark on 01/04/2015.
 */
public class GameScreen implements GameView.IGame, BitmapButton.OnClickListener, GameEvent.IGameEventListener {

    private FrameBuffer _Buffer;
    private ObjectTouchHandler _TouchHandler;
    private Board _Board;
    private BitmapButton _GoButton, _ClearButton;
    private Paint _TextPaint,_TimerOuterPaint,_TimerInnerPaint, _BoomPaint;
    private Assets _Assets;

    public void setAssets(Assets assets){_Assets=assets;}
    public void setBoard(Board board){
        _Board = board;
        _Board.addEventListener(this);
    }
    public void setTouchHandler(ObjectTouchHandler touch) {
        _TouchHandler = touch;
    }
    public void setBuffer(FrameBuffer buffer) {
        this._Buffer = buffer;
    }

    public GameScreen() {
    }
    public void initilize(){
        _TextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _TextPaint.setColor(Color.WHITE);
        _TextPaint.setTextSize(36);

        _TimerInnerPaint = new Paint();
        _TimerInnerPaint.setColor(Color.WHITE);
        _TimerOuterPaint = new Paint();
        _TimerOuterPaint.setColor(Color.RED);
        _BoomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _BoomPaint.setColor(Color.RED);
        _BoomPaint.setTextSize(144);

        _GoButton = new BitmapButton();
        _GoButton.setBitmaps(_Assets.GoButton, _Assets.GoButtonPressed, Defines.GO_BUTTON_X, Defines.GO_BUTTON_Y);
        _GoButton.setOnClickListener(this);

        _ClearButton = new BitmapButton();
        _ClearButton.setBitmaps(_Assets.ClearButton, _Assets.ClearButtonPressed, Defines.CLEAR_BUTTON_X, Defines.CLEAR_BUTTON_Y);
        _ClearButton.setOnClickListener(this);
    }
    public void registerTouchables()
    {
        _TouchHandler.add(_GoButton);
        _TouchHandler.add(_ClearButton);

        for (Tile t : _Board.tiles)
        {
            TouchTile touchTile = new TouchTile(t);
            _TouchHandler.add(touchTile);
        }
    }

    //IGame
    @Override
    public void Update() {
        _Board.update();
    }

    @Override
    public void Render(Canvas c) {
        _Buffer.clear(0);
        final Canvas buffCanvas = _Buffer.getCanvas();

        _Buffer.draw(_Assets.WordBench, Defines.WORD_BENCH_X, Defines.WORD_BENCH_Y);
        drawButtons();
        drawScore(buffCanvas);
        drawTimer(buffCanvas);
        drawTiles();
        drawBoom(buffCanvas);

        _Buffer.scaleToFit(c);
    }
    private void drawBoom(Canvas buffCanvas)
    {
        if (_Board.boom.isMessageAvailable)
        {
            final float y = Defines.BOOM_Y - (float)(_Board.boom.count*10);
            buffCanvas.drawText(_Board.boom.latestMessage, Defines.BOOM_X, y, _BoomPaint);

        }
    }
    private void drawTiles()
    {
        Point point = new Point();
        for (Tile t : _Board.tiles) {
            getTileAtlasCoords(point,t);
            _Buffer.draw(_Assets.TilesAtlas, t.x, t.y, point.x, point.y, Defines.TILE_WIDTH, Defines.TILE_HEIGHT);
        }

    }
    private void drawButtons()
    {
        _GoButton.draw(_Buffer);
        _ClearButton.draw(_Buffer);
    }
    private void drawScore(Canvas buffCanvas)
    {
        buffCanvas.drawText("Level: "+ Integer.toString(_Board.level), Defines.LEVEL_X, Defines.LEVEL_Y, _TextPaint);
        buffCanvas.drawText("Score: "+Integer.toString(_Board.score), Defines.SCORE_X, Defines.SCORE_Y, _TextPaint);

    }
    private void drawTimer(Canvas buffCanvas)
    {
        buffCanvas.drawRect(Defines.TIMER_OUTER_RECT,_TimerOuterPaint);
        float len = Defines.TIMER_INNER_RIGHT - Defines.TIMER_INNER_LEFT;
        len = Defines.TIMER_INNER_LEFT+ len * _Board.time/Defines.TIMER_MAX_VAL;
        buffCanvas.drawRect(Defines.TIMER_INNER_LEFT,Defines.TIMER_INNER_TOP,len,Defines.TIMER_INNER_BOTTOM,_TimerInnerPaint);
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

    @Override
    public void onClick(Object sender)
    {
        if (sender == _GoButton)
        {
            _Board.go();
        }
        else if (sender == _ClearButton)
        {
            _Board.clear();
        }

    }


    @Override
    public void onGameEvent(Object sender, int id) {
        switch (id){
            case GameEvents.GAME_EVENT_WORD_OK:
                _Assets.SoundManager.play(R.raw.coin,0.1f);
                break;
            default:
                _Assets.SoundManager.play(R.raw.laser,0.2f);
        }
    }
}
