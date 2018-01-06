package com.tsm.cards.resource;

import static com.tsm.cards.util.ContentTypeTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ContentTypeTestBuilder.SMALL_NAME;
import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_NAME;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_STATUS;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeResourceTest extends BaseResourceTest {

	private Supplier<? extends BaseResource> buildResourceFunction = ContentTypeTestBuilder::buildResource;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	public void build_NullNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", null, REQUIRED_CONTENT_TYPE_NAME);
	}

	@Test
	public void build_SmallNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CONTENT_TYPE_NAME_SIZE);
	}

	@Test
	public void build_LargeNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CONTENT_TYPE_NAME_SIZE);
	}

	@Test
	public void build_EmptyNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", "", INVALID_CONTENT_TYPE_NAME_SIZE);
	}

	//

	@Test
	public void build_NullStatusGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "status", null, REQUIRED_CONTENT_TYPE_STATUS);
	}

	@Test
	public void build_InvalidStatusGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "status", ContentTypeTestBuilder.getInvalidStatus(),
				INVALID_CONTENT_TYPE_STATUS);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String name = ContentTypeTestBuilder.getName();
		String status = ContentTypeTestBuilder.getStatus();

		ContentTypeResource result = ContentTypeTestBuilder.buildResource(name, status);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)),
				hasProperty("rules", nullValue()), hasProperty("status", is(status))));
	}

}
