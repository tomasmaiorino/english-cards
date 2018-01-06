package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.resources.ContentResource;

public class ContentTestBuilder extends BaseTestBuilder {

	public static final String LARGE_IMG_URL = random(151, true, true);
	public static final String SMALL_IMG_URL = random(1, true, true);
	public static final String INVALID_STATUS = "INVD";

	public static ContentStatus getContentStatus() {
		return ContentStatus.values()[RandomUtils.nextInt(0, ContentStatus.values().length - 1)];
	}

	public static String getStatus() {
		return getContentStatus().name();
	}

	public static String getName() {
		return random(ContentConstants.CONTENT_MAX_NAME_SIZE, true, true);
	}

	public static String getLargeName() {
		return getLargeString(ContentConstants.CONTENT_MAX_NAME_SIZE);
	}

	public static String getSmallName() {
		return getSmallString(ContentConstants.CONTENT_MIN_NAME_SIZE);
	}

	public static String getContent() {
		return getString(ContentConstants.CONTENT_MAX_CONTENT_SIZE);
	}

	public static String getLargeContent() {
		return getLargeString(ContentConstants.CONTENT_MAX_CONTENT_SIZE);
	}

	public static String getSmallContent() {
		return getSmallString(ContentConstants.CONTENT_MIN_CONTENT_SIZE);
	}

	public static ContentType getContentType() {
		return ContentTypeTestBuilder.buildModel(getId());
	}

	private static Integer getId() {
		return RandomUtils.nextInt(1, 100);
	}

	public static Content buildModel(final Integer id) {
		Content content = buildModel();
		ReflectionTestUtils.setField(content, "id", id);
		ReflectionTestUtils.setField(content, "created", LocalDateTime.now());
		return content;
	}

	public static Content buildModel(final String name, final String sContent, final ContentType contentType,
			final ContentStatus status) {
		return Content.ContentBuilder.Content(name, sContent, contentType, status).build();
	}

	public static Content buildModel() {
		return buildModel(getName(), getContent(), getContentType(), getContentStatus());
	}

	public static ContentResource buildResource() {
		return buildResource(getContent(), getName(), getStatus());
	}

	public static ContentResource buildResource(final String content, final String name, final String status) {
		ContentResource resource = new ContentResource();
		resource.setContent(content);
		resource.setName(name);
		resource.setStatus(status);
		return resource;
	}
}
