package com.tsm.cards.definition.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.documents.Definition;

@Transactional(propagation = Propagation.MANDATORY)
public interface DefinitionRepository
    extends BaseDefinitionRepository<Definition, String>, MongoRepository<Definition, String> {

    @Override
    @Query("{ 'id': ?0 }")
    Optional<Definition> findById(final String id);

    @Override
    @Query("{$or:[{'results.lexicalEntries.entries.senses.subsenses._id':?0},{'results.lexicalEntries.entries.senses._id':?0}]}")
    Optional<Definition> findByDefinitionId(final String definitionId);
}
