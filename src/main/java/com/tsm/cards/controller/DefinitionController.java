package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.DefinitionService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.ProcessWordsService;
import com.tsm.cards.service.TrackWordsService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/definitions")
@Slf4j
public class DefinitionController extends BaseController {

	private static final String APPLICATION_JSON_UTF_8 = "application/json; charset=UTF-8";

	@Autowired
	@Getter
	@Setter
	private KnownWordService knownWordService;

	@Autowired
	@Getter
	@Setter
	private DefinitionService definitionService;

	@Autowired
	@Getter
	@Setter
	private ManageWordService manageWordService;

	@Autowired
	@Getter
	@Setter
	private ProcessWordsService processWordsService;

	@Autowired
	@Getter
	@Setter
	private TrackWordsService trackWordsService;

	@Autowired
	@Getter
	@Setter
	private BuildDefinitionsResourceService buildDefinitionsResourceService;

	@RequestMapping(path = "/{word}", method = GET)
	@ResponseStatus(OK)
	public Definition findByWord(@PathVariable final String word) throws Exception {
		log.debug("Recieved a request to find a word [{}].", word);

		knownWordService.findByWord(word.toLowerCase());

		String lowerWord = word.toLowerCase();

		Definition definition = null;

		Optional<Definition> optionalDefinition = definitionService.findOptionalDefinitionById(lowerWord);

		if (!optionalDefinition.isPresent()) {
			log.info("Word not cached [{}] :(", lowerWord);
			definition = manageWordService.createDefinition(lowerWord);
		}

		log.debug("Sending response with definition resource: [{}].", definition);

		return definition;
	}

	@RequestMapping(method = POST, produces = APPLICATION_JSON_UTF_8, consumes = APPLICATION_JSON_UTF_8)
	@ResponseStatus(CREATED)
	public ResultResource getDefinitions(@RequestBody final DefinitionResource resource) throws Exception {
		log.debug("Recieved a request to process these words [{}].", resource);

		Set<String> givenWords = resource.getWords();

		Set<String> validWords = getValidWords(resource);

		Set<String> invalidWords = processWordsService.getInvalidWords(validWords, resource.getWords());

		Set<String> notCachedWords = null;

		List<Definition> cachedWords = processWordsService.getCachedWords(validWords);

		if (cachedWords.size() < validWords.size()) {
			notCachedWords = processWordsService.getNotCachedWords(cachedWords, validWords);
		}

		Set<String> notFoundWords = new HashSet<>();

		processNotCachedWords(cachedWords, notCachedWords, notFoundWords);

		processTrackWords(cachedWords);

		List<DefinitionResource> resourceRet = new ArrayList<>();
		if (!cachedWords.isEmpty()) {
			resourceRet = buildDefinitionsResourceService.loadResource(cachedWords);
		}

		ResultResource result = new ResultResource();
		result.setDefinitions(new TreeSet<>(resourceRet));
		result.setGivenWords(givenWords);
		result.setInvalidWords(invalidWords);
		result.setNotFoundWords(notFoundWords);

		log.debug("Sending response with definition results: [{}].", result);
		return result;
	}

	private void processTrackWords(List<Definition> cachedWords) {
		log.info("Tracking words ->");
		cachedWords.forEach(w -> {
			trackWordsService.incrementTrack(w.getId());
		});
		log.info("Tracking words <-");
	}

	private Set<String> getValidWords(final DefinitionResource resource) {
		Set<String> validWords = processWordsService.getValidWords(resource.getWords());

		if (validWords == null || validWords.isEmpty()) {
			throw new BadRequestException(String.format("None valid words was given: %s", resource.getWords()));
		}
		return validWords;
	}

	private void processNotCachedWords(final List<Definition> cachedWords, final Set<String> notCachedWords,
			final Set<String> notFoundWords) {
		List<Definition> notCached = new ArrayList<>();

		if (notCachedWords != null && !notCachedWords.isEmpty()) {

			notCachedWords.forEach(n -> {
				try {
					notCached.add(manageWordService.createDefinition(n));
				} catch (ResourceNotFoundException e) {
					notFoundWords.add(n);
					log.error("Word not found [{}].", n);
				} catch (Exception e) {
					log.error("Error creating definition from word: [{}].", n, e);
				}
			});
			cachedWords.addAll(notCached);
		}
	}
}
