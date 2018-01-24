package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.repository.ContentTypeRepository;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeServiceTest {

	private static final Integer CONTENT_TYPE_ID = 1;

	private static final Integer CONTENT_TYPE_ID_DUPLICATED = 2;

	@InjectMocks
	private ContentTypeService service;

	@Mock
	private ContentTypeRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = null;

		// Do test
		try {
			service.save(contentType);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_DuplicatedNameGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByName(contentType.getName())).thenReturn(Optional.of(contentType));

		// Do test
		try {
			service.save(contentType);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByName(contentType.getName());
		verify(mockRepository, times(0)).save(contentType);
	}

	@Test
	public void save_ValidContentTypeGiven_ShouldCreateClient() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByName(contentType.getName())).thenReturn(Optional.empty());
		when(mockRepository.save(contentType)).thenReturn(contentType);

		// Do test
		ContentType result = service.save(contentType);

		// Assertions
		verify(mockRepository).findByName(contentType.getName());

		assertNotNull(result);
		assertThat(result, is(contentType));
	}

	@Test
	public void update_NullOriginGiven_ShouldThrowException() {
		// Set up
		ContentType origin = null;
		ContentType model = ContentTypeTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_NewOriginGiven_ShouldThrowException() {
		// Set up
		ContentType origin = ContentTypeTestBuilder.buildModel();
		ContentType model = ContentTypeTestBuilder.buildModel();

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_NullModelGiven_ShouldThrowException() {
		// Set up
		ContentType origin = ContentTypeTestBuilder.buildModel();
		ContentType model = null;

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void update_DuplicatedNameGiven_ShouldThrowException() {
		// Set up
		ContentType origin = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentType model = ContentTypeTestBuilder.buildModel();
		ContentType duplicated = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID_DUPLICATED);

		// Expectations
		when(mockRepository.findByName(model.getName())).thenReturn(Optional.of(duplicated));

		// Do test
		try {
			service.update(origin, model);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByName(model.getName());
		verify(mockRepository, times(0)).save(origin);
	}

	@Test
	public void update_ValidObjectsGiven_ShouldUpdate() {
		// Set up
		ContentType origin = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentType model = ContentTypeTestBuilder.buildModel();
		String newName = "New name";
		model.setName(newName);

		// Expectations
		when(mockRepository.findByName(model.getName())).thenReturn(Optional.of(origin));
		when(mockRepository.save(origin)).thenReturn(origin);

		// Do test
		ContentType result = service.update(origin, model);

		// Assertions
		verify(mockRepository).findByName(model.getName());

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
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findById_NotFoundContentTypeGiven_ShouldThrowException() {
		// Set up
		Integer contentTypeId = 1;

		// Expectations
		when(mockRepository.findById(contentTypeId)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findById(contentTypeId);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findById(contentTypeId);
	}

	@Test
	public void findById_FoundContentTypeGiven_ShouldReturnContent() {
		// Set up
		Integer contentTypeId = 1;
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findById(contentTypeId)).thenReturn(Optional.of(contentType));

		// Do test
		ContentType result = service.findById(contentTypeId);

		// Assertions
		verify(mockRepository).findById(contentTypeId);

		assertNotNull(result);
		assertThat(result, is(contentType));
	}

	//

	@Test
	public void findByName_NullNamGiven_ShouldThrowException() {
		// Set up
		String name = null;

		// Do test
		try {
			service.findByName(name);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByName_EmptyNameGiven_ShouldThrowException() {
		// Set up
		String name = null;

		// Do test
		try {
			service.findByName(name);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByName_NotFoundContentTypeGiven_ShouldThrowException() {
		// Set up
		String name = "name";

		// Expectations
		when(mockRepository.findByName(name)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByName(name);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByName(name);
	}

	@Test
	public void findByName_FoundContentTypeGiven_ShouldReturnContent() {
		// Set up
		String name = "grammar";
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByName(name)).thenReturn(Optional.of(contentType));

		// Do test
		ContentType result = service.findByName(name);

		// Assertions
		verify(mockRepository).findByName(name);

		assertNotNull(result);
		assertThat(result, is(contentType));
	}

	//

	@Test
	public void findAllByStatus_NullIdGiven_ShouldThrowException() {
		// Set up
		ContentTypeStatus status = null;

		// Do test
		try {
			service.findAllByStatus(status);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findAllByStatus_NotFoundContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentTypeStatus status = ContentTypeStatus.ACTIVE;

		// Expectations
		when(mockRepository.findAllByStatus(status)).thenReturn(Collections.emptySet());

		// Do test
		Set<ContentType> result = service.findAllByStatus(status);

		// Assertions
		verify(mockRepository).findAllByStatus(status);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void findAllByStatus_FoundContentTypeGiven_ShouldReturnContent() {
		// Set up
		ContentTypeStatus status = ContentTypeStatus.ACTIVE;
		ContentType contentType = ContentTypeTestBuilder.buildModel();
		Set<ContentType> contentTypes = new HashSet<>();
		contentTypes.add(contentType);

		// Expectations
		when(mockRepository.findAllByStatus(status)).thenReturn(contentTypes);

		// Do test
		Set<ContentType> result = service.findAllByStatus(status);

		// Assertions
		verify(mockRepository).findAllByStatus(status);

		assertNotNull(result);
		assertThat(result.isEmpty(), is(false));
		assertThat(result.contains(contentType), is(true));
	}

}
