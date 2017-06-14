package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.KnownWord;
import com.tsm.cards.model.SetWordsCards;
import com.tsm.cards.resources.SetWordsCardsResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ProcessDefinitionsService;
import com.tsm.cards.service.SetWordsCardsService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/set-cards")
@Slf4j
public class SetWordsCardsController {

	@Autowired
	@Getter
	@Setter
	private ProcessDefinitionsService processDefinitionsService;

	@Autowired
	@Getter
	@Setter
	private SetWordsCardsService setWordsCardsService;

	@Autowired
	@Getter
	@Setter
	private KnownWordService knownWordService;

	@Autowired
	@Getter
	@Setter
	private BuildDefinitionsResourceService buildDefinitionsResourceService;

	@RequestMapping(method = POST)
	@ResponseStatus(OK)
	public List<SetWordsCardsResource> create(final SetWordsCardsResource resource) throws Exception {
		log.debug("Recieved a request to create a new set of cards [{}].", resource);

		if (resource.getWords() != null && !resource.getWords().isEmpty()) {
			log.info("load set words cards using given words.");
		} else {
			log.info("load new set words cards using.");
			List<SetWordsCards> allSetsAvailables = setWordsCardsService.findAll();
		}

		return null;
	}

	private Set<String> getKnowsWordsIds(final List<SetWordsCards> allSetsAvailables) {
		Set<String> ids = new HashSet<>();
		allSetsAvailables.forEach(s -> {
			s.getKnownWords().forEach(k -> {
				ids.add(k.getId());
			});
		});
		return ids;
	}

}
