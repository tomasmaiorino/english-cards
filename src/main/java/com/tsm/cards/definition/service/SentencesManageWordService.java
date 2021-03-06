package com.tsm.cards.definition.service;

import com.tsm.cards.documents.SentencesDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentencesManageWordService extends BaseManageWordService<SentencesDefinition> {

    @Autowired
    private SentencesDefinitionService sentencesDefinitionService;

    @Autowired
    private SentencesOxfordService oxfordService;

    @Override
    protected String getDefinitionServiceName() {
        return OxfordService.SENTENCES;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected BaseDefinitionService getDefinitionService() {
        return sentencesDefinitionService;
    }

    @Override
    protected OxfordService<SentencesDefinition> getOxfordService() {
        return oxfordService;
    }

}
