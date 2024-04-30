package com.pigdogbay.wordpig.model

import kotlin.concurrent.Volatile

/**
 * Created by Mark on 01/04/2015.
 */
class Tile(var letter: Int, @field:Volatile var x: Int, @field:Volatile var y: Int)
