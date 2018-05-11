package com.tsm.cards.model;

import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.util.CardTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class CardTest {

    private String name;
    private String imgUrl;
    private CardType cardType;
    private CardStatus status;

    @Before
    public void setUp() {
        name = random(100, true, true);
        imgUrl = random(100, true, true);
        status = CardStatus.ACTIVE;
        cardType = CardTestBuilder.buildCardType();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullNameGiven_ShouldThrowException() {

        // Set up
        name = null;

        // Do test
        buildCard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptyNameGiven_ShouldThrowException() {

        // Set up
        name = "";

        // Do test
        buildCard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullImgUrlGiven_ShouldThrowException() {

        // Set up
        imgUrl = null;

        // Do test
        buildCard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_EmptyImgUrlTokenGiven_ShouldThrowException() {

        // Set up
        imgUrl = "";

        // Do test
        buildCard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullCardTypeGiven_ShouldThrowException() {

        // Set up
        cardType = null;

        // Do test
        buildCard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_NullStatusGiven_ShouldThrowException() {

        // Set up
        status = null;

        // Do test
        buildCard();
    }

    private Card buildCard() {
        return CardTestBuilder.buildModel(name, imgUrl, cardType, status);
    }

    @Test
    public void build_AllValuesGiven_AllValuesShouldSet() {
        // Set up
        Card result = buildCard();

        // Assertions
        assertNotNull(result);
        assertThat(result,
            allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)), hasProperty("imgUrl", is(imgUrl)),
                hasProperty("cardType", is(cardType)), hasProperty("status", is(status))));
    }

}
