package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.Card;
import com.tsm.cards.service.CardService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/cards")
@Slf4j
public class CardsController {

	@Autowired
	@Getter
	@Setter
	private CardService cardService;

	@RequestMapping(method = POST)
	@ResponseStatus(OK)

	public ResponseEntity<?> save(@Valid @RequestBody final Card card, Errors errors) throws Exception {
		log.debug("Recieved a request to create a card [{}].", card);

		
		if (errors.hasErrors()) {



		}

		cardService.save(card);

		log.debug("returnig card [{}].", card);

		return null;
	}

}
