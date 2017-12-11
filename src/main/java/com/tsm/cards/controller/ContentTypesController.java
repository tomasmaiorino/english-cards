package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.HashSet;
import java.util.Set;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.parser.ContentTypeParser;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.service.ContentTypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/content-types")
@Slf4j
public class ContentTypesController extends BaseController {

	@Autowired
	private ContentTypeService service;

	@Autowired
	private ContentTypeParser parser;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	public ContentTypeResource save(@RequestBody final ContentTypeResource resource) {
		log.debug("Recieved a request to create a card type [{}].", resource);

		validate(resource, Default.class);

		ContentType contentType = parser.toModel(resource);

		contentType = service.save(contentType);

		ContentTypeResource result = parser.toResource(contentType);

		log.debug("returning resource [{}].", result);

		return result;
	}

	@RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	public ContentTypeResource update(@PathVariable final Integer id, @RequestBody final ContentTypeResource resource) {
		log.debug("Recieved a request to update a card type [{}].", resource);

		validate(resource, Default.class);

		ContentType origin = service.findById(id);

		ContentType model = parser.toModel(resource);

		origin = service.update(origin, model);

		ContentTypeResource result = parser.toResource(origin);

		log.debug("returning resource [{}].", result);

		return result;
	}

	@RequestMapping(path = "/{id}", method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	public ContentTypeResource findById(@PathVariable final Integer id) {

		log.debug("Recieved a request to find an cart type by id [{}].", id);

		ContentType cardtype = service.findById(id);

		ContentTypeResource resource = parser.toResource(cardtype);

		log.debug("returning resource: [{}].", resource);

		return resource;
	}

	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
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

}
