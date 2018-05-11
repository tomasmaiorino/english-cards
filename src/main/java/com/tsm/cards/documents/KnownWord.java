package com.tsm.cards.documents;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "known_words")
@TypeAlias("known_word")
// mongoimport --db test --collection known_words --file 20k.txt
public class KnownWord extends BaseDocumentModel {

    @Getter
    @Setter
    private String word;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
