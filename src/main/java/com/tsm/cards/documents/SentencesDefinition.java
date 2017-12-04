package com.tsm.cards.documents;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sentences_definition")
@TypeAlias("sentences_definition")
public class SentencesDefinition extends BaseDefinition {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
