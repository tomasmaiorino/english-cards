package com.tsm.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.cards.util.CardTypeTestBuilder;
import com.tsm.cards.util.ContentTypeRuleTestBuilder;
import com.tsm.controller.ContentTypesControllerIT;

import lombok.Getter;
import lombok.Setter;

public class ContentTypeResource {

	public static ContentTypeResource build() {
		return new ContentTypeResource();
	}

	public ContentTypeResource assertFields() {
		if (Objects.isNull(name)) {
			name();
		}
		if (Objects.isNull(status)) {
			status();
		}

		return this;
	}

	public ContentTypeResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when()
				.post(ContentTypesControllerIT.SAVE_CONTENTS_TYPES_END_POINT).as(ContentTypeResource.class);
	}

	private ContentTypeResource() {
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
	private Set<ContentTypeRuleResource> rules;

	@Getter
	@Setter
	private Set<ContentResource> contents;

	public ContentTypeResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public ContentTypeResource name() {
		return name(random(30, true, true));
	}

	public ContentTypeResource name(final String name) {
		this.name = name;
		return this;
	}

	public ContentTypeResource status() {
		return status(CardTypeTestBuilder.getStatus());
	}

	public ContentTypeResource status(final String status) {
		this.status = status;
		return this;
	}

	public ContentTypeResource imgUrl() {
		return imgUrl(CardTypeTestBuilder.getImgUrl());
	}

	public ContentTypeResource imgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
		return this;
	}

	private void addRule(ContentTypeRuleResource rule) {
		if (rules == null) {
			rules = new HashSet<>();
		}
		rules.add(rule);
	}

	public ContentTypeResource rules(Integer quantity) {
		ContentTypeRuleResource ruleResource = new ContentTypeRuleResource();
		while (quantity-- > 0) {
			ruleResource.setAttribute(ContentTypeRuleTestBuilder.getAttribute());
			ruleResource.setRule(ContentTypeRuleTestBuilder.getRule());
			addRule(ruleResource);
		}
		return this;
	}

	public void setRules(final Set<ContentTypeRuleResource> rules) {
		this.rules = rules;
	}
}