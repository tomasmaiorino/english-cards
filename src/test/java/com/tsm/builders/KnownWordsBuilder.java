package com.tsm.builders;

import java.util.ArrayList;
import java.util.List;

import com.tsm.cards.documents.KnownWord;

public class KnownWordsBuilder {

    public List<KnownWord> knownWords = new ArrayList<>();

    public KnownWordsBuilder words(final List<String> words) {
        words.forEach(w -> {
            KnownWord knownWord = new KnownWord();
            knownWord.setWord(w);
            knownWords.add(knownWord);
        });
        return this;
    }

    public List<KnownWord> build() {
        return knownWords;
    }

}
