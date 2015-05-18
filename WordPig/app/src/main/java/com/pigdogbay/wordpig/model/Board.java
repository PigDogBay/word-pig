package com.pigdogbay.wordpig.model;

import com.pigdogbay.library.games.Timer;
import com.pigdogbay.wordpig.Defines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mark on 01/04/2015.
 */
public class Board
{
    public enum GameState
    {
        Initialize,
        FirstWord,
        InPlay,
        TimesUp,
        Exit
    }

    public Screen screen;
    public List<Tile> tiles;
    public int score;
    public int level;
    public float time;
    public Boom boom;
    public WordChecker wordChecker;
    private Tray tray;
    private List<String> usedWords;
    private GameState gameState;
    private Timer timer;
    private GameEvent gameEvent;

    public Board(Screen screen)
    {
        this.screen = screen;
        tiles = new ArrayList<Tile>();
        score = 0;
        level = 42;
        time = Defines.TIMER_MAX_VAL;
        boom = new Boom();
        tray = new Tray();
        usedWords = new ArrayList<String>();
        gameState = GameState.Initialize;
        timer = new Timer(0);
        this.gameEvent = new GameEvent();
    }

    public void addEventListener(GameEvent.IGameEventListener listener)
    {
        this.gameEvent.addListener(listener);
    }

    public void update()
    {
        boom.update();
        switch (gameState)
        {
            case Initialize:
                boom.addMessage("Get Ready!");
                gameState = GameState.FirstWord;
                break;
            case FirstWord:
                if (usedWords.size()>0)
                {
                    gameState = GameState.InPlay;
                }
                break;
            case InPlay:
                time--;
                if (time==0)
                {
                    boom.addMessage("Times Up");
                    timer.reset(Defines.TIMES_UP_DURATION);
                    gameState = GameState.TimesUp;
                }
                break;
            case TimesUp:
                if (timer.hasTimeElapsed())
                {
                    gameState = GameState.Exit;
                    gameOver();
                }
                break;
            case Exit:
                break;
        }
    }

    public void setTiles(String newLetters)
    {
        tiles.clear();
        int x = Defines.TILE_POOL_X;
        for (char c : newLetters.toCharArray())
        {
            tiles.add(new Tile(c,x,Defines.TILE_POOL_Y));
            x = x +Defines.TILE_POOL_X_SPACING+Defines.TILE_WIDTH;
        }
    }

    public void go()
    {
        if (GameState.InPlay != gameState && GameState.FirstWord!= gameState)
        {
            //ignore
            return;
        }
        String word = getWord();
        if ("".equals(word))
        {
            gameEvent.fire(this,GameEvents.GAME_EVENT_WORD_EMPTY);
            boom.addMessage("eh?");
            return;
        }
        if (usedWords.contains(word))
        {
            gameEvent.fire(this,GameEvents.GAME_EVENT_WORD_ALREADY_USED);
            boom.addMessage("Used");
            return;
        }
        if (wordChecker.isWord(word))
        {
            int points = tray.getScore();
            boom.addMessage(Integer.toString(points));
            score = score + points;
            gameEvent.fire(this,GameEvents.GAME_EVENT_WORD_OK);
        }
        else
        {
            boom.addMessage(Integer.toString(Defines.SCORE_NOT_A_WORD));
            score = score + Defines.SCORE_NOT_A_WORD;
            gameEvent.fire(this,GameEvents.GAME_EVENT_WORD_DOES_NOT_EXIST);
        }
        usedWords.add(word);
    }

    public void clear()
    {
        int x = Defines.TILE_POOL_X;
        for (Tile t : tiles)
        {
            t.x = x;
            t.y = Defines.TILE_POOL_Y;
            x = x + Defines.TILE_POOL_X_SPACING+Defines.TILE_WIDTH;
        }
        boom.addMessage("CLEAR");
    }

    private String getWord()
    {
        tray.setTilesIfInTray(tiles);
        tray.sortTilesByPosition();
        return tray.toString();
    }

    private void gameOver()
    {
        screen.screenStateObserver.setValue(Screen.ScreenState.GameOver);
    }
}
