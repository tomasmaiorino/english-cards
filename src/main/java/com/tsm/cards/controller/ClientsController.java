package com.tsm.cards.controller;

import com.tsm.cards.model.Client;
import com.tsm.cards.parser.ClientParser;
import com.tsm.cards.resources.ClientResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/v1/clients")
public class ClientsController extends RestBaseController<ClientResource, Client, Integer> {

	@Autowired
	private ClientService service;

	@Autowired
	private ClientParser parser;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public ClientResource save(@RequestBody final ClientResource resource, final HttpServletRequest request) {
		return super.save(resource);
	}
	
	@Override
	public BaseService<Client, Integer> getService() {
		return service;
	}

	@Override
	public IParser<ClientResource, Client> getParser() {
		return parser;
	}

}
