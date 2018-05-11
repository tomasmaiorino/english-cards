package com.tsm.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.cards.util.CardTypeTestBuilder;
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.controller.ContentsControllerIT;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

public class ContentResource {

	public static ContentResource build() {
		return new ContentResource();
	}

	public ContentResource assertFields() {
		if (Objects.isNull(name)) {
			name();
		}
		if (Objects.isNull(status)) {
			status();
		}

		if (Objects.isNull(content)) {
			content();
		}

		if (Objects.isNull(contentType)) {
			contentType();
		}

		return this;
	}

	public ContentResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when()
				.post(ContentsControllerIT.SAVE_CONTENTS_END_POINT).as(ContentResource.class);
	}

	private ContentResource() {
	}

	@JsonIgnore
	@Getter
	@Setter
	private Map<String, String> headers;

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String imgUrl;

	@Getter
	@Setter
	private String status;

	@Getter
	@Setter
	private Boolean isHtml;

	@Getter
	@Setter
	private String content;

	@Getter
	@Setter
	private Integer contentType;

	public ContentResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public ContentResource name() {
		return name(random(30, true, true));
	}

	public ContentResource name(final String name) {
		this.name = name;
		return this;
	}

	public ContentResource status() {
		return status(CardTypeTestBuilder.getStatus());
	}

	public ContentResource status(final String status) {
		this.status = status;
		return this;
	}

	public ContentResource imgUrl() {
		return imgUrl(CardTypeTestBuilder.getImgUrl());
	}

	public ContentResource imgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
		return this;
	}

	public ContentResource isHtml() {
		return isHtml(true);
	}

	public ContentResource isHtml(final Boolean isHtml) {
		this.isHtml = isHtml;
		return this;
	}

	public ContentResource content() {
		return content(ContentTestBuilder.getContent());
	}

	public ContentResource content(final String content) {
		this.content = content;
		return this;
	}

	public ContentResource contentType() {
		return contentType(ContentTypeResource.build().headers(getHeaders()).create().getId());
	}

	public ContentResource contentType(final Integer contentType) {
		this.contentType = contentType;
		return this;
	}

}