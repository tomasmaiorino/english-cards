package com.tsm.cards.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;

@SuppressWarnings("unchecked")
public class ManageWordServiceTest {

	@Mock
	private OriginalCallService mockOriginalCallService;

	@Mock
	private OxfordService mockOxfordService;

	@InjectMocks
	private ManageWordService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createOriginalCall_NullWorkGiven__ShouldThrowException() throws Exception {
		// Set up
		String word = null;

		// Do test
		try {
			service.createOriginalCall(word);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockOxfordService);
		verifyZeroInteractions(mockOriginalCallService);
	}

	@Test
	public void createOriginalCall_ValidWorkGiven_ShouldNotFoundException() throws Exception {
		// Set up
		String word = "home";

		// Expectations
		when(mockOxfordService.findWordDefinition(word)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			service.createOriginalCall(word);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockOxfordService).findWordDefinition(word);
		verifyZeroInteractions(mockOriginalCallService);
	}

	@Test
	public void createOriginalCall_ValidWorkGiven() throws Exception {
		// Set up
		String word = "home";
		OriginalCall originalCall = new OriginalCall();

		// Expectations
		when(mockOxfordService.findWordDefinition(word)).thenReturn(originalCall);
		when(mockOriginalCallService.save(originalCall)).thenReturn(originalCall);

		// Do test
		OriginalCall result = service.createOriginalCall(word);

		// Assertions
		verify(mockOxfordService).findWordDefinition(word);
		verify(mockOriginalCallService).save(originalCall);

		assertThat(result, is(originalCall));
		assertThat(word, is(result.getId()));
	}
}
