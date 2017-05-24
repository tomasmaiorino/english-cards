package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.resources.DefinitionsResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OriginalCallService;
import com.tsm.cards.service.ProcessWordsService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/definitions")
@Slf4j
public class DefinitionController extends BaseController {

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

    @Autowired
    @Getter
    @Setter
    private BuildDefinitionsResourceService buildDefinitionsResourceService;

    @RequestMapping(path = "/{word}", method = GET)
    @ResponseStatus(OK)
    public OriginalCall findByWord(@PathVariable final String word) throws Exception {
        log.debug("Recieved a request to find a word [{}].", word);

        knownWordService.findByWord(word.toLowerCase());

        OriginalCall originalCall = null;

        try {

            originalCall = originalCallService.findById(word.toLowerCase());

        } catch (ResourceNotFoundException e) {
            log.debug("Word not cached [{}] :(", word);
            originalCall = manageWordService.createOriginalCall(word.toLowerCase());
        }

        log.debug("Sending response with definition resource: [{}].", originalCall);

        return originalCall;
    }

    @RequestMapping(method = POST, produces = "application/json; charset=UTF-8")
    @ResponseStatus(CREATED)
    public List<DefinitionsResource> getDefinitions(final DefinitionsResource resource) throws Exception {
        log.debug("Recieved a request to process these words [{}].", resource);

        Set<String> validWords = processWordsService.getValidWords(resource.getWords());

        if (validWords == null || validWords.isEmpty()) {
            throw new BadRequestException(String.format("None valid words was given: %s", resource));
        }

        List<OriginalCall> cachedWords = processWordsService.getCachedWords(validWords);

        Set<String> notCachedWords = null;

        if (!cachedWords.isEmpty() && cachedWords.size() < validWords.size()) {
            notCachedWords = processWordsService.getNotCachedWords(cachedWords, validWords);
        }

        processNotCachedWords(cachedWords, notCachedWords);

        List<DefinitionsResource> resourceRet = new ArrayList<>();
        if (!cachedWords.isEmpty()) {
        	resourceRet = buildDefinitionsResourceService.loadResource(cachedWords);
        }

        log.debug("Sending response with definition original call results: [{}].", resourceRet);

        return resourceRet;
    }

    private void processNotCachedWords(List<OriginalCall> cachedWords, Set<String> notCachedWords) {
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
    }
}
