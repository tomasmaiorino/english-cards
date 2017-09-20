package com.tsm.cards.definition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsm.cards.documents.SynonymsDefinition;

@Service
public class SynonymsManageWordService extends BaseManageWordService<SynonymsDefinition> {

    @Autowired
    private SynonymsDefinitionService definitionSynonymsService;

    @Autowired
    private SynonymsOxfordService oxfordService;

    @Override
    protected String getDefinitionServiceName() {
        return OxfordService.SYNONYMS;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BaseDefinitionService getDefinitionService() {
        return definitionSynonymsService;
    }

    @Override
    protected OxfordService<SynonymsDefinition> getOxfordService() {
        return oxfordService;
    }

}
