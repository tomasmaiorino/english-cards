package com.tsm.cards.parser;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardResource;

@Component
public class CardParser {

    public Card toModel(final CardResource resource, final CardType cardType) {
        Assert.notNull(cardType, "The cardType must not be null!");
        Assert.notNull(resource, "The resource must not be null!");
        Card card = new Card();
        card.setCardStatus(CardStatus.valueOf(resource.getStatus()));
        card.setCardType(cardType);
        card.setImgUrl(resource.getImgUrl());
        card.setName(resource.getName());
        return card;
    }

    public CardResource toResource(final Card card) {
        Assert.notNull(card, "The card must not be null!");
        CardResource cardResource = new CardResource();
        cardResource.setId(card.getId());
        cardResource.setCardType(card.getCardType().getId());
        cardResource.setImgUrl(card.getImgUrl());
        cardResource.setName(card.getName());
        cardResource.setStatus(card.getStatus().name());
        return cardResource;
    }

}
