package com.pigdogbay.wordpig.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pigdogbay.lib.diagnostics.Timing
import com.pigdogbay.lib.utils.LineReader
import com.pigdogbay.wordpig.R
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordCheckerTest {
    @Test
    fun testIsWord1() {
        val wordList: List<String> = LineReader.Read(InstrumentationRegistry.getInstrumentation().targetContext, R.raw.standard)
        val target = WordChecker(wordList)
        assertTrue(target.isWord("test"))
        assertFalse(target.isWord("qwsdt"))
    }

    @Test
    fun testLoadTimes() {
        var wordList: List<String?>? = null
        val timing = Timing()
        for (i in 0..9) {
            wordList = LineReader.Read(InstrumentationRegistry.getInstrumentation().targetContext, R.raw.standard)
        }
        timing.logDuration("tests")
    }

    @Test
    fun testSearchTimes() {
        val wordList: List<String> = LineReader.Read(InstrumentationRegistry.getInstrumentation().targetContext, R.raw.standard)
        val target = WordChecker(wordList)
        val timing = Timing()
        for (i in 0..9) {
            assertTrue(target.isWord("test"))
        }
        timing.logDuration("tests")
    }
}
