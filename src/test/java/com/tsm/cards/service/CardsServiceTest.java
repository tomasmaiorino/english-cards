package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.Cards;
import com.tsm.cards.repository.CardsRepository;

@FixMethodOrder(MethodSorters.JVM)
public class CardsServiceTest {

	@Mock
	private CardsRepository mockRepository;

	@InjectMocks
	private CardsService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullCardsGiven_ShouldThrowException() {
		// Set up
		Cards cards = null;

		// Do test
		try {
			service.save(cards);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_ValidCardsGiven_CardSaved() {
		// Set up
		Cards cards = new Cards();

		// Expectations
		when(mockRepository.save(cards)).thenReturn(cards);

		// Do test
		Cards result = service.save(cards);

		// Assertions
		verify(mockRepository).save(cards);
		assertThat(result, is(cards));
	}

	@Test
	public void findByWord_NullWordGiven_ShouldThrowException() {
		// Set up
		String word = null;

		// Do test
		try {
			service.findByWord(word);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByWord_emptyWordGiven_ShouldThrowException() {
		// Set up
		String word = "";

		// Do test
		try {
			service.findByWord(word);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByWord_validWordGiven_NotWordFound_ShouldThrowException() {
		// Set up
		String word = "word";

		// Expectations
		when(mockRepository.findByWord(word)).thenReturn(Collections.emptyList());

		// Do test
		List<Cards> result = service.findByWord(word);

		// Assertions
		verify(mockRepository).findByWord(word);
		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void findByWord_validWordGiven_CardsFound() {
		// Set up
		String word = "word";
		Cards cards = new Cards();

		List<Cards> cardsList = new ArrayList<>();
		cardsList.add(cards);

		// Expectations
		when(mockRepository.findByWord(word)).thenReturn(cardsList);

		// Do test
		List<Cards> result = service.findByWord(word);

		// Assertions
		verify(mockRepository).findByWord(word);
		assertThat(result, is(cardsList));
	}

}
