package com.tsm.cards.service;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.repository.ContentRepository;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class ContentServiceTest {

	private static final Integer CONTENT_TYPE_ID = 1;

	private static final Integer CONTENT_ID = 1;

	@InjectMocks
	private ContentService service;

	@Mock
	private ContentRepository repository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullContentGiven_ShouldThrowException() {
		// Set up
		Content contentType = null;

		// Do test
		try {
			service.save(contentType);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void save_ValidContentGiven_ShouldCreateClient() {
		// Set up
		Content content = ContentTestBuilder.buildModel();

		// Expectations
		when(repository.save(content)).thenReturn(content);

		// Do test
		Content result = service.save(content);

		// Assertions

		assertNotNull(result);
		assertThat(result, is(content));
	}

	@Test
	public void update_NullOriginGiven_ShouldThrowException() {
		// Set up
		Content origin = null;
		Content model = ContentTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_NewOriginGiven_ShouldThrowException() {
		// Set up
		Content origin = ContentTestBuilder.buildModel();
		Content model = ContentTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_NullModelGiven_ShouldThrowException() {
		// Set up
		Content origin = ContentTestBuilder.buildModel();
		Content model = null;

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void update_ValidObjectsGiven_ShouldUpdate() {
		// Set up
		Content origin = ContentTestBuilder.buildModel(CONTENT_TYPE_ID);
		origin.setImgUrl(ContentTestBuilder.getImgUrl());
		Content model = ContentTestBuilder.buildModel();
		String newName = "New name";
		model.setName(newName);
		model.setImgUrl(ContentTestBuilder.getImgUrl());

		// Expectations
		when(repository.save(origin)).thenReturn(origin);

		// Do test
		Content result = service.update(origin, model);

		// Assertions
		assertNotNull(result);
		assertThat(result, is(origin));
		assertThat(result.getName(), is(newName));
	}

	@Test
	public void findById_NullIdGiven_ShouldThrowException() {
		// Set up
		Integer contentTypeId = null;

		// Do test
		try {
			service.findById(contentTypeId);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findById_NotFoundContentGiven_ShouldThrowException() {
		// Set up
		Integer contentTypeId = 1;

		// Expectations
		when(repository.findById(contentTypeId)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findById(contentTypeId);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(repository).findById(contentTypeId);
	}

	@Test
	public void findById_FoundContentGiven_ShouldReturnContent() {
		// Set up
		Integer contentTypeId = 1;
		Content contentType = ContentTestBuilder.buildModel();

		// Expectations
		when(repository.findById(contentTypeId)).thenReturn(Optional.of(contentType));

		// Do test
		Content result = service.findById(contentTypeId);

		// Assertions
		verify(repository).findById(contentTypeId);

		assertNotNull(result);
		assertThat(result, is(contentType));
	}

	//

	@Test
	public void findAllByContentTypeAndStatus_NewContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel();
		ContentStatus status = ContentTestBuilder.getContentStatus();

		// Do test
		try {
			service.findAllByContentTypeAndStatus(contentType, status);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findAllByContentTypeAndStatus_NullContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = null;
		ContentStatus status = ContentStatus.ACTIVE;

		// Do test
		try {
			service.findAllByContentTypeAndStatus(contentType, status);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findAllByContentTypeAndStatus_NullStatusGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentStatus status = null;

		// Do test
		try {
			service.findAllByContentTypeAndStatus(contentType, status);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findAllByContentTypeAndStatus_NotFoundContentsGiven_ShouldReturnEmptyContent() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentStatus status = ContentTestBuilder.getContentStatus();

		// Expectations
		when(repository.findAllByContentTypeAndStatus(contentType, status)).thenReturn(Collections.emptySet());

		// Do test
		Set<Content> result = service.findAllByContentTypeAndStatus(contentType, status);

		// Assertions
		verify(repository).findAllByContentTypeAndStatus(contentType, status);
		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));

	}

	@Test
	public void findAllByContentTypeAndStatus_ValidContentTypeAndStatusGiven_ShouldReturnContent() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentStatus status = ContentTestBuilder.getContentStatus();
		Set<Content> contents = new HashSet<>();
		Content content = ContentTestBuilder.buildModel(CONTENT_ID);
		contents.add(content);

		// Expectations
		when(repository.findAllByContentTypeAndStatus(contentType, status)).thenReturn(contents);

		// Do test
		Set<Content> result = service.findAllByContentTypeAndStatus(contentType, status);

		// Assertions
		verify(repository).findAllByContentTypeAndStatus(contentType, status);
		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
	}

	//

	@Test
	public void delete_NewContentGiven_ShouldThrowException() {
		// Set up
		Content model = ContentTestBuilder.buildModel();

		// Do test
		try {
			service.delete(model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void delete_NullContentGiven_ShouldThrowException() {
		// Set up
		Content model = null;

		// Do test
		try {
			service.delete(model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void delete_ValidContentGiven_ShouldDeleteContent() {
		// Set up
		Content model = ContentTestBuilder.buildModel(CONTENT_ID);

		// Expectations
		doNothing().when(repository).delete(model);

		// Do test
		service.delete(model);

		// Assertions
		verify(repository).delete(model);
	}

}
