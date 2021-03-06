package com.tsm.cards.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("senses")
public class Senses {

    @Getter
    @Setter
    private List<String> definitions;

    @Getter
    @Setter
    @JsonIgnore
    private String id;

    @Getter
    @Setter
    private List<Subsenses> subsenses;

    @Getter
    @Setter
    private List<Synonyms> synonyms;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
