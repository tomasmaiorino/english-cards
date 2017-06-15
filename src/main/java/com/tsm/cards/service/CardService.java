package com.tsm.cards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.model.Card;
import com.tsm.cards.repository.CardRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CardService {

	@Autowired
	private CardRepository repository;

	@Transactional
	public Card save(final Card card) {
		Assert.notNull(card, "The card must not be null.");
		log.info("Saving card [{}] .", card);

		repository.save(card);

		log.info("Saved card [{}].", card);
		return card;
	}

}
