package com.tsm.cards.resources;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import lombok.Getter;
import lombok.Setter;

public class DefinitionsResource {

    @Getter
    @Setter
    private String word;

    @Getter
    @Setter
    private Map<String, String> definitions;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
