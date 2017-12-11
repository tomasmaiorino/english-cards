package com.tsm.cards.parser;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.resources.ContentTypeResource;

@Component
public class ContentTypeParser {

	public ContentType toModel(final ContentTypeResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		ContentType cardType = new ContentType();
		cardType.setName(resource.getName());
		cardType.setStatus(ContentTypeStatus.valueOf(resource.getStatus()));
		if (StringUtils.isNotBlank(resource.getImgUrl())) {
			cardType.setImgUrl(resource.getImgUrl());
		}
		return cardType;
	}

	public Set<ContentTypeResource> toResources(final Set<ContentType> cardsType) {
		Assert.notEmpty(cardsType, "The cardsType must not be empty!");
		Set<ContentTypeResource> resources = new HashSet<>();
		cardsType.forEach(ct -> resources.add(toResource(ct)));
		return resources;
	}

	public ContentTypeResource toResource(final ContentType cardType) {
		Assert.notNull(cardType, "The cardType must not be null!");
		ContentTypeResource resource = new ContentTypeResource();
		resource.setName(cardType.getName());
		resource.setId(cardType.getId());
		resource.setStatus(cardType.getStatus().name());
		if (StringUtils.isNotBlank(cardType.getImgUrl())) {
			resource.setImgUrl(cardType.getImgUrl());
		}
		// if (!CollectionUtils.isEmpty(cardType.getCards())) {
		// cardType.getCards().forEach(c -> {
		// resource.addCards(cardParser.toResource(c));
		// });
		// }
		return resource;
	}

}
