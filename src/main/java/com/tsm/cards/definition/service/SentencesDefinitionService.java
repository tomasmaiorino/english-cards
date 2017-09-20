package com.tsm.cards.definition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.definition.repository.SentencesDefinitionRepository;
import com.tsm.cards.documents.SentencesDefinition;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class SentencesDefinitionService extends BaseDefinitionService<SentencesDefinition, String> {

    @Autowired
    private SentencesDefinitionRepository repository;

    @Override
    public SentencesDefinitionRepository getRepository() {
        return repository;
    }

    @Override
    public String getServiceName() {
        return OxfordService.SENTENCES;
    }

}
