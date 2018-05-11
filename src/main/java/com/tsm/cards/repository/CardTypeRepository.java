package com.tsm.cards.repository;

import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface CardTypeRepository extends IBaseRepository<CardType, Integer>, Repository<CardType, Integer> {

	@Query("SELECT ct FROM CardType ct WHERE UPPER(ct.name) = UPPER(?1)")
	Optional<CardType> findByName(final String name);

	Set<CardType> findAllByStatus(final CardTypeStatus status);
}
