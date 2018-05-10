package com.tsm.cards.definition.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.documents.TrackWord;

@Transactional(propagation = Propagation.MANDATORY)
public interface TrackWordRepository extends MongoRepository<TrackWord, Long> {

    @Query("{ 'id': ?0 }")
    Optional<TrackWord> findById(final String id);

}
