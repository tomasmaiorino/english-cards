package com.tsm.cards.model;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeTest {

	@Test(expected = IllegalArgumentException.class)
	public void build_NullHostGiven_ShouldThrowException() {
		// Set up
		String name = null;

		// Do test
		ContentType contentType = new ContentType();
		contentType.setName(name);
	}

	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void build_NullCardGiven_ShouldThrowException() {
		// Set up
		Set<Card> cards = null;

		// Do test
		ContentType contentType = new ContentType();
		// contentType.setCards(cards);
	}

	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void build_EmptyCardGiven_ShouldThrowException() {
		// Set up
		Set<Card> cards = Collections.emptySet();

		// Do test
		ContentType contentType = new ContentType();
		// contentType.setCards(cards);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setImgUrl_NullImgUrlGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = new ContentType();

		contentType.setImgUrl(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setStatus_NullContentTypeStatusGiven_ShouldThrowException() {
		// Set up
		ContentType contentType = new ContentType();
		ContentTypeStatus contentTypeStatus = null;

		// Do test
		contentType.setStatus(contentTypeStatus);
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

}
