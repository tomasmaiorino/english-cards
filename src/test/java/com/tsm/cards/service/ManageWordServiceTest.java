package com.tsm.cards.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class ManageWordServiceTest {

	@Mock
	private DefinitionService mockDefinitionService;

	@Mock
	private OxfordService mockOxfordService;

	@InjectMocks
	private ManageWordService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createDefinition_NullWorkGiven__ShouldThrowException() throws Exception {
		// Set up
		String word = null;

		// Do test
		try {
			service.createDefinition(word);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockOxfordService);
		verifyZeroInteractions(mockDefinitionService);
	}

	@Test
	public void createDefinition_ValidWorkGiven_ShouldNotFoundException() throws Exception {
		// Set up
		String word = "home";

		// Expectations
		when(mockOxfordService.findWordDefinition(word)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			service.createDefinition(word);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockOxfordService).findWordDefinition(word);
		verifyZeroInteractions(mockDefinitionService);
	}

	@Test
	public void createDefinition_ValidWorkGiven() throws Exception {
		// Set up
		String word = "home";
		Definition definition = new Definition();

		// Expectations
		when(mockOxfordService.findWordDefinition(word)).thenReturn(definition);
		when(mockDefinitionService.save(definition)).thenReturn(definition);

		// Do test
		Definition result = service.createDefinition(word);

		// Assertions
		verify(mockOxfordService).findWordDefinition(word);
		verify(mockDefinitionService).save(definition);

		assertThat(result, is(definition));
		assertThat(word, is(result.getId()));
	}
}
