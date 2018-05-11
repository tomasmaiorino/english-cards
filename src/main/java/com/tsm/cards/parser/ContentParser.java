package com.tsm.cards.parser;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentBuilder;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.resources.ContentResource;
import com.tsm.cards.resources.IParser;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ContentParser implements IParser<ContentResource, Content> {

	@Override
	public Content toModel(final ContentResource resource) {
		throw new NotImplementedException("Not implemented.");
	}

	public Content toModel(final ContentResource resource, final ContentType contentType) {
		Assert.notNull(resource, "The resource must not be null!");
		Assert.notNull(contentType, "The contentType must not be null!");
		Assert.isTrue(!contentType.isNew(), "The contentType must not be new!");

		ContentBuilder builder = Content.ContentBuilder.Content(resource.getName(), resource.getContent(), contentType,
				ContentStatus.valueOf(resource.getStatus()));

		if (resource.getIsHtml() != null) {
			builder.isHtml(resource.getIsHtml());
		}
		if (StringUtils.isNotBlank(resource.getImgUrl())) {
			builder.imgUrl(resource.getImgUrl());
		}

		return builder.build();
	}

	@Override
	public ContentResource toResource(final Content model) {
		Assert.notNull(model, "The model must not be null!");
		Assert.isTrue(!model.isNew(), "The model must not be new!");
		ContentResource resource = new ContentResource();
		resource.setContent(model.getContent());
		resource.setId(model.getId());
		resource.setImgUrl(model.getImgUrl());
		resource.setIsHtml(model.getIsHtml());
		resource.setName(model.getName());
		resource.setStatus(model.getStatus().name());
		return resource;
	}

	@Override
	public Set<ContentResource> toResources(final Set<Content> models) {
		Assert.notEmpty(models, "The models must not be empty!");
		return models.stream().map(r -> toResource(r)).collect(Collectors.toSet());
	}

}
