package com.tsm.cards.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.model.SetWordsCards;
import com.tsm.cards.repository.SetWordsCardsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class SetWordsCardsService {

    @Autowired
    private SetWordsCardsRepository repository;

    @Transactional
    public SetWordsCards save(final SetWordsCards setWordsCards) {
        Assert.notNull(setWordsCards, "The setWordsCards must not be null.");
        log.info("Saving setWordsCards [{}] .", setWordsCards);

        repository.save(setWordsCards);

        log.info("Saved setWordsCards [{}].", setWordsCards);
        return setWordsCards;
    }

    public List<SetWordsCards> findAll() {
        log.info("Find all setWordsCards.");

        List<SetWordsCards> sets = repository.findAll();

        log.info("Found setWordsCards [{}].", sets.size());
        return sets;
    }

}
