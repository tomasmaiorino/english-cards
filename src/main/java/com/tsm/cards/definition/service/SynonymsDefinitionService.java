package com.tsm.cards.definition.service;

import com.tsm.cards.definition.repository.SynonymsDefinitionRepository;
import com.tsm.cards.documents.SynonymsDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class SynonymsDefinitionService extends BaseDefinitionService<SynonymsDefinition, String> {

    @Autowired
    private SynonymsDefinitionRepository repository;

    @Override
    public SynonymsDefinitionRepository getRepository() {
        return repository;
    }

    @Override
    public String getServiceName() {
        return OxfordService.SYNONYMS;
    }

}
