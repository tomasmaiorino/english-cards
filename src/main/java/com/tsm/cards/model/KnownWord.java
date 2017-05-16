package com.tsm.cards.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "known_words")
@TypeAlias("known_word")
//mongoimport --db test --collection known_words --file 20k.txt
public class KnownWord extends BaseModel {

    @Getter
    @Setter
    private String word;
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
