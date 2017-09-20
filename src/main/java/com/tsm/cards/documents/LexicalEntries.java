package com.tsm.cards.documents;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.Setter;

@TypeAlias("lexicalEntries")
public class LexicalEntries {

    @Getter
    @Setter
    private List<Entries> entries;

    @Getter
    @Setter
    private String language;

    @Getter
    @Setter
    private String lexicalCategory;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private List<Sentence> sentences;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
