package com.pigdogbay.library.games;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements GameThread.GameThreadClient {
    public interface IGame
    {
        void Update();
        void Render(Canvas c);
    }

    private GameThread _GameThread;
    private SurfaceHolder _Holder;
    private IGame _Game;
    private boolean _ShowFPS=false;
    private Paint _TextPaint;
    private FramesPerSecond _FPS;

    public void setShowFPS(boolean show)
    {
        _ShowFPS = show;
    }
    public GameView(Context context, IGame game) {
        super(context);
        _Game = game;
        _Holder = getHolder();
        _FPS = new FramesPerSecond();
        _TextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _TextPaint.setColor(Color.WHITE);
        _TextPaint.setTextSize(36);
    }

    public void pause() {
        Log.v("MPDB", "GameView pause");
        if (_GameThread==null)
        {
            return;
        }
        _GameThread.setRunning(false);
        while (true) {
            try {
                _GameThread.join();
                _GameThread = null;
                break;

            } catch (InterruptedException e) {
            }
        }

    }

    public void resume() {
        Log.v("MPDB", "GameView resume");
        _GameThread = new GameThread(this);
        _GameThread.setRunning(true);
        _GameThread.start();
    }

    public void Update() {
        _Game.Update();
    }

    public void Render() {
        if (!_Holder.getSurface().isValid()) {
            return;
        }
        Canvas c = null;
        try {
            c = _Holder.lockCanvas();
            synchronized (_Holder) {
                _Game.Render(c);
                if (_ShowFPS){
                    _FPS.CalculateFPS();
                    c.drawText(_FPS.getFPS(), 0, 36, _TextPaint);
                }
            }
        } finally {
            if (c != null) {
                _Holder.unlockCanvasAndPost(c);
            }
        }

    }
}

