package com.tsm.cards.controller;

import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.parser.CardTypeParser;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.CardTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api/v1/cards-type")
@Slf4j
public class CardsTypeController extends RestBaseController<CardTypeResource, CardType, Integer> {

	@Autowired
	private CardTypeService service;

	@Autowired
	private CardTypeParser parser;

	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	public Set<CardTypeResource> findAll(@RequestParam(value = "q", required = false) String q) {

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
	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public CardTypeResource save(@RequestBody final CardTypeResource resource) {
		return super.save(resource);
	}

	@Override
	@RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public CardTypeResource update(@PathVariable Integer id, @RequestBody final CardTypeResource resource) {
		return super.update(id, resource);
	}

	@RequestMapping(method = GET, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(OK)
	public CardTypeResource findById(@PathVariable Integer id) {
		return super.findById(id);
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
