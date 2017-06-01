package com.tsm.cards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.KnownWordRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class KnownWordService {

    @Autowired
    private KnownWordRepository repository;

    public KnownWord findByWord(final String word) {
        Assert.hasText(word, "The word must not be empty or null.");
        log.info("Searching for known word [{}] .", word);

        KnownWord knownWord = repository.findByWord(word).orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found [{}] knownWord.", knownWord);

        return knownWord;
    }

    @Transactional
    public KnownWord save(final KnownWord knownWord) {
        Assert.notNull(knownWord, "The knownWord must not be empty or null.");
        log.debug("Saving knownWord [{}]", knownWord);

        repository.save(knownWord);

        log.debug("Saved [{}] knownWord.", knownWord);

        return knownWord;
    }

}
