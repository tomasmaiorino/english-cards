package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.exceptions.BadRequestException;
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
@RequestMapping(value = "/api/v1/definitions")
@Slf4j
public class DefinitionController extends BaseController {

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

    @RequestMapping(path = "/{word}", method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public ResultResource findByWord(@PathVariable final String word) throws Exception {
        log.debug("Recieved a request to find a word [{}].", word);

        knownWordService.findByWord(word.toLowerCase());

        String lowerWord = word.toLowerCase();

        Definition definition = null;

        Optional<Definition> optionalDefinition = definitionService.findOptionalDefinitionById(lowerWord);

        if (!optionalDefinition.isPresent()) {
            log.info("Word not cached [{}] :(", lowerWord);
            definition = manageWordService.createDefinition(lowerWord);
        } else {
            definition = optionalDefinition.get();
        }

        List<DefinitionResource> resourceRet = buildDefinitionsResourceService
            .loadResource(Collections.singletonList(definition));

        if (resourceRet.isEmpty()) {
            throw new BadRequestException("");
        }

        ResultResource result = new ResultResource();
        result.setDefinitions(new HashSet<>(resourceRet));
        result.setGivenWords(Collections.singleton(word));
        
        log.debug("Sending response with result resource: [{}].", result);

        return result;
    }
}
