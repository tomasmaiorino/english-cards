package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
public class ContentTypesController extends RestBaseController<ContentTypeResource, ContentType, Integer> {

	@Autowired
	private ContentTypeService service;

	@Autowired
	private ContentTypeParser parser;

	//
	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@Override
	public Set<ContentTypeResource> findAll(@RequestParam(value = "q", required = false) String q) {

		log.debug("Recieved a request to find all active card types. q = [{}]", q);

		Set<ContentType> contents = new HashSet<>();

		if (StringUtils.isNotBlank(q)) {
			ContentType contentType = service.findByName(q);
			contents.add(contentType);
		} else {
			contents = service.findAllByStatus(ContentTypeStatus.ACTIVE);
		}

		Set<ContentTypeResource> resources = new HashSet<>();
		if (!contents.isEmpty()) {
			resources = parser.toResources(contents);
		}

		log.debug("returning resources: [{}].", resources.size());

		return resources;
	}
	
	@Override
	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public ContentTypeResource save(@RequestBody final ContentTypeResource resource) {
		return super.save(resource);
	}

	@Override
	@RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@ResponseBody
	public ContentTypeResource update(@PathVariable Integer id, @RequestBody final ContentTypeResource resource) {
		return super.update(id, resource);
	}

	@RequestMapping(method = GET, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(OK)
	public ContentTypeResource findById(@PathVariable Integer id) {
		return super.findById(id);
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
