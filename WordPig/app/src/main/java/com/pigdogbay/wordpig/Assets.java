package com.pigdogbay.wordpig;

import android.content.Context;
import android.graphics.Bitmap;

import com.pigdogbay.library.games.AssetsReader;
import com.pigdogbay.library.games.SoundManager;
import com.pigdogbay.library.utils.LineReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 01/04/2015.
 */
public class Assets {

    public static Bitmap TilesAtlas;
    public static Bitmap GoButton;
    public static Bitmap GoButtonPressed;
    public static Bitmap ClearButton;
    public static Bitmap ClearButtonPressed;
    public static Bitmap WordBench;

    public static List<String> wordList;
    public static SoundManager SoundManager;



    public static void load(Context context)
    {
        //Don't close the asset manager, as we will need if onCreate is called again
        AssetsReader assets = new AssetsReader(context);
        Assets.TilesAtlas =  assets.loadBitmap("tiles.png", Bitmap.Config.RGB_565);
        Assets.GoButton =  assets.loadBitmap("go_button.png", Bitmap.Config.RGB_565);
        Assets.GoButtonPressed =  assets.loadBitmap("go_button_pressed.png", Bitmap.Config.RGB_565);
        Assets.ClearButton =  assets.loadBitmap("clear_button.png", Bitmap.Config.RGB_565);
        Assets.ClearButtonPressed =  assets.loadBitmap("clear_button_pressed.png", Bitmap.Config.RGB_565);
        Assets.WordBench =  assets.loadBitmap("word_bench.png", Bitmap.Config.RGB_565);

        try {
            Assets.wordList =  LineReader.Read(context, R.raw.standard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assets.SoundManager = new SoundManager();
        Assets.SoundManager.initialize();
        ArrayList<Integer> sounds = new ArrayList<Integer>();
        sounds.add(R.raw.coin);
        sounds.add(R.raw.powerup);
        sounds.add(R.raw.laser);
        Assets.SoundManager.loadSounds(context, sounds);
    }
}
