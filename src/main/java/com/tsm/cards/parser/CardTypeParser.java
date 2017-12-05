package com.tsm.cards.parser;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardTypeResource;

@Component
public class CardTypeParser {

	@Autowired
	private CardParser cardParser;

	public CardType toModel(final CardTypeResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		CardType cardType = new CardType();
		cardType.setName(resource.getName());
		if (StringUtils.isNotBlank(resource.getImgUrl())) {
			cardType.setImgUrl(resource.getImgUrl());
		}
		return cardType;
	}

	public Set<CardTypeResource> toResources(final Set<CardType> cardsType) {
		Assert.notEmpty(cardsType, "The cardsType must not be empty!");
		Set<CardTypeResource> resources = new HashSet<>();
		cardsType.forEach(ct -> resources.add(toResource(ct)));
		return resources;
	}

	public CardTypeResource toResource(final CardType cardType) {
		Assert.notNull(cardType, "The cardType must not be null!");
		CardTypeResource resource = new CardTypeResource();
		resource.setName(cardType.getName());
		resource.setId(cardType.getId());
		if (StringUtils.isNotBlank(cardType.getImgUrl())) {
			resource.setImgUrl(cardType.getImgUrl());
		}
		if (!CollectionUtils.isEmpty(cardType.getCards())) {
			cardType.getCards().forEach(c -> {
				resource.addCards(cardParser.toResource(c));
			});
		}
		return resource;
	}

}
