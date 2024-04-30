package com.pigdogbay.wordpig;

import android.content.Context;
import android.graphics.Bitmap;

import com.pigdogbay.lib.utils.LineReader;
import com.pigdogbay.library.games.AssetsReader;
import com.pigdogbay.library.games.SoundManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 01/04/2015.
 */
public class Assets {

    public Bitmap TilesAtlas;
    public Bitmap GoButton;
    public Bitmap GoButtonPressed;
    public Bitmap ClearButton;
    public Bitmap ClearButtonPressed;
    public Bitmap WordBench;

    public List<String> wordList;
    public SoundManager SoundManager;



    public void load(Context context)
    {
        //Don't close the asset manager, as we will need if onCreate is called again
        AssetsReader assets = new AssetsReader(context);
        this.TilesAtlas =  assets.loadBitmap("tiles.png", Bitmap.Config.RGB_565);
        this.GoButton =  assets.loadBitmap("go_button.png", Bitmap.Config.RGB_565);
        this.GoButtonPressed =  assets.loadBitmap("go_button_pressed.png", Bitmap.Config.RGB_565);
        this.ClearButton =  assets.loadBitmap("clear_button.png", Bitmap.Config.RGB_565);
        this.ClearButtonPressed =  assets.loadBitmap("clear_button_pressed.png", Bitmap.Config.RGB_565);
        this.WordBench =  assets.loadBitmap("word_bench.png", Bitmap.Config.RGB_565);

        try {
            this.wordList =  LineReader.Read(context, R.raw.standard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.SoundManager = new SoundManager();
        this.SoundManager.initialize();
        ArrayList<Integer> sounds = new ArrayList<Integer>();
        sounds.add(R.raw.coin);
        sounds.add(R.raw.powerup);
        sounds.add(R.raw.laser);
        this.SoundManager.loadSounds(context, sounds);
    }
}
