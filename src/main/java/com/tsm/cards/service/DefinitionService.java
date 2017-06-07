package com.tsm.cards.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.repository.DefinitionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class DefinitionService {

    @Autowired
    private DefinitionRepository repository;

    @Transactional
    public Definition save(final Definition definition) {
        Assert.notNull(definition, "The definition must not be null.");
        log.info("Saving definition [{}] .", definition);

        repository.save(definition);

        log.info("Saved definition [{}].", definition);
        return definition;
    }

    public Definition findByDefinitionId(final String definitionId) {
        Assert.notNull(definitionId, "The definitionId must not be null.");
        log.info("Searching for definition by definitions id [{}] .", definitionId);

        Definition definition = repository.findByDefinitionId(definitionId).orElse(null);

        log.info("Found cache definition ? [{}] .", definition != null);

        return definition;
    }

    public Definition findById(final String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for cached definition [{}] .", word);

        Definition definition = findOptionalDefinitionById(word)
            .orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found cache [{}] definition.", definition);

        return definition;

    }

    public Optional<Definition> findOptionalDefinitionById(final String word) {
        Assert.notNull(word, "The id must not be null.");
        return repository.findById(word);
    }

}
