package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Collections;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardTypeResource;

public class CardTypeTestBuilder {

    public static final String LARGE_NAME = random(31, true, true);
    public static final String SMALL_NAME = random(1, true, true);
    public static final String VALID_NAME = random(30, true, true);
    public static final Card CARD = CardTestBuilder.buildModel();

    public static CardType buildModel() {
        return buildModel(VALID_NAME, CARD);
    }

    public static CardType buildModel(final String name, final Card card) {
        CardType cardType = new CardType();
        cardType.setName(name);
        cardType.setCards(Collections.singleton(card));
        return cardType;
    }

    public static CardTypeResource buildResource() {
        CardTypeResource resource = new CardTypeResource();
        resource.setName(VALID_NAME);
        return resource;
    }
}
