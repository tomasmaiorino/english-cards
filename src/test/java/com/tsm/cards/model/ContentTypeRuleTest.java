package com.tsm.cards.model;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.util.ContentTypeRuleTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeRuleTest {

	private String attribute;
	private String rule;
	private ContentType contentType;

	private static final Integer CONTENT_TYPE_ID = RandomUtils.nextInt(1, 100);

	@Before
	public void setUp() {
		attribute = random(100, true, true);
		rule = random(100, true, true);
		contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullAttributeGiven_ShouldThrowException() {
		// Set up
		attribute = null;

		// Do test
		buildContentTypeRule();

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyAttributeGiven_ShouldThrowException() {
		// Set up
		attribute = "";

		// Do test
		buildContentTypeRule();

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullRuleGiven_ShouldThrowException() {
		// Set up
		rule = null;

		// Do test
		buildContentTypeRule();

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullContentTypeGiven_ShouldThrowException() {
		// Set up
		contentType = null;

		// Do test
		buildContentTypeRule();

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyRuleGiven_ShouldThrowException() {
		// Set up
		rule = "";

		// Do test
		buildContentTypeRule();

	}

	private ContentTypeRule buildContentTypeRule() {
		return ContentTypeRuleTestBuilder.buildModel(rule, contentType, attribute);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		ContentTypeRule contentTypeRule = buildContentTypeRule();

		// Assertions
		assertNotNull(contentTypeRule);
		assertThat(contentTypeRule, allOf(hasProperty("id", nullValue()), hasProperty("attribute", is(attribute)),
				hasProperty("contentType", is(contentType)), hasProperty("rule", is(rule))));
	}

}
