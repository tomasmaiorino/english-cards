package com.tsm.cards.documents;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

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
