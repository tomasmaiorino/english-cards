package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Entries;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.model.Results;
import com.tsm.cards.resources.DefinitionsResource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildDefinitionsResourceService {

    public List<DefinitionsResource> loadResource(List<OriginalCall> cachedWords) {
        Assert.notEmpty(cachedWords, "The cachedWords must not be empty.");
        log.info("Loading resource ->");
        List<DefinitionsResource> resources = new ArrayList<>();

        cachedWords.forEach(o -> {
            o.getResults().forEach(r -> {
                r.getLexicalEntries().forEach(l -> {
                    l.getEntries().forEach(e -> {
                        createDefinitions(resources, r, e);
                    });
                });
            });
        });
        log.info("Loading resource <-");
        return resources;
    }

    private void createDefinitions(List<DefinitionsResource> resources, Results r, Entries e) {
        DefinitionsResource resource = new DefinitionsResource();
        Map<String, String> definitions = new HashMap<>();
        e.getSenses().forEach(s -> {
            resource.setWord(r.getId());
            definitions.put(s.getId(), s.getDefinitions().get(0));
            if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
                s.getSubsenses().forEach(ss -> {
                    definitions.put(ss.getId(), ss.getDefinitions().get(0));
                });
            }
        });
        resource.setDefinitions(definitions);
        resources.add(resource);
    }

}
