package com.tsm.cards.definition.service;

import com.tsm.cards.definition.repository.BaseDefinitionRepository;
import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
public abstract class BaseDefinitionService<T extends BaseDefinition, ID extends Serializable> {

    protected abstract BaseDefinitionRepository<T, ID> getRepository();

    protected abstract String getServiceName();

    @Transactional
    public T save(final T definition) {
        Assert.notNull(definition, "The definition must not be null.");
        log.info("Saving {} definition [{}] .", getServiceName(), definition);

        getRepository().save(definition);

        log.info("Saved definition [{}].", definition);
        return definition;
    }

    public T findByDefinitionId(final String definitionId) {
        Assert.notNull(definitionId, "The definitionId must not be null.");
        log.info("Searching for {} by definitions id [{}] .", getServiceName(), definitionId);

        T definition = getRepository().findByDefinitionId(definitionId).orElse(null);

        log.info("Found cache definition ? [{}] .", definition != null);

        return definition;
    }

    public T findById(final String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for cached {} [{}] .", getServiceName(), word);

        T definition = findOptionalDefinitionById(word)
            .orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found cache [{}] definition.", definition);

        return definition;

    }

    public Optional<T> findOptionalDefinitionById(final String word) {
        Assert.notNull(word, "The id must not be null.");
        log.info("Searching for definition [{}] by id for the service [{}]", word, getServiceName());
        return getRepository().findById(word);
    }

}
