package com.tsm.cards.definition.repository;

import com.tsm.cards.documents.SentencesDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
public interface SentencesDefinitionRepository extends BaseDefinitionRepository<SentencesDefinition, String>, MongoRepository<SentencesDefinition, String> {

    @Override
    @Query("{ 'id': ?0 }")
    Optional<SentencesDefinition> findById(final String id);

    @Override
    @Query("{$or:[{'results.lexicalEntries.entries.senses.subsenses._id':?0},{'results.lexicalEntries.entries.senses._id':?0}]}")
    Optional<SentencesDefinition> findByDefinitionId(final String definitionId);
}
