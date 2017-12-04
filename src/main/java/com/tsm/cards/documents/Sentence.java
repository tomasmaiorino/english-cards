package com.tsm.cards.documents;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import lombok.Getter;
import lombok.Setter;

public class Sentence {


    @Getter
    @Setter
    private List<String> regions;

    @Getter
    @Setter
    private String text;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
