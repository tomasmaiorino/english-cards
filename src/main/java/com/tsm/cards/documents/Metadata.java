package com.tsm.cards.documents;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.Setter;

@TypeAlias("metadata")
public class Metadata {

    @Getter
    @Setter
    private String provider;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
