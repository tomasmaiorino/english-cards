package com.tsm.cards.model;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;

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
