package com.tsm.cards.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.Card;

@Transactional(propagation = Propagation.MANDATORY)
public interface CardRepository extends MongoRepository<Card, Long> {

	@Query("{ 'type' : ?0 }")
	Set<Card> findByType(final String type);

}