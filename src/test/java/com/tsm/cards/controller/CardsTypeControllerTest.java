package com.tsm.cards.controller;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.parser.CardTypeParser;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.service.CardTypeService;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class CardsTypeControllerTest {

	@Mock
	private CardTypeService mockService;

	@Mock
	private CardTypeParser mockParser;

	@InjectMocks
	private CardsTypeController controller;

	private static final String FIND_ALL_QUERY = "";

	@Mock
	private Validator validator;

	private Integer CARD_TYPE_ID = 1;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidCardTypeResourceGiven_ShouldThrowException() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(mockService, mockParser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void save_DuplicatedCardTypeResourceGiven_ShouldSaveCardType() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(cardType);
		when(mockService.save(cardType)).thenThrow(BadRequestException.class);

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(cardType);
		verify(mockParser).toModel(resource);

	}

	@Test
	public void save_ValidCardTypeResourceGiven_ShouldSaveCardType() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(cardType);
		when(mockService.save(cardType)).thenReturn(cardType);
		when(mockParser.toResource(cardType)).thenReturn(resource);

		// Do test
		CardTypeResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(cardType);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(cardType);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void update_InvalidCardTypeResourceGiven_ShouldThrowException() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.update(CARD_TYPE_ID, resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(mockService, mockParser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void update_NotFoundCardTypeResourceGiven_ShouldThrowException() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockService.findById(CARD_TYPE_ID)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			controller.update(CARD_TYPE_ID, resource);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).findById(CARD_TYPE_ID);
		verify(mockParser, times(0)).toResource(any(CardType.class));
	}

	@Test
	public void udpate_ValidCardTypeResourceGiven_ShouldSaveCardType() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();
		CardType cardType = CardTypeTestBuilder.buildModel();
		CardType origin = CardTypeTestBuilder.buildModel();
		ReflectionTestUtils.setField(origin, "id", CARD_TYPE_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockService.findById(CARD_TYPE_ID)).thenReturn(origin);
		when(mockParser.toModel(resource)).thenReturn(cardType);
		when(mockService.update(origin, cardType)).thenReturn(origin);
		when(mockParser.toResource(origin)).thenReturn(resource);

		// Do test
		CardTypeResource result = controller.update(CARD_TYPE_ID, resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).update(origin, cardType);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(origin);
		verify(mockService).findById(CARD_TYPE_ID);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void findById_NotFoundCardTypeGiven_ShouldThrowException() {

		// Expectations
		when(mockService.findById(CARD_TYPE_ID)).thenThrow(new ResourceNotFoundException(""));

		// Do test
		try {
			controller.findById(CARD_TYPE_ID);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockService).findById(CARD_TYPE_ID);
		verifyZeroInteractions(mockParser);
	}

	@Test
	public void findById_FoundCardTypeGiven_ShouldReturnContent() {
		// Set up
		CardTypeResource resource = CardTypeTestBuilder.buildResource();
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(mockService.findById(CARD_TYPE_ID)).thenReturn(cardType);
		when(mockParser.toResource(cardType)).thenReturn(resource);

		// Do test
		CardTypeResource result = controller.findById(CARD_TYPE_ID);

		// Assertions
		verify(mockService).findById(CARD_TYPE_ID);
		verify(mockParser).toResource(cardType);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void findAll_NotFoundCardsTypeGiven_ShouldReturnEmptyContent() {
		// Set up
		Set<CardType> cardsType = Collections.emptySet();

		// Expectations
		when(mockService.findAllByStatus(CardTypeStatus.ACTIVE)).thenReturn(cardsType);

		// Do test
		Set<CardTypeResource> result = controller.findAll(FIND_ALL_QUERY);

		// Assertions
		verify(mockService).findAllByStatus(CardTypeStatus.ACTIVE);
		verifyZeroInteractions(mockParser);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));

	}

	@Test
	public void findAll_FoundCardsTypeGiven_ShouldReturnContent() {
		// Set up
		Set<CardType> cardsType = new HashSet<>(1);
		CardType cardType = CardTypeTestBuilder.buildModel();
		cardsType.add(cardType);

		Set<CardTypeResource> resources = new HashSet<>(1);
		CardTypeResource resource = CardTypeTestBuilder.buildResource();
		resources.add(resource);

		// Expectations
		when(mockService.findAllByStatus(CardTypeStatus.ACTIVE)).thenReturn(cardsType);
		when(mockParser.toResources(cardsType)).thenReturn(resources);

		// Do test
		Set<CardTypeResource> result = controller.findAll(FIND_ALL_QUERY);

		// Assertions
		verify(mockService).findAllByStatus(CardTypeStatus.ACTIVE);
		verify(mockParser).toResources(cardsType);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
	}

}
