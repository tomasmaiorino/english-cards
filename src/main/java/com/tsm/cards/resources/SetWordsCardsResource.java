package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SetWordsCardsResource {

    @Getter
    @Setter
    private List<DefinitionResource> definitionsResource;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<String> words;

}
