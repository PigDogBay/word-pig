package com.pigdogbay.wordpig.model;

import java.util.List;

/**
 * Created by Mark on 02/04/2015.
 */
public class WordChecker
{
    List<String> wordList;

    public WordChecker(List<String> wordList)
    {
        this.wordList = wordList;
    }

    public boolean isWord(String candidate)
    {
//        int len = candidate.length();
        for (String word : wordList)
        {
            if (word.equals(candidate))
            {
                return true;
            }
        }
        return false;
    }
}
