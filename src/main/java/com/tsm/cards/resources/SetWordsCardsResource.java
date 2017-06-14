package com.tsm.cards.resources;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
