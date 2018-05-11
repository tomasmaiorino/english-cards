package com.tsm.cards.definition.service;

import com.tsm.cards.documents.Definition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefinitionManageWordService extends BaseManageWordService<Definition> {

    @Autowired
    private DefinitionService definitionSynonymsService;

    @Autowired
    private OxfordService<Definition> oxfordService;

    @Override
    protected String getDefinitionServiceName() {
        return OxfordService.DEFINITIONS;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected BaseDefinitionService getDefinitionService() {
        return definitionSynonymsService;
    }

    @Override
    protected OxfordService<Definition> getOxfordService() {
        return oxfordService;
    }

}
