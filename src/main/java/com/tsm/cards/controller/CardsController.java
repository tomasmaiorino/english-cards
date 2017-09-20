package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.CardType;
import com.tsm.cards.parser.CardParser;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.service.CardService;
import com.tsm.cards.service.CardTypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/cards")
@Slf4j
public class CardsController extends BaseController {

    @Autowired
    private CardService service;

    @Autowired
    private CardTypeService cardTypeService;

    @Autowired
    private CardParser parser;

    @RequestMapping(method = POST,
        consumes = JSON_VALUE,
        produces = JSON_VALUE)
    @ResponseStatus(CREATED)
    public CardResource save(@RequestBody final CardResource resource) {
        log.debug("Recieved a request to create a card  [{}].", resource);

        validate(resource, Default.class);

        CardType cardType = cardTypeService.findById(resource.getCardType());

        Card card = parser.toModel(resource, cardType);

        card = service.save(card);

        CardResource result = parser.toResource(card);

        log.debug("returning resource [{}].", result);

        return result;
    }

    @RequestMapping(method = PUT,
        path = "/{id}",
        consumes = JSON_VALUE,
        produces = JSON_VALUE)
    @ResponseStatus(CREATED)
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

}
