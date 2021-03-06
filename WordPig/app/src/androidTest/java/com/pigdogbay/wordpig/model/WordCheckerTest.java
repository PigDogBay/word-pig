package com.pigdogbay.wordpig.model;

import android.test.AndroidTestCase;

import com.pigdogbay.library.diagnostics.Timing;
import com.pigdogbay.library.utils.LineReader;
import com.pigdogbay.wordpig.R;


import java.util.List;

public class WordCheckerTest extends AndroidTestCase {

    public void testIsWord1() throws Exception {
        List<String> wordList = LineReader.Read(getContext(), R.raw.standard);
        WordChecker target = new WordChecker(wordList);
        assertTrue(target.isWord("test"));
        assertFalse(target.isWord("qwsdt"));
    }

    public void testLoadTimes() throws Exception {
        List<String> wordList = null;

        Timing timing = new Timing();
        for (int i=0;i<10;i++) {
            wordList = LineReader.Read(getContext(), R.raw.standard);
        }
        timing.LogDuration("tests");
    }

    public void testSearchTimes() throws Exception {
        List<String> wordList = LineReader.Read(getContext(), R.raw.standard);
        WordChecker target = new WordChecker(wordList);

        Timing timing = new Timing();
        for (int i=0;i<10;i++) {
            assertTrue(target.isWord("test"));
        }
        timing.LogDuration("tests");
    }

}