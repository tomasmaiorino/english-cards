package com.tsm.cards.parser;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeBuilder;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.resources.IParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ContentTypeParser implements IParser<ContentTypeResource, ContentType> {

	@Autowired
	private ContentTypeRuleParser ruleParser;

	@Autowired
	private ContentParser contentParser;

	public ContentType toModel(final ContentTypeResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		ContentTypeBuilder builder = ContentType.ContentTypeBuilder.ContentType(resource.getName());
		builder.status(ContentTypeStatus.valueOf(resource.getStatus()));
		if (StringUtils.isNotBlank(resource.getImgUrl())) {
			builder.imgUrl(resource.getImgUrl());
		}
		ContentType contentType = builder.build();
		if (!CollectionUtils.isEmpty(resource.getRules())) {
			resource.getRules().forEach(r -> contentType.addRule(ruleParser.toModel(r, contentType)));
		}
		return builder.build();
	}

	public Set<ContentTypeResource> toResources(final Set<ContentType> cardsType) {
		Assert.notEmpty(cardsType, "The cardsType must not be empty!");
		Set<ContentTypeResource> resources = new HashSet<>();
		cardsType.forEach(ct -> resources.add(toResource(ct)));
		return resources;
	}

	public ContentTypeResource toResource(final ContentType contentType) {
		Assert.notNull(contentType, "The contentType must not be null!");

		ContentTypeResource resource = new ContentTypeResource();
		resource.setName(contentType.getName());
		resource.setId(contentType.getId());
		resource.setStatus(contentType.getStatus().name());

		if (StringUtils.isNotBlank(contentType.getImgUrl())) {
			resource.setImgUrl(contentType.getImgUrl());
		}
		if (contentType.getRules() != null && !contentType.getRules().isEmpty()) {
			resource.setRules(
					contentType.getRules().stream().map(r -> ruleParser.toResource(r)).collect(Collectors.toSet()));
		}
		// filter content
		if (!CollectionUtils.isEmpty(contentType.getContents())) {
			contentType.getContents().removeIf(c -> c.getStatus().equals(Content.ContentStatus.INACTIVE));
			resource.setContents(contentParser.toResources(contentType.getContents()));
		}

		return resource;
	}

}
