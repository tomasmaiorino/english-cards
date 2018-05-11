package com.tsm.cards.parser;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentTypeRule;
import com.tsm.cards.resources.ContentTypeRuleResource;
import com.tsm.cards.resources.IParser;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;

@Component
public class ContentTypeRuleParser implements IParser<ContentTypeRuleResource, ContentTypeRule> {

	@Override
	public ContentTypeRule toModel(final ContentTypeRuleResource resource) {
		return null;
	}

	public ContentTypeRule toModel(final ContentTypeRuleResource resource, final ContentType contentType) {
		Assert.notNull(resource, "The resource must not be null!");
		Assert.notNull(contentType, "The contentType must not be null!");
		return ContentTypeRule.ContentTypeRuleBuilder
				.ContentTypeRule(resource.getAttribute(), resource.getRule(), contentType).build();
	}

	@Override
	public ContentTypeRuleResource toResource(final ContentTypeRule model) {
		Assert.notNull(model, "The model must not be null!");
		Assert.isTrue(!model.isNew(), "The model must not be new!");
		ContentTypeRuleResource resource = new ContentTypeRuleResource();
		resource.setAttribute(model.getAttribute());
		resource.setRule(model.getRule());
		return resource;
	}

	@Override
	public Set<ContentTypeRuleResource> toResources(final Set<ContentTypeRule> models) {
		Assert.notEmpty(models, "The models must not be empty!");

		return null;
	}

}
