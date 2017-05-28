package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Definition;
import com.tsm.cards.model.Entries;
import com.tsm.cards.model.Results;
import com.tsm.cards.resources.DefinitionResource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildDefinitionsResourceService {

    //TODO refactor these methods
    public List<DefinitionResource> loadResource(List<Definition> cachedWords) {
        Assert.notEmpty(cachedWords, "The cachedWords must not be empty.");
        log.info("Loading resource ->");
        List<DefinitionResource> resources = new ArrayList<>();

        List<Results> results = cachedWords.stream().flatMap(c -> c.getResults().stream()).filter(r -> r.getLexicalEntries() != null)
            .collect(Collectors.toList());

        results.forEach(r -> {
            Map<String, String> definitions = new HashMap<>();
            DefinitionResource resource = new DefinitionResource();
            r.getLexicalEntries().forEach(l -> {
                resource.setWord(r.getId());
                if (l.getEntries() != null && !l.getEntries().isEmpty()) {
                    l.getEntries().forEach(e -> {
                        createDefinitions(e, definitions);
                    });
                }
            });
            resource.setDefinitions(definitions);
            resources.add(resource);
        });
        log.info("Loading resource <-");
        return resources.stream().sorted((r1, r2) -> r1.getWord().compareTo(r2.getWord())).collect(Collectors.toList());
    }

    private void createDefinitions(Entries e, Map<String, String> definitions) {
        if (e.getSenses() != null && !e.getSenses().isEmpty()) {
            e.getSenses().forEach(s -> {
                if (s.getDefinitions() != null && !s.getDefinitions().isEmpty()) {
                    definitions.put(s.getId(), s.getDefinitions().get(0));
                }
                if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
                    s.getSubsenses().forEach(ss -> {
                        definitions.put(ss.getId(), ss.getDefinitions().get(0));
                    });
                }
            });
        }
    }
}
