package com.tsm.cards.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.model.Cards;
import com.tsm.cards.repository.CardsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CardsService {

    @Autowired
    private CardsRepository repository;

    @Transactional
    public Cards save(final Cards cards) {
        Assert.notNull(cards, "The cards must not be null.");
        log.info("Saving cards [{}] .", cards);

        repository.save(cards);

        log.info("Saved cards [{}].", cards);
        return cards;
    }

    public List<Cards> findByWord(final String word) {
        Assert.hasText(word, "The word must not be empty or null.");
        log.debug("Searching for cards with word [{}] .", word);

        List<Cards> cards = repository.findByWord(word);

        log.debug("Found [{}] cards.", cards);

        return cards;
    }

}
