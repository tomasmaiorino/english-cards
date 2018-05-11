package com.tsm.cards.controller;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.Client;
import com.tsm.cards.parser.ClientParser;
import com.tsm.cards.resources.ClientResource;
import com.tsm.cards.service.ClientService;
import com.tsm.cards.util.ClientTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerTest {

	@Mock
	private ClientService mockService;

	@Mock
	private ClientParser mockParser;

	@InjectMocks
	private ClientsController controller;

	@Mock
	private Validator validator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidClientResourceGiven_ShouldThrowException() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();

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
	public void save_DuplicatedClientResourceGiven_ShouldSaveClient() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(client);
		when(mockService.save(client)).thenThrow(BadRequestException.class);

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(client);
		verify(mockParser).toModel(resource);

	}

	@Test
	public void save_ValidClientResourceGiven_ShouldSaveClient() {
		// Set up
		ClientResource resource = ClientTestBuilder.buildResoure();
		Client client = ClientTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(client);
		when(mockService.save(client)).thenReturn(client);
		when(mockParser.toResource(client)).thenReturn(resource);

		// Do test
		ClientResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(client);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(client);

		assertNotNull(result);
		assertThat(result, is(resource));
	}
}
