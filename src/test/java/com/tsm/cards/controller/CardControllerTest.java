package com.tsm.cards.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.Card;
import com.tsm.cards.service.CardService;

public class CardControllerTest {

	@InjectMocks
	private CardService service;

	@Mock
	private CardsController cardsController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullWordGiven_ShouldThrowException() {
		// Set up
		Card card = null;

		// Do test
		try {
			cardsController.save(card);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(service);
	}

}
