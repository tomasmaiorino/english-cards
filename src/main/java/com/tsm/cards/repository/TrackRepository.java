package com.tsm.cards.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.Track;

@Transactional(propagation = Propagation.MANDATORY)
public interface TrackRepository extends MongoRepository<Track, Long> {
	
    @Query("{ 'id': ?0 }")
    Optional<Track> findById(final String id);

}
