package com.tsm.cards.definition.service;

import org.springframework.util.Assert;

import com.tsm.cards.documents.BaseDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseManageWordService<T extends BaseDefinition> {

    protected abstract BaseDefinitionService<T, ?> getDefinitionService();

    protected abstract String getDefinitionServiceName();

    protected abstract OxfordService<T> getOxfordService();

    public T createDefinition(final String word) throws Exception {
        Assert.hasText(word, "The word must not be empty or null.");
        log.debug("Creating definition [{}] for known word [{}].", getDefinitionServiceName(), word);

        T definition = getOxfordService().find(word);
        definition.setId(word);

        return getDefinitionService().save(definition);
    }

}
