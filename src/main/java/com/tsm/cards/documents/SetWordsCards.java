package com.tsm.cards.documents;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "set_words_cards")
@TypeAlias("set_words_cards")
public class SetWordsCards extends BaseDocumentModel {

    @Getter
    @Setter
    private List<KnownWord> knownWords;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private boolean showHome;

    @Getter
    @Setter
    private boolean temporary;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
