package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.CardType;
import com.tsm.cards.parser.CardTypeParser;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.service.CardTypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/cards-type")
@Slf4j
public class CardsTypeController extends BaseController {

    @Autowired
    private CardTypeService service;

    @Autowired
    private CardTypeParser parser;

    @RequestMapping(method = POST,
        consumes = JSON_VALUE,
        produces = JSON_VALUE)
    @ResponseStatus(CREATED)
    public CardTypeResource save(@RequestBody final CardTypeResource resource) {
        log.debug("Recieved a request to create a card type [{}].", resource);

        validate(resource, Default.class);

        CardType cardType = parser.toModel(resource);

        cardType = service.save(cardType);

        CardTypeResource result = parser.toResource(cardType);

        log.debug("returnig resource [{}].", result);

        return result;
    }

}
