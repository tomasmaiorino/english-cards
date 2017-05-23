package com.tsm.cards.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.OriginalCall;

@Transactional(propagation = Propagation.MANDATORY)
public interface OriginalCallRepository extends MongoRepository<OriginalCall, Long> {

    @Query("{ 'id': ?0 }")
    Optional<OriginalCall> findById(final String id);

    @Query("{$or:[{'results.lexicalEntries.entries.senses.subsenses._id':?0},{'results.lexicalEntries.entries.senses._id':?0}]}")
    Optional<OriginalCall> findByDefinitionId(final String definitionId);
}
