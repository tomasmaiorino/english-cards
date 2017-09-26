package com.tsm.cards.parser;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CardTypeParserTest {

	@InjectMocks
	private CardTypeParser parser;

	@Mock
	private CardParser cardParser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		CardTypeResource resource = null;

		// Do test
		parser.toModel(resource);
	}

	@Test
	public void toModel_ValidResourceGiven_ShouldCreateCardModel() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();

		// Do test
		CardType result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName()))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullCardGiven_ShouldThrowException() {
		// Set up
		CardType cardType = null;

		// Do test
		parser.toResource(cardType);
	}

	@Test
	public void toResource_ValidCardParserGiven_ShouldCreateResourceModel() {
		// Set up
		CardType cardType = CardTypeTestBuilder.buildModel();
		ReflectionTestUtils.setField(cardType, "id", 1);
		CardResource cardResource = CardTestBuilder.buildResource();

		// Expectations
		when(cardParser.toResource(anyObject())).thenReturn(cardResource);

		// Do test
		CardTypeResource result = parser.toResource(cardType);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", is(cardType.getId())), hasProperty("name", is(cardType.getName()))));

	}
}
