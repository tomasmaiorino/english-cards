package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.parser.CardTypeParser;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.CardTypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/cards-type")
@Slf4j
public class CardsTypeController extends BaseController<CardTypeResource, CardType, Integer> {

	@Autowired
	private CardTypeService service;

	@Autowired
	private CardTypeParser parser;

	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	public Set<CardTypeResource> findAll() {

		log.debug("Recieved a request to find all active card types.");

		Set<CardType> cards = service.findAllByStatus(CardTypeStatus.ACTIVE);

		Set<CardTypeResource> resources = new HashSet<>();
		if (!cards.isEmpty()) {
			resources = parser.toResources(cards);
		}

		log.debug("returning resources: [{}].", resources.size());

		return resources;
	}

	@Override
	public BaseService<CardType, Integer> getService() {
		return service;
	}

	@Override
	public IParser<CardTypeResource, CardType> getParser() {
		return parser;
	}

}
