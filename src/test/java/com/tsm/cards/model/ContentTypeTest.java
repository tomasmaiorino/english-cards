package com.tsm.cards.model;

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

import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.util.ContentTypeRuleTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeTest {

	private String name;

	@Before
	public void setUp() {
		name = ContentTypeTestBuilder.getName();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_NullNameGiven_ShouldThrowException() {
		// Set up
		name = null;

		// Do test
		buildContentType();
	}

	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyNameGiven_ShouldThrowException() {
		// Set up
		name = "";

		// Do test
		buildContentType();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setStatus_NullStatusGiven_ShouldThrowException() {
		// Set up
		ContentTypeStatus status = null;
		ContentType contentType = buildContentType();

		// Do test
		contentType.setStatus(status);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addContentRule_NullRuleGiven_ShouldThrowException() {

		// Set up
		ContentType contentType = buildContentType();
		ContentTypeRule rule = null;

		// Do test
		contentType.addRule(rule);
	}

	@Test
	public void addContentRule_ValidRuleGiven_ShouldAddRule() {

		// Set up
		ContentType contentType = buildContentType();
		ContentTypeRule rule = ContentTypeRuleTestBuilder.buildModel();

		// Do test
		contentType.addRule(rule);

		// Assertions
		assertThat(contentType.getRules().isEmpty(), is(false));
		assertThat(contentType.getRules().contains(rule), is(true));
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String name = ContentTypeTestBuilder.getName();
		ContentType contentType = ContentTypeTestBuilder.buildModel(name);

		// Assertions
		assertNotNull(contentType);
		assertThat(contentType, allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)),
				hasProperty("imgUrl", nullValue())));
	}

	private ContentType buildContentType() {
		return ContentType.ContentTypeBuilder.ContentType(name).build();
	}

}
