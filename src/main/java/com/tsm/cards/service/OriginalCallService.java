package com.tsm.cards.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.repository.OriginalCallRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class OriginalCallService {

    @Autowired
    private OriginalCallRepository repository;

    @Transactional
    public OriginalCall save(OriginalCall originalCall) {
        Assert.notNull(originalCall, "The originalCall must not be null.");
        log.info("Saving originalCall [{}] .", originalCall);

        repository.save(originalCall);

        log.info("Saved originalCall [{}].", originalCall);
        return originalCall;
    }

    public OriginalCall findByDefinitionId(final String definitionId) {
        Assert.notNull(definitionId, "The definitionId must not be null.");
        log.info("Searching for original call by definitions id [{}] .", definitionId);

        OriginalCall originalCall = repository.findByDefinitionId(definitionId).orElse(null);

        log.info("Found cache original call? [{}] l.", originalCall);

        return originalCall;
    }

    public OriginalCall findById(final String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for original call cache [{}] .", word);

        OriginalCall originalCall = findOptionalOriginalCallById(word)
            .orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found cache [{}] originalCall.", originalCall);

        return originalCall;

    }

    public Optional<OriginalCall> findOptionalOriginalCallById(final String word) {
        Assert.notNull(word, "The id must not be null.");
        log.info("Searching for original call cache [{}] .", word);

        return repository.findById(word);
    }

}
