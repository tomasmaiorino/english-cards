package com.tsm.cards.definition.service;

import com.tsm.cards.documents.Entries;
import com.tsm.cards.documents.Results;
import com.tsm.cards.documents.Synonyms;
import com.tsm.cards.resources.DefinitionResource;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SynonymsBuildDefResourceService extends BaseBuildDefResourceService {

    @Value("${maxDefinitionSynonymsList}")
    @Setter
    private Integer maxDefinitionSynonymsList;

    @Override
    protected Integer getMaxDefinitionSize() {
        return maxDefinitionSynonymsList;
    }

    @Override
    protected List<DefinitionResource> getResource(final List<Results> results) {
        List<DefinitionResource> resources = new ArrayList<>();
        results.forEach(r -> {
            Map<String, String> synonyms = new HashMap<>();
            DefinitionResource resource = new DefinitionResource();
            r.getLexicalEntries().forEach(l -> {
                resource.setWord(r.getId());
                if (l.getEntries() != null && !l.getEntries().isEmpty()) {
                    l.getEntries().forEach(e -> {
                        createDefinitions(e, synonyms);
                    });
                }
            });

            resource.setSynonyms(synonyms);
            resources.add(resource);
        });

        return resources;
    }

    private void createDefinitions(Entries e, Map<String, String> definitions) {
        if (e.getSenses() != null && !e.getSenses().isEmpty()) {
            e.getSenses().forEach(s -> {
                if (!CollectionUtils.isEmpty(s.getSynonyms())) {
                    addSynonyms(definitions, s.getSynonyms());
                }
                if (!CollectionUtils.isEmpty(s.getSubsenses())) {
                    s.getSubsenses().forEach(ss -> {
                        addSynonyms(definitions, ss.getSynonyms());
                    });
                }
            });
        }
    }

    private void addSynonyms(Map<String, String> definitions, List<Synonyms> content) {
        if (definitions.size() == maxDefinitionSynonymsList || CollectionUtils.isEmpty(content)) {
            return;
        }
        content.forEach(s -> {
            if (definitions.size() == maxDefinitionSynonymsList) {
                return;
            }
            definitions.put(s.getId(), s.getText());
        });
    }

}
