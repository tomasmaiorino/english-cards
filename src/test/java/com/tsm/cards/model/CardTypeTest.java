package com.tsm.cards.model;

import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

	@Test(expected = IllegalArgumentException.class)
	public void setImgUrl_NullImgUrlGiven_ShouldThrowException() {
		// Set up
		Set<Card> cards = Collections.emptySet();

		// Do test
		CardType cardType = new CardType();
		cardType.setCards(cards);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addCard_NullCardGiven_ShouldThrowException() {
		// Set up
		Card card = null;
		CardType cardType = new CardType();

		// Do test
		cardType.addCard(card);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setCardTypeStatus_NullCardTypeStatusGiven_ShouldThrowException() {
		// Set up
		CardType cardType = new CardType();
		CardTypeStatus cardTypeStatus = null;

		// Do test
		cardType.setCardTypeStatus(cardTypeStatus);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		Card card = CardTestBuilder.buildModel();
		String name = CardTypeTestBuilder.getName();
		CardType cardType = CardTypeTestBuilder.buildModel(name);
		cardType.addCard(card);

		// Assertions
		assertNotNull(cardType);
		assertThat(cardType, allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)),
				hasProperty("imgUrl", nullValue())));
		assertThat(cardType.getCards().contains(card), is(true));
	}

}
