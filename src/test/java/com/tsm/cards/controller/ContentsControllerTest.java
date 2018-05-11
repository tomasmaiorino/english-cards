package com.tsm.cards.controller;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Content;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.parser.ContentParser;
import com.tsm.cards.resources.ContentResource;
import com.tsm.cards.service.ContentService;
import com.tsm.cards.service.ContentTypeService;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class ContentsControllerTest {

	private static final Integer CONTENT_ID = 1;

	@Mock
	private ContentTypeService contentTypeService;

	@Mock
	private ContentService service;

	@Mock
	private ContentParser parser;

	@InjectMocks
	private ContentsController controller;

	@Mock
	private Validator validator;

	private static final Integer CONTENT_TYPE_ID = 1;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidContentResourceGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();

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
		verifyZeroInteractions(service, parser);
	}

	@Test
	public void save_NotFoundContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		resource.setContentType(CONTENT_TYPE_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(contentTypeService.findById(CONTENT_TYPE_ID)).thenThrow(new ResourceNotFoundException(""));

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(contentTypeService).findById(CONTENT_TYPE_ID);
		verifyZeroInteractions(service, parser);
	}

	@Test
	public void save_ValidContentResourceGiven_ShouldSaveContentType() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		resource.setContentType(CONTENT_TYPE_ID);
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		Content model = ContentTestBuilder.buildModel(CONTENT_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(contentTypeService.findById(CONTENT_TYPE_ID)).thenReturn(contentType);
		when(parser.toModel(resource, contentType)).thenReturn(model);
		when(service.save(model)).thenReturn(model);
		when(parser.toResource(model)).thenReturn(resource);

		// Do test
		ContentResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(parser).toModel(resource, contentType);
		verify(contentTypeService).findById(CONTENT_TYPE_ID);
		verify(service).save(model);
		verify(parser).toResource(model);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void update_InvalidContentResourceGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.update(CONTENT_TYPE_ID, resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(service, parser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void update_NotFoundContentResourceGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(service.findById(CONTENT_TYPE_ID)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			controller.update(CONTENT_TYPE_ID, resource);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(service).findById(CONTENT_TYPE_ID);
		verify(parser, times(0)).toResource(any(Content.class));
	}

	@Test
	public void update_NotFoundContentTypeResourceGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		resource.setContentType(CONTENT_TYPE_ID);

		Content model = ContentTestBuilder.buildModel(CONTENT_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(service.findById(CONTENT_ID)).thenReturn(model);
		when(contentTypeService.findById(CONTENT_TYPE_ID)).thenThrow(new ResourceNotFoundException(""));

		// Do test
		try {
			controller.update(CONTENT_ID, resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(service).findById(CONTENT_ID);
		verify(contentTypeService).findById(CONTENT_TYPE_ID);
		verify(parser, times(0)).toResource(any(Content.class));
	}

	@Test
	public void udpate_ValidContentResourceGiven_ShouldSaveContentType() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		resource.setContentType(CONTENT_TYPE_ID);
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		Content model = ContentTestBuilder.buildModel(CONTENT_ID);
		Content origin = ContentTestBuilder.buildModel(CONTENT_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(service.findById(CONTENT_TYPE_ID)).thenReturn(origin);
		when(contentTypeService.findById(CONTENT_TYPE_ID)).thenReturn(contentType);
		when(parser.toModel(resource, contentType)).thenReturn(model);
		when(service.update(origin, model)).thenReturn(origin);
		when(parser.toResource(origin)).thenReturn(resource);

		// Do test
		ContentResource result = controller.update(CONTENT_TYPE_ID, resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(service).findById(CONTENT_TYPE_ID);
		verify(contentTypeService).findById(CONTENT_ID);
		verify(parser).toModel(resource, contentType);
		verify(service).update(origin, model);
		verify(parser).toResource(origin);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void delete_NotFoundContentGiven_ShouldThrowException() {

		// Expectations
		when(service.findById(CONTENT_ID)).thenThrow(new ResourceNotFoundException(""));

		// Do test
		try {
			controller.delete(CONTENT_ID);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(service).findById(CONTENT_ID);
		verify(service, times(0)).delete(any(Content.class));
	}

	@Test
	public void delete_ValidContentGiven_ShouldThrowException() {
		// Set up
		Content model = ContentTestBuilder.buildModel(CONTENT_ID);

		// Expectations
		when(service.findById(CONTENT_ID)).thenReturn(model);
		doNothing().when(service).delete(model);

		// Do test
		controller.delete(CONTENT_ID);

		// Assertions
		verify(service).findById(CONTENT_ID);
		verify(service).delete(model);
	}

}
