package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Content;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.parser.ContentParser;
import com.tsm.cards.resources.ContentResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.ContentService;
import com.tsm.cards.service.ContentTypeService;
import com.tsm.cards.util.ErrorCodes;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/contents")
@Slf4j
public class ContentsController extends RestBaseController<ContentResource, Content, Integer> {

	@Autowired
	private ContentService service;

	@Autowired
	private ContentTypeService contentTypeService;

	@Autowired
	private ContentParser parser;

	@Override
	public BaseService<Content, Integer> getService() {
		return service;
	}

	@Override
	public IParser<ContentResource, Content> getParser() {
		return parser;
	}

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@Override
	public ContentResource save(@RequestBody final ContentResource resource) {
		log.debug("Recieved a request to create a card [{}].", resource);

		validate(resource, Default.class);

		ContentType contentType = recoverContentType(resource.getContentType());

		Content content = parser.toModel(resource, contentType);

		content = service.save(content);

		ContentResource result = parser.toResource(content);

		log.debug("returning resource [{}].", result);

		return result;
	}

	@RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(OK)
	@Override
	public ContentResource update(@PathVariable final Integer id, @RequestBody final ContentResource resource) {
		log.debug("Recieved a request to update a card [{}].", resource);

		validate(resource, Default.class);

		Content origin = service.findById(id);

		ContentType contentType = recoverContentType(resource.getContentType());

		Content model = parser.toModel(resource, contentType);

		origin = service.update(origin, model);

		ContentResource result = parser.toResource(origin);

		log.debug("returning resource [{}].", result);

		return result;
	}
	
	@RequestMapping(method = GET, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(OK)
	public ContentResource findById(@PathVariable Integer id) {
		return super.findById(id);
	}

	@Override
	@RequestMapping(method = DELETE, path = "/{id}", produces = JSON_VALUE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		super.delete(id);
	}
	
	private ContentType recoverContentType(final Integer contentTypeId) {
		ContentType type = null;
		try {
			type = contentTypeService.findById(contentTypeId);
		} catch (ResourceNotFoundException e) {
			throw new BadRequestException(ErrorCodes.CONTENT_TYPE_NOT_FOUND);
		}
		return type;
	}

}
