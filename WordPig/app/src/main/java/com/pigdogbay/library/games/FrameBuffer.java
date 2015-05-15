package com.pigdogbay.library.games;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Expected usage:
 * -Create in your IGame implementation
 * -During render
 * --Clear buffer using clear()
 * --Draw game using draw methods
 * --draw buffer to the surface view, using scaleToFit()
 * -When finished with the buffer (eg on activity pause) call close()
 */
public class FrameBuffer {

    private Bitmap _Buffer;
    private Canvas _Canvas;
    Rect _SrcRect = new Rect();
    Rect _DstRect = new Rect();

    /**
     * @return the buffer's underlying bitmap
     */
    public Bitmap getBuffer() {
        return _Buffer;
    }

    /**
     * Recycles the underlying bitmap
     */
    public void close(){
        _Buffer.recycle();
    }

    public FrameBuffer(int width, int height){
        _Buffer = Bitmap.createBitmap(width,height,Config.RGB_565);
        _Canvas = new Canvas(_Buffer);
    }
    public void clear(int color){
        _Canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }
    public Canvas getCanvas(){
        return _Canvas;
    }
    /**
     * Draws a section of the bitmap to the buffer
     * Ideal for sprite sheets/image atlas
     * @param bitmap
     * @param x - buffer x co-ord
     * @param y - buffer y co-ord
     * @param srcX
     * @param srcY
     * @param srcWidth
     * @param srcHeight
     */
    public void draw(Bitmap bitmap, int x, int y, int srcX, int srcY,
                     int srcWidth, int srcHeight) {
        _SrcRect.left = srcX;
        _SrcRect.top = srcY;
        _SrcRect.right = srcX + srcWidth - 1;
        _SrcRect.bottom = srcY + srcHeight - 1;

        _DstRect.left = x;
        _DstRect.top = y;
        _DstRect.right = x + srcWidth - 1;
        _DstRect.bottom = y + srcHeight - 1;

        _Canvas.drawBitmap(bitmap, _SrcRect, _DstRect,null);
    }

    public void draw(Bitmap bitmap, int x, int y) {
        _Canvas.drawBitmap(bitmap, x, y, null);
    }

    public int getWidth() {
        return _Buffer.getWidth();
    }

    public int getHeight() {
        return _Buffer.getHeight();
    }


    /**
     * Draws the buffer to the canvas, the buffer is scaled to fit.
     * @param c canvas from the surface view
     */
    public void scaleToFit(Canvas c){
        c.getClipBounds(_DstRect);
        c.drawBitmap(_Buffer,null,_DstRect,null);
    }

}
