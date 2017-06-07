package com.tsm.cards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Definition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManageWordService {

    @Autowired
    @Getter
    @Setter
    private DefinitionService definitionService;

    @Autowired
    @Getter
    @Setter
    private OxfordService oxfordService;

    public Definition createDefinition(final String word) throws Exception {
        Assert.hasText(word, "The word must not be empty or null.");
        log.debug("creating definition for known word [{}] .", word);

        Definition definition = oxfordService.findWordDefinition(word);
        definition.setId(word);

        return definitionService.save(definition);
    }

}
