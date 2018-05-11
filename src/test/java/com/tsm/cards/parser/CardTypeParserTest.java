package com.tsm.cards.parser;

import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

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
		resource.setImgUrl(CardTypeTestBuilder.getImgUrl());
		// Do test
		CardType result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
						hasProperty("status", is(CardTypeStatus.valueOf(resource.getStatus()))),
						hasProperty("imgUrl", is(resource.getImgUrl()))));

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
		cardType.setImgUrl(CardTypeTestBuilder.getImgUrl());

		ReflectionTestUtils.setField(cardType, "id", 1);
		CardResource cardResource = CardTestBuilder.buildResource();

		// Expectations
		when(cardParser.toResource(anyObject())).thenReturn(cardResource);

		// Do test
		CardTypeResource result = parser.toResource(cardType);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", is(cardType.getId())), hasProperty("name", is(cardType.getName())),
				hasProperty("status", is(cardType.getStatus().name())), hasProperty("imgUrl", is(result.getImgUrl()))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResources_EmptyCardTypes_ShouldThrowException() {
		// Set up
		Set<CardType> cardsType = Collections.emptySet();

		// Do test
		parser.toResources(cardsType);
	}

	@Test
	public void toResources_ValidCardTypes_ShouldReturnResources() {
		// Set up
		Set<CardType> cardsType = new HashSet<>();
		CardType cardType = CardTypeTestBuilder.buildModel();
		;
		cardsType.add(cardType);

		// Do test
		Set<CardTypeResource> result = parser.toResources(cardsType);

		// Assertions
		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
		assertThat(result.iterator().next().getName(), is(cardType.getName()));
	}
}