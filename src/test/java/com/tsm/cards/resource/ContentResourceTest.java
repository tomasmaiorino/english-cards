package com.tsm.cards.resource;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.resources.ContentResource;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.cards.util.ErrorCodes;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class ContentResourceTest extends BaseResourceTest {

	private Supplier<? extends BaseResource> buildResourceFunction = ContentTestBuilder::buildResource;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	public void build_NullNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", null, ErrorCodes.REQUIRED_CONTENT_NAME);
	}

	@Test
	public void build_SmallNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", ContentTestBuilder.getSmallName(),
				ErrorCodes.INVALID_CONTENT_NAME_SIZE);
	}

	@Test
	public void build_LargeNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", ContentTestBuilder.getLargeName(),
				ErrorCodes.INVALID_CONTENT_NAME_SIZE);
	}

	@Test
	public void build_EmptyNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", "", ErrorCodes.INVALID_CONTENT_NAME_SIZE);
	}

	//

	@Test
	public void build_NullStatusGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "status", null, ErrorCodes.REQUIRED_CONTENT_STATUS);
	}

	@Test
	public void build_InvalidStatusGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "status", ContentTestBuilder.getInvalidStatus(),
				ErrorCodes.INVALID_CONTENT_STATUS);
	}

	//

	@Test
	public void build_NullContentGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "content", null, ErrorCodes.REQUIRED_CONTENT);
	}

	@Test
	public void build_SmallContentGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "content", ContentTestBuilder.getSmallContent(),
				ErrorCodes.INVALID_CONTENT_SIZE);
	}

	@Test
	public void build_LargeContentGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "content", ContentTestBuilder.getLargeContent(),
				ErrorCodes.INVALID_CONTENT_SIZE);
	}

	@Test
	public void build_EmptyContentGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "content", "", ErrorCodes.INVALID_CONTENT_SIZE);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String name = ContentTestBuilder.getName();
		String status = ContentTestBuilder.getStatus();
		String content = ContentTestBuilder.getContent();

		ContentResource result = ContentTestBuilder.buildResource(content, name, status);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)),
				hasProperty("content", is(content)), hasProperty("status", is(status))));
	}

}
