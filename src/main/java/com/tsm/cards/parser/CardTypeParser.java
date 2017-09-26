package com.tsm.cards.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardTypeResource;

@Component
public class CardTypeParser {

    @Autowired
    private CardParser cardParser;

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
        if (!CollectionUtils.isEmpty(cardType.getCards())) {
            cardType.getCards().forEach(c -> {
                resource.addCards(cardParser.toResource(c));
            });
        }
        return resource;

    }

}
