package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;
import org.apache.commons.lang3.RandomUtils;
import com.tsm.cards.model.Card;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.resources.ContentTypeResource;

public class ContentTypeTestBuilder {

	public static final String LARGE_NAME = random(31, true, true);
	public static final String SMALL_NAME = random(1, true, true);
	public static final String VALID_NAME = random(30, true, true);
	public static final Card CARD = CardTestBuilder.buildModel();

	public static ContentType buildModel() {
		return buildModel(VALID_NAME, CARD);
	}

	public static ContentType buildModel(final String name) {
		return buildModel(name, null);
	}

	public static ContentType buildModel(final String name, final Card card) {
		ContentType cardType = new ContentType();
		cardType.setName(name);
//		if (card != null) {
//			cardType.setCards(Collections.singleton(card));
//		}
		cardType.setStatus(getContentTypeStatus());
		return cardType;
	}

	public static ContentTypeStatus getContentTypeStatus() {
		return ContentTypeStatus.values()[RandomUtils.nextInt(0, ContentTypeStatus.values().length - 1)];
	}

	public static ContentTypeResource buildResource() {
		ContentTypeResource resource = new ContentTypeResource();
		resource.setName(VALID_NAME);
		resource.setStatus(getStatus());
		return resource;
	}

	public static String getStatus() {
		return getContentTypeStatus().name();
	}

	public static String getName() {
		return random(30, true, true);
	}

	public static String getImgUrl() {
		return "http://" + random(20, true, false) + ".com";
	}
}
