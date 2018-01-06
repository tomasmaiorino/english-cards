package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentTypeRule;
import com.tsm.cards.resources.ContentTypeRuleResource;

public class ContentTypeRuleTestBuilder {

	public static final String LARGE_NAME = random(31, true, true);
	public static final String SMALL_NAME = random(1, true, true);
	public static final String VALID_NAME = random(30, true, true);
	public static final Card CARD = CardTestBuilder.buildModel();
	public static final Integer CONTENT_TYPE_ID = 1;

	public static ContentTypeRule buildModel(final Integer id) {
		ContentTypeRule contentType = buildModel();
		ReflectionTestUtils.setField(contentType, "id", id);
		return contentType;
	}

	public static ContentTypeRule buildModel() {
		return buildModel(getRule(), ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID), getAttribute());
	}

	public static ContentTypeRule buildModel(final String rule, final ContentType contentType, final String attribute) {
		return ContentTypeRule.ContentTypeRuleBuilder.ContentTypeRule(attribute, rule, contentType).build();
	}

	public static ContentTypeRuleResource buildResource() {
		return buildResource(getAttribute(), getRule());
	}

	private static ContentTypeRuleResource buildResource(final String attribute, final String rule) {
		ContentTypeRuleResource resource = new ContentTypeRuleResource();
		resource.setAttribute(attribute);
		resource.setRule(rule);
		return resource;
	}

	public static String getAttribute() {
		return random(30, true, true);
	}

	public static String getRule() {
		return random(30, true, true);
	}
}
