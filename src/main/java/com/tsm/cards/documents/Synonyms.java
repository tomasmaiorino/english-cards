package com.tsm.cards.documents;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.Setter;

@TypeAlias("synonyms")
public class Synonyms {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String standard;

    @Getter
    @Setter
    private String text;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
