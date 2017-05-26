package com.tsm.cards.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "definition")
@TypeAlias("definition")
public class Definition {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private List<Results> results;

    @Getter
    @Setter
    private Metadata metadata;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}