package com.tsm.cards.controller;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.CardType;
import com.tsm.cards.parser.CardParser;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.CardService;
import com.tsm.cards.service.CardTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api/v1/cards")
@Slf4j
public class CardsController extends RestBaseController<CardResource, Card, Integer> {

	@Autowired
	private CardService service;

	@Autowired
	private CardTypeService cardTypeService;

	@Autowired
	private CardParser parser;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@Override
	public CardResource save(@RequestBody final CardResource resource) {
		log.debug("Recieved a request to create a card [{}].", resource);

		validate(resource, Default.class);

		CardType cardType = cardTypeService.findById(resource.getCardType());

		Card card = parser.toModel(resource, cardType);

		card = service.save(card);

		CardResource result = parser.toResource(card);

		log.debug("returning resource [{}].", result);

		return result;
	}

	@RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@Override
	public CardResource update(@PathVariable final Integer id, @RequestBody final CardResource resource) {
		log.debug("Recieved a request to update a card [{}].", resource);

		validate(resource, Default.class);

		Card origin = service.findById(id);

		CardType cardType = cardTypeService.findById(resource.getCardType());

		Card model = parser.toModel(resource, cardType);

		origin = service.update(origin, model);

		CardResource result = parser.toResource(origin);

		log.debug("returning resource [{}].", result);

		return result;
	}

	@RequestMapping(method = GET, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(OK)
	public CardResource findById(@PathVariable Integer id) {
		return super.findById(id);
	}

	@Override
	@RequestMapping(method = DELETE, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		super.delete(id);
	}

	@Override
	public BaseService<Card, Integer> getService() {
		return service;
	}

	@Override
	public IParser<CardResource, Card> getParser() {
		return parser;
	}
}
