package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.repository.CardTypeRepository;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class CardTypeServiceTest {

	private static final Integer CARD_TYPE_ID = 1;
	private static final Integer CARD_TYPE_ID_DUPLICATED = 2;

	@InjectMocks
	private CardTypeService service;

	@Mock
	private CardTypeRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullCardTypeGiven_ShouldThrowException() {
		// Set up
		CardType cardType = null;

		// Do test
		try {
			service.save(cardType);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_DuplicatedNameGiven_ShouldThrowException() {
		// Set up
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByName(cardType.getName())).thenReturn(Optional.of(cardType));

		// Do test
		try {
			service.save(cardType);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByName(cardType.getName());
		verify(mockRepository, times(0)).save(cardType);
	}

	@Test
	public void save_ValidCardTypeGiven_ShouldCreateClient() {
		// Set up
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByName(cardType.getName())).thenReturn(Optional.empty());
		when(mockRepository.save(cardType)).thenReturn(cardType);

		// Do test
		CardType result = service.save(cardType);

		// Assertions
		verify(mockRepository).findByName(cardType.getName());

		assertNotNull(result);
		assertThat(result, is(cardType));
	}

	@Test
	public void update_NullOriginGiven_ShouldThrowException() {
		// Set up
		CardType origin = null;
		CardType model = CardTypeTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_NewOriginGiven_ShouldThrowException() {
		// Set up
		CardType origin = CardTypeTestBuilder.buildModel();
		CardType model = CardTypeTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_NullModelGiven_ShouldThrowException() {
		// Set up
		CardType origin = CardTypeTestBuilder.buildModel();
		CardType model = null;

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_DuplicatedNameGiven_ShouldThrowException() {
		// Set up
		CardType origin = CardTypeTestBuilder.buildModel(CARD_TYPE_ID);
		CardType model = CardTypeTestBuilder.buildModel();
		CardType duplicated = CardTypeTestBuilder.buildModel(CARD_TYPE_ID_DUPLICATED);

		// Expectations
		when(mockRepository.findByName(model.getName())).thenReturn(Optional.of(duplicated));

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByName(model.getName());
		verify(mockRepository, times(0)).save(origin);
	}

	@Test
	public void update_ValidObjectsGiven_ShouldUpdate() {
		// Set up
		CardType origin = CardTypeTestBuilder.buildModel(CARD_TYPE_ID);
		CardType model = CardTypeTestBuilder.buildModel();
		model.setName("new Name");

		// Expectations
		when(mockRepository.findByName(model.getName())).thenReturn(Optional.of(origin));
		when(mockRepository.save(origin)).thenReturn(origin);

		// Do test
		CardType result = service.update(origin, model);

		// Assertions
		verify(mockRepository).findByName(model.getName());

		assertNotNull(result);
		assertThat(result, is(origin));
	}

	@Test
	public void findById_NullIdGiven_ShouldThrowException() {
		// Set up
		Integer cardTypeId = null;

		// Do test
		try {
			service.findById(cardTypeId);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findById_NotFoundCardTypeGiven_ShouldThrowException() {
		// Set up
		Integer cardTypeId = 1;

		// Expectations
		when(mockRepository.findById(cardTypeId)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findById(cardTypeId);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findById(cardTypeId);
	}

	@Test
	public void findById_FoundCardTypeGiven_ShouldReturnContent() {
		// Set up
		Integer cardTypeId = 1;
		CardType cardType = CardTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findById(cardTypeId)).thenReturn(Optional.of(cardType));

		// Do test
		CardType result = service.findById(cardTypeId);

		// Assertions
		verify(mockRepository).findById(cardTypeId);

		assertNotNull(result);
		assertThat(result, is(cardType));
	}

	//

	@Test
	public void findAllByStatus_NullIdGiven_ShouldThrowException() {
		// Set up
		CardTypeStatus status = null;

		// Do test
		try {
			service.findAllByStatus(status);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findAllByStatus_NotFoundCardTypeGiven_ShouldThrowException() {
		// Set up
		CardTypeStatus status = CardTypeStatus.ACTIVE;

		// Expectations
		when(mockRepository.findAllByStatus(status)).thenReturn(Collections.emptySet());

		// Do test
		Set<CardType> result = service.findAllByStatus(status);

		// Assertions
		verify(mockRepository).findAllByStatus(status);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void findAllByStatus_FoundCardTypeGiven_ShouldReturnContent() {
		// Set up
		CardTypeStatus status = CardTypeStatus.ACTIVE;
		CardType cardType = CardTypeTestBuilder.buildModel();
		Set<CardType> cardTypes = new HashSet<>();
		cardTypes.add(cardType);

		// Expectations
		when(mockRepository.findAllByStatus(status)).thenReturn(cardTypes);

		// Do test
		Set<CardType> result = service.findAllByStatus(status);

		// Assertions
		verify(mockRepository).findAllByStatus(status);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
		assertThat(result.contains(cardType), is(true));
	}

}
