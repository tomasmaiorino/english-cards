package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.resources.DefinitionsResource;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OriginalCallService;
import com.tsm.cards.service.ProcessWordsService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/definition")
@Slf4j
public class DefinitionController {

	@Autowired
	@Getter
	@Setter
	private KnownWordService knownWordService;

	@Autowired
	@Getter
	@Setter
	private OriginalCallService originalCallService;

	@Autowired
	@Getter
	@Setter
	private ManageWordService manageWordService;

	@Autowired
	@Getter
	@Setter
	private ProcessWordsService processWordsService;

	@RequestMapping(path = "/{word}", method = GET)
	@ResponseStatus(OK)
	public OriginalCall findByWord(@PathVariable final String word) throws Exception {
		log.debug("Recieved a request to find a word [{}].", word);

		knownWordService.findByWord(word.toLowerCase());

		OriginalCall originalCall = null;

		try {

			originalCall = originalCallService.findOriginalCallById(word.toLowerCase());

		} catch (ResourceNotFoundException e) {
			log.debug("Word not cached [{}] :(", word);
			originalCall = manageWordService.createOriginalCall(word.toLowerCase());
		}

		log.debug("Sending response with definition resource: [{}].", originalCall);

		return originalCall;
	}

	private Set<String> getValidWords(String words) {
		Set<String> receivedWords = processWordsService.splitWords(words);
		return processWordsService.getValidWords(receivedWords);
	}

	@RequestMapping(method = POST)
	@ResponseStatus(OK)
	public List<DefinitionsResource> getDefinitions(final String words) throws Exception {
		log.debug("Recieved a request to process these words [{}].", words);

		Set<String> validWords = getValidWords(words);

		List<OriginalCall> cachedWords = processWordsService.getCachedWords(validWords);

		Set<String> notCachedWords = null;

		if (!cachedWords.isEmpty() && cachedWords.size() < validWords.size()) {
			notCachedWords = processWordsService.getNotCachedWords(cachedWords, validWords);
		}

		List<OriginalCall> notCached = new ArrayList<>();

		if (notCachedWords != null && !notCachedWords.isEmpty()) {

			notCachedWords.forEach(n -> {
				try {
					notCached.add(manageWordService.createOriginalCall(n));
				} catch (Exception e) {
					log.error("Error creating original call from word: [{}].", n);
				}
			});
			cachedWords.addAll(notCached);
		}

		List<DefinitionsResource> resource = new ArrayList<>();
		if (!cachedWords.isEmpty()) {
			resource = loadResource(cachedWords);
		}

		log.debug("Sending response with definition original call results: [{}].", resource);

		return resource;
	}

	private List<DefinitionsResource> loadResource(List<OriginalCall> cachedWords) {
		List<DefinitionsResource> resources = new ArrayList<>();

		cachedWords.forEach(o -> {
			o.getResults().forEach(r -> {
				r.getLexicalEntries().forEach(l -> {
					l.getEntries().forEach(e -> {
						DefinitionsResource resource = new DefinitionsResource();
						Map<String, String> definitions = new HashMap<>();
						e.getSenses().forEach(s -> {
							resource.setWord(r.getId());
							definitions.put(s.getId(), s.getDefinitions().get(0));
							s.getSubsenses().forEach(ss -> {
								definitions.put(ss.getId(), ss.getDefinitions().get(0));
							});
						});
						resource.setDefinitions(definitions);
						resources.add(resource);
					});
				});
			});
		});
		return resources;
	}
}
