package com.tsm.cards.definition.repository;

import com.tsm.cards.documents.SetWordsCards;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
public interface SetWordsCardsRepository extends MongoRepository<SetWordsCards, Long> {

    @Query("{ 'known_word.id' : ?0 }")
    List<SetWordsCards> findByKnownWord(final String id);

    List<SetWordsCards> findAll();
}
