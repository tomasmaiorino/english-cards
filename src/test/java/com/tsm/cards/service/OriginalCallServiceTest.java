package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.repository.OriginalCallRepository;

public class OriginalCallServiceTest {

	@InjectMocks
	private OriginalCallService service;

	@Mock
	private OriginalCallRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullOriginalGiven_ShouldThrowException() {
		// Set up
		OriginalCall originalCall = null;

		// Do test
		try {
			service.save(originalCall);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_ValidOriginalGiven_ShouldSave() {
		// Set up
		OriginalCall originalCall = buildOriginalCall("home");

		// Expectations
		when(mockRepository.save(originalCall)).thenReturn(originalCall);

		// Do test
		OriginalCall returns = service.save(originalCall);

		// Assertions
		verify(mockRepository).save(originalCall);

		assertThat(returns, is(originalCall));
	}

	@Test
	public void findById_NullWordId_ShouldThrowException() {
		// Set up
		String word = null;

		// Do test
		try {
			service.findById(word);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findById_ValidWordId_ShouldReturnOriginalCallNotFound() {
		// Set up
		String word = "home";

		// Expectations
		Optional<OriginalCall> emptyOptional = Optional.empty();
		when(mockRepository.findById(word)).thenReturn(emptyOptional);

		// Do test
		try {
			service.findById(word);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findById(word);
	}

	@Test
	public void findById_ValidWordIdGiven_OriginalCallFound() {
		// Set up
		String word = "word";

		// Expectations
		OriginalCall originalCall = buildOriginalCall(word);
		Optional<OriginalCall> expectedOptional = Optional.of(originalCall);
		when(mockRepository.findById(word)).thenReturn(expectedOptional);

		// Do test
		OriginalCall result = service.findById(word);

		// Assertions
		verify(mockRepository).findById(word);
		assertThat(result, is(originalCall));
	}

	@Test
	public void findByDefinitionId_NullDefinitionIdGiven_ShouldThrowException() {
		// Set up
		String definitionId = null;

		// Do test
		try {
			service.findByDefinitionId(definitionId);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByDefinitionId_ValidDefinitionIdGiven_NotResultFound() {
		// Set up
		String definitionId = "123";

		// Expectations
		Optional<OriginalCall> emptyOptional = Optional.empty();
		when(mockRepository.findByDefinitionId(definitionId)).thenReturn(emptyOptional);

		// Do test
		OriginalCall result = service.findByDefinitionId(definitionId);

		// Assertions
		verify(mockRepository).findByDefinitionId(definitionId);
		Assert.assertNull(result);
	}

	@Test
	public void findByDefinitionId_ValidDefinitionIdGiven_ShouldReturnOriginalCall() {
		// Set up
		String definitionId = "123";
		String word = "home";
		OriginalCall originalCall = buildOriginalCall(word);

		// Expectations
		Optional<OriginalCall> optional = Optional.of(originalCall);
		when(mockRepository.findByDefinitionId(definitionId)).thenReturn(optional);

		// Do test
		OriginalCall result = service.findByDefinitionId(definitionId);

		// Assertions
		verify(mockRepository).findByDefinitionId(definitionId);
		Assert.assertNotNull(result);
		assertThat(result, is(originalCall));
	}

	private OriginalCall buildOriginalCall(String id) {
		OriginalCall call = new OriginalCall();
		call.setId(id);
		return call;
	}
}
