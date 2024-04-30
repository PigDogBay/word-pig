package com.pigdogbay.wordpig

import android.content.Context
import android.graphics.Bitmap
import com.pigdogbay.lib.games.AssetsReader
import com.pigdogbay.lib.games.SoundManager
import com.pigdogbay.lib.utils.LineReader
import java.io.IOException

/**
 * Created by Mark on 01/04/2015.
 */
class Assets {
    var TilesAtlas: Bitmap? = null
    var GoButton: Bitmap? = null
    var GoButtonPressed: Bitmap? = null
    var ClearButton: Bitmap? = null
    var ClearButtonPressed: Bitmap? = null
    var WordBench: Bitmap? = null
    var wordList: List<String>? = null
    var SoundManager: SoundManager? = null
    fun load(context: Context?) {
        //Don't close the asset manager, as we will need if onCreate is called again
        val assets = AssetsReader(context!!)
        TilesAtlas = assets.loadBitmap("tiles.png", Bitmap.Config.RGB_565)
        GoButton = assets.loadBitmap("go_button.png", Bitmap.Config.RGB_565)
        GoButtonPressed = assets.loadBitmap("go_button_pressed.png", Bitmap.Config.RGB_565)
        ClearButton = assets.loadBitmap("clear_button.png", Bitmap.Config.RGB_565)
        ClearButtonPressed = assets.loadBitmap("clear_button_pressed.png", Bitmap.Config.RGB_565)
        WordBench = assets.loadBitmap("word_bench.png", Bitmap.Config.RGB_565)
        try {
            wordList = LineReader.Read(context, R.raw.standard)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        SoundManager = SoundManager()
        SoundManager!!.initialize()
        val sounds = ArrayList<Int>()
        sounds.add(R.raw.coin)
        sounds.add(R.raw.powerup)
        sounds.add(R.raw.laser)
        SoundManager!!.loadSounds(context, sounds)
    }
}
