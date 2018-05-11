package com.tsm.cards.parser;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class CardParserTest {

    private CardParser parser = new CardParser();

    @Test(expected = IllegalArgumentException.class)
    public void toModel_NullResourceGiven_ShouldThrowException() {
        // Set up
        CardResource resource = null;
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Do test
        parser.toModel(resource, cardType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toModel_NullCardTypeGiven_ShouldThrowException() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        CardType cardType = null;

        // Do test
        parser.toModel(resource, cardType);
    }

    @Test
    public void toModel_ValidResourceGiven_ShouldCreateCardModel() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Do test
        Card result = parser.toModel(resource, cardType);

        // Assertions
        assertNotNull(result);
        assertThat(result, allOf(
            hasProperty("id", nullValue()),
            hasProperty("name", is(resource.getName())),
            hasProperty("imgUrl", is(resource.getImgUrl())),
            hasProperty("cardType", is(cardType)),
            hasProperty("status", is(CardStatus.valueOf(resource.getStatus())))));

    }

    @Test(expected = IllegalArgumentException.class)
    public void toResource_NullCardGiven_ShouldThrowException() {
        // Set up
        Card client = null;

        // Do test
        parser.toResource(client);
    }

    @Test
    public void toResource_ValidCardParserGiven_ShouldCreateResourceModel() {
        // Set up
        Card card = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(card, "id", 1);

        CardType cardType = CardTypeTestBuilder.buildModel();
        card.setCardType(cardType);

        // Do test
        CardResource result = parser.toResource(card);

        // Assertions
        assertNotNull(result);
        assertThat(result, allOf(
            hasProperty("id", is(card.getId())),
            hasProperty("name", is(card.getName())),
            hasProperty("imgUrl", is(card.getImgUrl())),
            hasProperty("cardType", is(card.getCardType().getId())),
            hasProperty("status", is(CardStatus.ACTIVE.name()))));

    }
}
