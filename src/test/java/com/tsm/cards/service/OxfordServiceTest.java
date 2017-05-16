package com.tsm.cards.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class OxfordServiceTest {

	@InjectMocks
	private OxfordService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void findWordDefinition_NullWordGiven_ShouldThrowException() throws Exception {
		// Set up
		String word = null;

		// Do test
		service.findWordDefinition(word);
	}

}
