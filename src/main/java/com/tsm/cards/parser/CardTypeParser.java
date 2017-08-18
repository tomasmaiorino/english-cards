package com.tsm.cards.parser;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardTypeResource;

@Component
public class CardTypeParser {

    public CardType toModel(final CardTypeResource resource) {
        Assert.notNull(resource, "The resource must not be null!");
        CardType cardType = new CardType();
        cardType.setName(resource.getName());
        return cardType;
    }

    public CardTypeResource toResource(final CardType cardType) {
        Assert.notNull(cardType, "The cardType must not be null!");
        CardTypeResource resource = new CardTypeResource();
        resource.setName(cardType.getName());
        resource.setId(cardType.getId());
        return resource;

    }

}
