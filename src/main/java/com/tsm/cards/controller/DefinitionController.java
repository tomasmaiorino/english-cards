package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.DefinitionService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.ProcessWordsService;

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
    public List<DefinitionResource> getDefinitions(@RequestBody final DefinitionResource resource) throws Exception {
        log.debug("Recieved a request to process these words [{}].", resource);

        Set<String> validWords = getValidWords(resource);

        List<Definition> cachedWords = processWordsService.getCachedWords(validWords);

        Set<String> notCachedWords = null;

        if (cachedWords.size() < validWords.size()) {
            notCachedWords = processWordsService.getNotCachedWords(cachedWords, validWords);
        }

        processNotCachedWords(cachedWords, notCachedWords);

        List<DefinitionResource> resourceRet = new ArrayList<>();
        if (!cachedWords.isEmpty()) {
            resourceRet = buildDefinitionsResourceService.loadResource(cachedWords);
        }

        log.debug("Sending response with definition results: [{}].", resourceRet);

        return resourceRet;
    }

    private Set<String> getValidWords(final DefinitionResource resource) {
        Set<String> validWords = processWordsService.getValidWords(resource.getWords());

        if (validWords == null || validWords.isEmpty()) {
            throw new BadRequestException(String.format("None valid words was given: %s", resource.getWords()));
        }
        return validWords;
    }

    private void processNotCachedWords(List<Definition> cachedWords, Set<String> notCachedWords) {
        List<Definition> notCached = new ArrayList<>();

        if (notCachedWords != null && !notCachedWords.isEmpty()) {

            notCachedWords.forEach(n -> {
                try {
                    notCached.add(manageWordService.createDefinition(n));
                } catch (Exception e) {
                    log.error("Error creating original call from word: [{}].", n);
                }
            });
            cachedWords.addAll(notCached);
        }
    }
}
