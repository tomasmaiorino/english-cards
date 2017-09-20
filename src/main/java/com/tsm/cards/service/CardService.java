package com.tsm.cards.service;

import static com.tsm.cards.util.ErrorCodes.CARD_NOT_FOUND;
import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CARD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
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
        Assert.notNull(card, "The card must not be null!");
        log.info("Saving card [{}] .", card);

        repository.findByImgUrl(card.getImgUrl()).ifPresent(c -> {
            throw new BadRequestException(DUPLICATED_CARD);
        });

        repository.save(card);

        log.info("Saved card [{}].", card);

        return card;
    }

    @Transactional
    public Card update(final Card origin, final Card model) {
        Assert.notNull(origin, "The origin must not be null!");
        Assert.notNull(origin.getId(), "The origin must not be new!");
        Assert.notNull(model, "The model must not be null!");
        log.info("Updating card origin[{}] to model [{}].", origin, model);

        repository.findByImgUrl(model.getImgUrl()).ifPresent(c -> {
            if (!c.getId().equals(origin.getId())) {
                throw new BadRequestException(DUPLICATED_CARD);
            }
        });

        merge(origin, model);

        repository.save(origin);

        log.info("Updated card [{}].", origin);

        return origin;
    }

    private void merge(final Card origin, final Card model) {
        origin.setCardStatus(model.getStatus());
        origin.setCardType(model.getCardType());
        origin.setImgUrl(model.getImgUrl());
        origin.setName(model.getName());
    }

    public Card findById(final Integer id) {
        Assert.notNull(id, "The id must not be null!");
        log.info("Finding card by id [{}] .", id);

        Card card = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CARD_NOT_FOUND));

        log.info("Card found [{}].", card);

        return card;
    }
}
