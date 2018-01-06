package com.tsm.cards.parser;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.resources.ContentResource;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentParserTest {

	private static final Integer CONTENT_TYPE_ID = 1;

	@InjectMocks
	private ContentParser parser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = null;
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);

		// Do test
		parser.toModel(resource, contentType);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		ContentType contentType = null;

		// Do test
		parser.toModel(resource, contentType);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NewContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel();

		// Do test
		parser.toModel(resource, contentType);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void toModel_ValidResourceGiven_ShouldCreateContentModel() {
		// Set up
		ContentResource resource = ContentTestBuilder.buildResource();
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);

		// Do test
		Content result = parser.toModel(resource, contentType);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
				hasProperty("content", is(resource.getContent())), hasProperty("imgUrl", is(resource.getImgUrl())),
				hasProperty("isHtml", is(false)), hasProperty("contentType", is(contentType)),
				hasProperty("status", is(ContentStatus.valueOf(resource.getStatus())))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullContentGiven_ShouldThrowException() {
		// Set up
		Content client = null;

		// Do test
		parser.toResource(client);
	}

	@Test
	public void toResource_ValidContentGiven_ShouldCreateResource() {
		// Set up
		Content contentType = ContentTestBuilder.buildModel(CONTENT_TYPE_ID);

		// Do test
		ContentResource result = parser.toResource(contentType);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", is(contentType.getId())),
				hasProperty("name", is(contentType.getName())), hasProperty("content", is(contentType.getContent())),
				hasProperty("isHtml", is(contentType.getIsHtml())), hasProperty("imgUrl", is(contentType.getImgUrl())),
				hasProperty("status", is(contentType.getStatus().name()))));

	}
}