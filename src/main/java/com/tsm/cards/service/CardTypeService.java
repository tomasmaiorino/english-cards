package com.tsm.cards.service;

import static com.tsm.cards.util.ErrorCodes.CARD_TYPE_NOT_FOUND;
import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CARD_TYPE;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.CardType;
import com.tsm.cards.repository.CardTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CardTypeService {

    @Autowired
    private CardTypeRepository repository;

    @Transactional
    public CardType save(final CardType cardType) {
        Assert.notNull(cardType, "The cardType must not be null!");
        log.info("Saving cardType [{}] .", cardType);

        repository.findByName(cardType.getName()).ifPresent(c -> {
            throw new BadRequestException(DUPLICATED_CARD_TYPE);
        });
        
        repository.save(cardType);

        log.info("Saved cardType [{}].", cardType);

        return cardType;
    }

    public CardType findById(final Integer id) {
        Assert.notNull(id, "The id must not be null!");
        log.info("Finding client by token [{}] .", id);

        CardType cardType = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CARD_TYPE_NOT_FOUND));

        log.info("CardType found [{}].", cardType);

        return cardType;
    }
}
