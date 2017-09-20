package com.tsm.cards.definition.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tsm.cards.documents.Results;
import com.tsm.cards.documents.Sentence;
import com.tsm.cards.resources.DefinitionResource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SentencesBuildDefResourceService extends BaseBuildDefResourceService {

    @Value("${maxDefinitionSentencesList}")
    @Setter
    private Integer maxDefinitionSentencesList;

    @Override
    protected Integer getMaxDefinitionSize() {
        return maxDefinitionSentencesList;
    }

    @Override
    protected List<DefinitionResource> getResource(final List<Results> results) {
        List<DefinitionResource> resources = new ArrayList<>();
        results.forEach(r -> {
            List<String> sentences = new ArrayList<>();
            DefinitionResource resource = new DefinitionResource();
            resource.setWord(r.getId());
            r.getLexicalEntries().forEach(l -> {
                log.info("Sentences found [{}].",l.getSentences() != null ? l.getSentences().size() : 0);
                addSentences(sentences, l.getSentences());
            });

            resource.setSentences(sentences);
            resources.add(resource);
            log.info("Sentences found [{}].", resource.getSentences() != null ? resource.getSentences().size() : 0);
        });

        return resources;
    }

    private void addSentences(final List<String> definitions, final List<Sentence> content) {
        if (definitions.size() == maxDefinitionSentencesList || CollectionUtils.isEmpty(content)) {
            return;
        }
        content.forEach(s -> {
            if (definitions.size() == maxDefinitionSentencesList) {
                return;
            }
            definitions.add(s.getText());
        });
    }

}
