package com.tsm.cards.definition.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.Results;
import com.tsm.cards.resources.DefinitionResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseBuildDefResourceService {

    protected abstract Integer getMaxDefinitionSize();

    public List<DefinitionResource> loadResource(List<BaseDefinition> cachedWords) {
        Assert.notEmpty(cachedWords, "The cachedWords must not be empty.");
        log.info("Loading resource ->");

        List<Results> results = cachedWords.stream().flatMap(c -> c.getResults().stream())
            .filter(r -> r.getLexicalEntries() != null).collect(Collectors.toList());

        List<DefinitionResource> resources = getResource(results);

        log.info("Loading resource <-");
        return resources;
    }

    protected abstract List<DefinitionResource> getResource(final List<Results> results);

}
