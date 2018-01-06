package com.tsm.cards.parser;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.resources.IParser;

@Component
public class CardParser implements IParser<CardResource, Card> {

	public Card toModel(final CardResource resource, final CardType cardType) {
		Assert.notNull(cardType, "The cardType must not be null!");
		Assert.notNull(resource, "The resource must not be null!");
		return Card.CardBuilder
				.Card(resource.getName(), cardType, CardStatus.valueOf(resource.getStatus()), resource.getImgUrl())
				.build();
	}

	public CardResource toResource(final Card card) {
		Assert.notNull(card, "The card must not be null!");
		CardResource cardResource = new CardResource();
		cardResource.setId(card.getId());
		cardResource.setCardType(card.getCardType().getId());
		cardResource.setImgUrl(card.getImgUrl());
		cardResource.setName(card.getName());
		cardResource.setStatus(card.getStatus().name());
		return cardResource;
	}

	@Override
	public Card toModel(final CardResource resource) {
		return null;
	}

	@Override
	public Set<CardResource> toResources(final Set<Card> models) {
		return null;
	}

}
