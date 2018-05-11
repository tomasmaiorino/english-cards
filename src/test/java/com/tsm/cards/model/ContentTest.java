package com.tsm.cards.model;

import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTest {

	private String name;
	private String imgUrl;
	private String content;
	private ContentType contentType;
	private ContentStatus status;
	private static final Integer CONTENT_TYPE_ID = RandomUtils.nextInt(1, 100);

	@Before
	public void setUp() {
		name = random(100, true, true);
		imgUrl = random(100, true, true);
		content = random(100, true, true);
		status = ContentTestBuilder.getContentStatus();
		contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullNameGiven_ShouldThrowException() {

		// Set up
		name = null;

		// Do test
		buildContent();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyNameGiven_ShouldThrowException() {

		// Set up
		name = "";

		// Do test
		buildContent();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setImgUrl_NullImgUrlGiven_ShouldThrowException() {

		// Set up
		imgUrl = null;
		Content content = buildContent();

		// Do test
		content.setImgUrl(imgUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setImgUrl_EmptyImgUrlTokenGiven_ShouldThrowException() {

		// Set up
		imgUrl = "";
		Content content = buildContent();

		// Do test
		content.setImgUrl(imgUrl);

	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullContentTypeGiven_ShouldThrowException() {

		// Set up
		contentType = null;

		// Do test
		buildContent();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NewContentTypeGiven_ShouldThrowException() {

		// Set up
		contentType = ContentTypeTestBuilder.buildModel();

		// Do test
		buildContent();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullStatusGiven_ShouldThrowException() {

		// Set up
		status = null;

		// Do test
		buildContent();
	}

	private Content buildContent() {
		return ContentTestBuilder.buildModel(name, content, contentType, status);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		Content result = buildContent();

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)), hasProperty("imgUrl", nullValue()),
						hasProperty("isHtml", is(false)), hasProperty("contentType", is(contentType)),
						hasProperty("status", is(status))));
	}

}
