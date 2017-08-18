package com.tsm.cards.model;

import java.util.Collections;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class CardTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void build_NullHostGiven_ShouldThrowException() {
        // Set up
        String name = null;

        // Do test
        CardType cardType = new CardType();
        cardType.setName(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullCardGiven_ShouldThrowException() {
        // Set up
        Set<Card> cards = null;

        // Do test
        CardType cardType = new CardType();
        cardType.setCards(cards);
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptyCardGiven_ShouldThrowException() {
        // Set up
        Set<Card> cards = Collections.emptySet();

        // Do test
        CardType cardType = new CardType();
        cardType.setCards(cards);
    }
}
