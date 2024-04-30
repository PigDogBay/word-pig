package com.pigdogbay.wordpig

import android.graphics.RectF

/**
 * Created by Mark on 02/04/2015.
 */
object Defines {
    const val BOARD_HEIGHT = 1000
    const val BOARD_WIDTH = 1500
    const val TILE_WIDTH = 128
    const val TILE_HEIGHT = 128
    const val WORD_BENCH_X = 50
    const val WORD_BENCH_Y = 400
    const val GO_BUTTON_X = 850
    const val GO_BUTTON_Y = 700
    const val CLEAR_BUTTON_X = 200
    const val CLEAR_BUTTON_Y = GO_BUTTON_Y
    const val TILE_POOL_Y = 200
    const val TILE_POOL_X = 100
    const val TILE_POOL_X_SPACING = 22
    const val SCORE_X = 100f
    const val SCORE_Y = 60f
    const val LEVEL_X = 1200f
    const val LEVEL_Y = SCORE_Y
    @JvmField
    val TIMER_OUTER_RECT = RectF(90f, 90f, 1410f, 140f)
    const val TIMER_INNER_LEFT = 100f
    const val TIMER_INNER_TOP = 100f
    const val TIMER_INNER_RIGHT = 1400f
    const val TIMER_INNER_BOTTOM = 130f
    const val TIMER_MAX_VAL = 2000f
    const val BOOM_X = 650f
    const val BOOM_Y = 600f
    const val TRAY_MIN_Y = WORD_BENCH_Y
    const val TRAY_MAX_Y = WORD_BENCH_Y + 200
    const val SCORE_NOT_A_WORD = -5
    @JvmField
    val SCORE_LETTER = intArrayOf( //a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q ,r,s,t,u,v,w,x,y,z
        1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    )
    const val TIMES_UP_DURATION = 4000L
    const val HOME_TILES_LINE1_Y = 200
    const val HOME_TILES_LINE2_Y = 350
}
