package com.tsm.cards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.Card;

@Transactional(propagation = Propagation.MANDATORY)
public interface CardRepository extends IBaseRepository<Card, Integer>, Repository<Card, Integer> {

	@Query("SELECT c FROM Card c WHERE UPPER(c.imgUrl) = UPPER(?1)")
	Optional<Card> findByImgUrl(final String imgUrl);

}
