package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.parser.ContentTypeParser;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.service.ContentTypeService;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypesControllerTest {

	@Mock
	private ContentTypeService mockService;

	@Mock
	private ContentTypeParser mockParser;

	@InjectMocks
	private ContentTypesController controller;

	@Mock
	private Validator validator;

	private Integer CONTENT_TYPE_ID = 1;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidContentTypeResourceGiven_ShouldThrowException() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();

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
	public void save_DuplicatedContentTypeResourceGiven_ShouldSaveContentType() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(contentType);
		when(mockService.save(contentType)).thenThrow(BadRequestException.class);

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(contentType);
		verify(mockParser).toModel(resource);

	}

	@Test
	public void save_ValidContentTypeResourceGiven_ShouldSaveContentType() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockParser.toModel(resource)).thenReturn(contentType);
		when(mockService.save(contentType)).thenReturn(contentType);
		when(mockParser.toResource(contentType)).thenReturn(resource);

		// Do test
		ContentTypeResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).save(contentType);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(contentType);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void update_InvalidContentTypeResourceGiven_ShouldThrowException() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();

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
		verifyZeroInteractions(mockService, mockParser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void update_NotFoundContentTypeResourceGiven_ShouldThrowException() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockService.findById(CONTENT_TYPE_ID)).thenThrow(ResourceNotFoundException.class);

		// Do test
		try {
			controller.update(CONTENT_TYPE_ID, resource);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).findById(CONTENT_TYPE_ID);
		verifyZeroInteractions(mockService, mockParser);
	}

	@Test
	public void udpate_ValidContentTypeResourceGiven_ShouldSaveContentType() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel();
		ContentType origin = ContentTypeTestBuilder.buildModel();
		ReflectionTestUtils.setField(origin, "id", CONTENT_TYPE_ID);

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(mockService.findById(CONTENT_TYPE_ID)).thenReturn(origin);
		when(mockParser.toModel(resource)).thenReturn(contentType);
		when(mockService.update(origin, contentType)).thenReturn(origin);
		when(mockParser.toResource(origin)).thenReturn(resource);

		// Do test
		ContentTypeResource result = controller.update(CONTENT_TYPE_ID, resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(mockService).update(origin, contentType);
		verify(mockParser).toModel(resource);
		verify(mockParser).toResource(origin);
		verify(mockService).findById(CONTENT_TYPE_ID);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void findById_NotFoundContentTypeGiven_ShouldThrowException() {

		// Expectations
		when(mockService.findById(CONTENT_TYPE_ID)).thenThrow(new ResourceNotFoundException(""));

		// Do test
		try {
			controller.findById(CONTENT_TYPE_ID);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockService).findById(CONTENT_TYPE_ID);
		verifyZeroInteractions(mockParser);
	}

	@Test
	public void findById_FoundContentTypeGiven_ShouldReturnContent() {
		// Set up
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(mockService.findById(CONTENT_TYPE_ID)).thenReturn(contentType);
		when(mockParser.toResource(contentType)).thenReturn(resource);

		// Do test
		ContentTypeResource result = controller.findById(CONTENT_TYPE_ID);

		// Assertions
		verify(mockService).findById(CONTENT_TYPE_ID);
		verify(mockParser).toResource(contentType);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void findAll_NotFoundCardsTypeGiven_ShouldReturnEmptyContent() {
		// Set up
		Set<ContentType> cardsType = Collections.emptySet();

		// Expectations
		when(mockService.findAllByStatus(ContentTypeStatus.ACTIVE)).thenReturn(cardsType);

		// Do test
		Set<ContentTypeResource> result = controller.findAll();

		// Assertions
		verify(mockService).findAllByStatus(ContentTypeStatus.ACTIVE);
		verifyZeroInteractions(mockParser);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));

	}

	@Test
	public void findAll_FoundCardsTypeGiven_ShouldReturnContent() {
		// Set up
		Set<ContentType> cardsType = new HashSet<>(1);
		ContentType contentType = ContentTypeTestBuilder.buildModel();
		cardsType.add(contentType);

		Set<ContentTypeResource> resources = new HashSet<>(1);
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		resources.add(resource);

		// Expectations
		when(mockService.findAllByStatus(ContentTypeStatus.ACTIVE)).thenReturn(cardsType);
		when(mockParser.toResources(cardsType)).thenReturn(resources);

		// Do test
		Set<ContentTypeResource> result = controller.findAll();

		// Assertions
		verify(mockService).findAllByStatus(ContentTypeStatus.ACTIVE);
		verify(mockParser).toResources(cardsType);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
	}

}
