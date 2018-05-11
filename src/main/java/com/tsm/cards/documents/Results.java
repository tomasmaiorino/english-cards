package com.tsm.cards.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("results")
public class Results {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String language;

    @Getter
    @Setter
    @JsonIgnore
    private String type;

    @Getter
    @Setter
    private List<LexicalEntries> lexicalEntries;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
