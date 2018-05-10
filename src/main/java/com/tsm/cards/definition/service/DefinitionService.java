package com.tsm.cards.definition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.definition.repository.BaseDefinitionRepository;
import com.tsm.cards.definition.repository.DefinitionRepository;
import com.tsm.cards.documents.Definition;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class DefinitionService extends BaseDefinitionService<Definition, String> {

    @Autowired
    private DefinitionRepository repository;

    @Override
    public String getServiceName() {
        return OxfordService.DEFINITIONS;
    }

    @Override
    protected BaseDefinitionRepository<Definition, String> getRepository() {
        return repository;
    }

}
