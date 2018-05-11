package com.tsm.cards.definition.controller;

import com.tsm.cards.definition.service.*;
import com.tsm.cards.documents.SentencesDefinition;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/v1/definitions")
public class SentencesDefinitionsController extends BaseDefinitionController<SentencesDefinition> {

    @Autowired
    private KnownWordService knownWordService;

    @Autowired
    private SentencesDefinitionService sentencesDefinitionService;

    @Autowired
    private SentencesManageWordService manageWordService;

    @Autowired
    private TrackWordsService trackWordsService;

    @Autowired
    private SentencesBuildDefResourceService buildDefinitionsResourceService;

    @Override
    protected KnownWordService getKnownWordService() {
        return knownWordService;
    }

    @Override
    protected TrackWordsService getTrackWordsService() {
        return trackWordsService;
    }

    @Override
    protected String getServiceName() {
        return OxfordService.SENTENCES;
    }

    @Override
    protected SentencesDefinitionService getDefinitionService() {
        return sentencesDefinitionService;
    }

    @Override
    protected BaseManageWordService<SentencesDefinition> getManageWordService() {
        return manageWordService;
    }

    @Override
    protected BaseBuildDefResourceService getBuildDefinitionsResourceService() {
        return buildDefinitionsResourceService;
    }

    @RequestMapping(path = "/{word}/sentences", method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public ResultResource findSentences(@PathVariable final String word) throws Exception {
        return findDefinition(word);
    }

}
