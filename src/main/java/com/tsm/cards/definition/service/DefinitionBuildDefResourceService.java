package com.tsm.cards.definition.service;

import com.tsm.cards.documents.Entries;
import com.tsm.cards.documents.Results;
import com.tsm.cards.resources.DefinitionResource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DefinitionBuildDefResourceService extends BaseBuildDefResourceService {

    @Value("${maxDefinitionListSize}")
    @Setter
    private Integer maxDefinitionListSize;

    @Override
    protected Integer getMaxDefinitionSize() {
        return maxDefinitionListSize;
    }

    @Override
    protected List<DefinitionResource> getResource(final List<Results> results) {
        List<DefinitionResource> resources = new ArrayList<>();
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
            log.info("Definitions found [{}].", resource.getSentences() != null ? resource.getSentences().size() : 0);
        });

        return resources;
    }

    private void createDefinitions(final Entries e, final Map<String, String> definitions) {
        if (e.getSenses() != null && !e.getSenses().isEmpty()) {
            e.getSenses().forEach(s -> {
                if (s.getDefinitions() != null && !s.getDefinitions().isEmpty()) {
                    addDefinition(definitions, s.getId(), s.getDefinitions());
                }
                if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
                    s.getSubsenses().forEach(ss -> {
                        addDefinition(definitions, ss.getId(), ss.getDefinitions());
                    });
                }
            });
        }
    }

    private void addDefinition(final Map<String, String> definitions, final String id, final List<String> content) {
        if (definitions.size() == maxDefinitionListSize || content == null || content.isEmpty()) {
            return;
        }
        definitions.put(id, content.get(0));
    }

}
