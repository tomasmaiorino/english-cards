package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.Card;
import com.tsm.cards.repository.CardRepository;

@FixMethodOrder(MethodSorters.JVM)
public class CardServiceTest {

	@InjectMocks
	private CardService service;

	@Mock
	private CardRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullCardlGiven_ShouldThrowException() {
		// Set up
		Card card = null;

		// Do test
		try {
			service.save(card);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_ValidCardGiven_ShouldSave() {
		// Set up
		Card card = buildCard();

		// Expectations
		when(mockRepository.save(card)).thenReturn(card);

		// Do test
		Card result = service.save(card);

		// Assertions
		verify(mockRepository).save(card);

		assertThat(result, is(card));
	}

	private Card buildCard() {
		Card card = new Card();
		card.setWord("home");
		return card;
	}
}
