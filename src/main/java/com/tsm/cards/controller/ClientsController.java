package com.tsm.cards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.Client;
import com.tsm.cards.parser.ClientParser;
import com.tsm.cards.resources.ClientResource;
import com.tsm.cards.resources.IParser;
import com.tsm.cards.service.BaseService;
import com.tsm.cards.service.ClientService;

@RestController
@RequestMapping(value = "/api/v1/clients")
public class ClientsController extends RestBaseController<ClientResource, Client, Integer> {

	@Autowired
	private ClientService service;

	@Autowired
	private ClientParser parser;

	@Override
	public BaseService<Client, Integer> getService() {
		return service;
	}

	@Override
	public IParser<ClientResource, Client> getParser() {
		return parser;
	}

}
