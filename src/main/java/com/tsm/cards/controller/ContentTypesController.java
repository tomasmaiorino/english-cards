package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.parser.ContentTypeParser;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.ContentTypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/content-types")
@Slf4j
public class ContentTypesController extends BaseController<ContentTypeResource, ContentType, Integer> {

	@Autowired
	private ContentTypeService service;

	@Autowired
	private ContentTypeParser parser;

	//
	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@Override
	public Set<ContentTypeResource> findAll() {

		log.debug("Recieved a request to find all active card types.");

		Set<ContentType> cards = service.findAllByStatus(ContentTypeStatus.ACTIVE);

		Set<ContentTypeResource> resources = new HashSet<>();
		if (!cards.isEmpty()) {
			resources = parser.toResources(cards);
		}

		log.debug("returning resources: [{}].", resources.size());

		return resources;
	}

	@Override
	public BaseService<ContentType, Integer> getService() {
		return service;
	}

	@Override
	public IParser<ContentTypeResource, ContentType> getParser() {
		return parser;
	}

}
