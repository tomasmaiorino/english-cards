package com.tsm.cards.documents;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("subsenses")
public class Subsenses {

    @Getter
    @Setter
    private List<String> definitions;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private List<Synonyms> synonyms;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
