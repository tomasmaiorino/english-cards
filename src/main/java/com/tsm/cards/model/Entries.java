package com.tsm.cards.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@TypeAlias("entries")
public class Entries {
    
    @Getter @Setter
    @JsonIgnore
    private String homographNumber;
    
    @Getter @Setter
    private String language;
    
    @Getter @Setter
    private String lexicalCategory;
    
    @Getter @Setter
    private String text;
    
    @Getter @Setter
    private List<Senses> senses;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
