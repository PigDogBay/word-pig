package com.pigdogbay.library.games;


import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * Loads bitmaps, sounds from the top level assets folder
 * Internally this class uses AssetManager, call close() when finished loading assets
 *
 */
public class AssetsReader
{

    AssetManager _AssetManager;

    public AssetsReader(Context context)
    {
        _AssetManager = context.getAssets();
    }

    /**
     * Closes the underlying AssetManager
     */
    public void close()
    {
        _AssetManager.close();
    }

    /**
     * Loads a bitmap from the assets folder
     *
     * Call bitmap.recycle() when finished with bitmap
     *
     * @throws RuntimeException if couldn't load a bitmap
     * @param fileName e.g. background.png
     * @param config - preferred configuration
     * @return Bitmap
     */
    public Bitmap loadBitmap(String fileName, Bitmap.Config config)
    {
        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = _AssetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null) {
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }
}
