package com.pigdogbay.wordpig;


import android.graphics.RectF;

/**
 * Created by Mark on 02/04/2015.
 */
public final class Defines
{
    public static final int BOARD_HEIGHT = 1000;
    public static final int BOARD_WIDTH = 1500;
    public static final int TILE_WIDTH = 128;
    public static final int TILE_HEIGHT = 128;
    public static final int WORD_BENCH_X = 50;
    public static final int WORD_BENCH_Y = 400;
    public static final int GO_BUTTON_X = 850;
    public static final int GO_BUTTON_Y = 700;
    public static final int CLEAR_BUTTON_X = 200;
    public static final int CLEAR_BUTTON_Y = GO_BUTTON_Y;
    public static final int TILE_POOL_Y = 200;
    public static final int TILE_POOL_X = 100;
    public static final int TILE_POOL_X_SPACING = 22;
    public static final float SCORE_X = 100f;
    public static final float SCORE_Y = 60f;
    public static final float LEVEL_X = 1200f;
    public static final float LEVEL_Y = SCORE_Y;
    public static final RectF TIMER_OUTER_RECT = new RectF(90,90,1410,140);
    public static final float TIMER_INNER_LEFT = 100f;
    public static final float TIMER_INNER_TOP = 100f;
    public static final float TIMER_INNER_RIGHT = 1400f;
    public static final float TIMER_INNER_BOTTOM = 130f;
    public static final float TIMER_MAX_VAL = 2000f;
    public static final float BOOM_X = 650;
    public static final float BOOM_Y = 600;

    public static final int TRAY_MIN_Y = WORD_BENCH_Y;
    public static final int TRAY_MAX_Y = WORD_BENCH_Y+200;

    public static final int SCORE_NOT_A_WORD = -5;
    public static final int[] SCORE_LETTER = new int[]{
            //a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q ,r,s,t,u,v,w,x,y,z
              1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10
    };

    public static final long TIMES_UP_DURATION = 4000L;

    public static final int HOME_TILES_LINE1_Y = 200;
    public static final int HOME_TILES_LINE2_Y = 350;


}
