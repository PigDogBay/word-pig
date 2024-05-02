package com.pigdogbay.wordpig

import android.content.Context
import android.graphics.Bitmap
import com.pigdogbay.lib.games.AssetsReader
import com.pigdogbay.lib.games.SoundManager
import com.pigdogbay.lib.utils.LineReader

/**
 * Created by Mark on 01/04/2015.
 */
object Assets {
    lateinit var tilesAtlas: Bitmap
    lateinit var goButton: Bitmap
    lateinit var goButtonPressed: Bitmap
    lateinit var clearButton: Bitmap
    lateinit var clearButtonPressed: Bitmap
    lateinit var wordBench: Bitmap
    lateinit var wordList: List<String>
    val soundManager = SoundManager()

    fun load(context: Context) {
        //Don't close the asset manager, as we will need if onCreate is called again
        val assets = AssetsReader(context)
        tilesAtlas = assets.loadBitmap("tiles.png", Bitmap.Config.RGB_565)!!
        goButton = assets.loadBitmap("go_button.png", Bitmap.Config.RGB_565)!!
        goButtonPressed = assets.loadBitmap("go_button_pressed.png", Bitmap.Config.RGB_565)!!
        clearButton = assets.loadBitmap("clear_button.png", Bitmap.Config.RGB_565)!!
        clearButtonPressed = assets.loadBitmap("clear_button_pressed.png", Bitmap.Config.RGB_565)!!
        wordBench = assets.loadBitmap("word_bench.png", Bitmap.Config.RGB_565)!!
        wordList = LineReader.Read(context, R.raw.standard)
        soundManager.initialize()
        val sounds = ArrayList<Int>()
        sounds.add(R.raw.coin)
        sounds.add(R.raw.powerup)
        sounds.add(R.raw.laser)
        soundManager.loadSounds(context, sounds)
    }
}
