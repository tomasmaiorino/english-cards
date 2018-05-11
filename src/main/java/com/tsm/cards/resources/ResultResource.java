package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class ResultResource {

    @Getter
    @Setter
    private Set<String> givenWords;

    @Getter
    @Setter
    private Set<String> invalidWords;
    
    @Getter
    @Setter
    private Set<String> notFoundWords;

    @Getter
    @Setter
    private Set<DefinitionResource> definitions;

}
