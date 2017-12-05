package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Collections;

import org.apache.commons.lang3.RandomUtils;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.resources.CardTypeResource;

public class CardTypeTestBuilder {

	public static final String LARGE_NAME = random(31, true, true);
	public static final String SMALL_NAME = random(1, true, true);
	public static final String VALID_NAME = random(30, true, true);
	public static final Card CARD = CardTestBuilder.buildModel();

	public static CardType buildModel() {
		return buildModel(VALID_NAME, CARD);
	}

	public static CardType buildModel(final String name) {
		return buildModel(name, null);
	}

	public static CardType buildModel(final String name, final Card card) {
		CardType cardType = new CardType();
		cardType.setName(name);
		if (card != null) {
			cardType.setCards(Collections.singleton(card));
		}
		return cardType;
	}

	public static CardTypeResource buildResource() {
		CardTypeResource resource = new CardTypeResource();
		resource.setName(VALID_NAME);
		resource.setStatus(getStatus());
		return resource;
	}

	public static String getStatus() {
		return CardTypeStatus.values()[RandomUtils.nextInt(0, CardTypeStatus.values().length - 1)].name();
	}

	public static String getName() {
		return random(30, true, true);
	}

	public static String getImgUrl() {
		return "http://" + random(20, true, false) + ".com";
	}
}
