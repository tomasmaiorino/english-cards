package com.tsm.cards.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.Cards;


@Transactional(propagation = Propagation.MANDATORY)
public interface CardsRepository extends MongoRepository<Cards, Long> {

    @Query("{ 'word': ?0 }")
    List<Cards> findByWord(String word);
    
}
