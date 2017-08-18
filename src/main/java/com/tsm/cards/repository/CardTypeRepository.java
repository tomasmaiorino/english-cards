package com.tsm.cards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.CardType;

@Transactional(propagation = Propagation.MANDATORY)
public interface CardTypeRepository extends Repository<CardType, Integer> {

    CardType save(CardType cardType);
    
    Optional<CardType> findById(final Integer id);
    
    @Query("SELECT ct FROM CardType ct WHERE UPPER(ct.name) = UPPER(?1)")
    Optional<CardType> findByName(final String name);
}
