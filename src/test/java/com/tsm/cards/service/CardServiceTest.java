package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Card;
import com.tsm.cards.repository.CardRepository;
import com.tsm.cards.util.CardTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class CardServiceTest {

	private static final Integer CARD_ID = 1;

	private static final Integer CARD_ID_DUPLICATED = 2;

	@InjectMocks
	private CardService service;

	@Mock
	private CardRepository repository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullCardGiven_ShouldThrowException() {
		// Set up
		Card card = null;

		// Do test
		try {
			service.save(card);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void save_DuplicatedImgUrlGiven_ShouldThrowException() {
		// Set up
		Card card = CardTestBuilder.buildModel();

		// Expectations
		when(repository.findByImgUrl(card.getImgUrl())).thenReturn(Optional.of(card));

		// Do test
		try {
			service.save(card);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(repository).findByImgUrl(card.getImgUrl());
		verify(repository, times(0)).save(card);
	}

	@Test
	public void save_ValidCardGiven_ShouldCreateClient() {
		// Set up
		Card card = CardTestBuilder.buildModel();

		// Expectations
		when(repository.findByImgUrl(card.getImgUrl())).thenReturn(Optional.empty());
		when(repository.save(card)).thenReturn(card);

		// Do test
		Card result = service.save(card);

		// Assertions
		verify(repository).findByImgUrl(card.getImgUrl());

		assertNotNull(result);
		assertThat(result, is(card));
	}

	@Test
	public void update_NullOriginGiven_ShouldThrowException() {
		// Set up
		Card origin = null;
		Card model = CardTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_NewOriginGiven_ShouldThrowException() {
		// Set up
		Card origin = CardTestBuilder.buildModel();
		Card model = CardTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_NullModelGiven_ShouldThrowException() {
		// Set up
		Card origin = CardTestBuilder.buildModel();
		Card model = null;

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_DuplicatedNameGiven_ShouldThrowException() {
		// Set up
		Card origin = CardTestBuilder.buildModel(CARD_ID);
		Card model = CardTestBuilder.buildModel();
		Card duplicated = CardTestBuilder.buildModel(CARD_ID_DUPLICATED);

		// Expectations
		when(repository.findByImgUrl(model.getImgUrl())).thenReturn(Optional.of(duplicated));

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(repository).findByImgUrl(model.getImgUrl());
		verify(repository, times(0)).save(origin);
	}

	@Test
	public void update_ValidObjectsGiven_ShouldUpdate() {
		// Set up
		Card origin = CardTestBuilder.buildModel(CARD_ID);
		Card model = CardTestBuilder.buildModel();
		model.setName("new Name");

		// Expectations
		when(repository.findByImgUrl(model.getImgUrl())).thenReturn(Optional.of(origin));
		when(repository.save(origin)).thenReturn(origin);

		// Do test
		Card result = service.update(origin, model);

		// Assertions
		verify(repository).findByImgUrl(model.getImgUrl());

		assertNotNull(result);
		assertThat(result, is(origin));
	}

	@Test
	public void findById_NullIdGiven_ShouldThrowException() {
		// Set up
		Integer cardId = null;

		// Do test
		try {
			service.findById(cardId);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findById_NotFoundCardGiven_ShouldThrowException() {
		// Set up
		Integer cardId = 1;

		// Expectations
		when(repository.findById(cardId)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findById(cardId);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(repository).findById(cardId);
	}

	@Test
	public void findById_FoundCardGiven_ShouldReturnContent() {
		// Set up
		Integer cardId = 1;
		Card card = CardTestBuilder.buildModel();

		// Expectations
		when(repository.findById(cardId)).thenReturn(Optional.of(card));

		// Do test
		Card result = service.findById(cardId);

		// Assertions
		verify(repository).findById(cardId);

		assertNotNull(result);
		assertThat(result, is(card));
	}

	//

	@Test
	public void delete_NullCardGiven_ShouldThrowException() {
		// Set up
		Card card = null;

		// Do test
		try {
			service.delete(card);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_NewCardGiven_ShouldThrowException() {
		// Set up
		Card card = CardTestBuilder.buildModel();

		// Do test
		try {
			service.delete(card);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void delete_ValidObjectsGiven_ShouldUpdate() {
		// Set up
		Card card = CardTestBuilder.buildModel(CARD_ID);

		// Expectations
		doNothing().when(repository).delete(card);

		// Do test
		service.delete(card);

		// Assertions
		verify(repository).delete(card);

	}

}
