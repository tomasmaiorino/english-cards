package com.tsm.cards.documents;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

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
