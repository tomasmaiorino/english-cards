package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.resources.ContentTypeResource;

public class ContentTypeTestBuilder extends BaseTestBuilder {

	public static final String LARGE_NAME = random(31, true, true);
	public static final String SMALL_NAME = random(1, true, true);
	public static final String VALID_NAME = random(30, true, true);
	public static final Card CARD = CardTestBuilder.buildModel();

	public static String getName() {
		return getString(ContentTypeConstants.CONTENT_TYPE_MAX_NAME_SIZE);
	}

	public static String getLargeName() {
		return getLargeString(ContentTypeConstants.CONTENT_TYPE_MAX_NAME_SIZE);
	}

	public static String getSmallName() {
		return getSmallString(ContentTypeConstants.CONTENT_TYPE_MIN_NAME_SIZE);
	}

	public static ContentType buildModel() {
		return buildModel(VALID_NAME, CARD);
	}

	public static ContentType buildModel(final Integer id) {
		ContentType contentType = buildModel(VALID_NAME, CARD);
		ReflectionTestUtils.setField(contentType, "id", id);
		ReflectionTestUtils.setField(contentType, "created", LocalDateTime.now());
		return contentType;
	}

	public static ContentType buildModel(final String name) {
		return buildModel(name, null);
	}

	public static ContentType buildModel(final String name, final Card card) {
		ContentType contentType = ContentType.ContentTypeBuilder.ContentType(name).build();
		contentType.setName(name);
		contentType.setStatus(getContentTypeStatus());
		return contentType;
	}

	public static ContentTypeStatus getContentTypeStatus() {
		return ContentTypeStatus.values()[RandomUtils.nextInt(0, ContentTypeStatus.values().length - 1)];
	}

	public static ContentTypeResource buildResource() {
		return buildResource(getName(), getStatus());
	}

	public static ContentTypeResource buildResource(final String name, final String status) {
		ContentTypeResource resource = new ContentTypeResource();
		resource.setName(name);
		resource.setStatus(status);
		return resource;
	}

	public static String getStatus() {
		return getContentTypeStatus().name();
	}

}
