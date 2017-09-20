package com.tsm.cards.definition.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.definition.service.BaseBuildDefResourceService;
import com.tsm.cards.definition.service.BaseManageWordService;
import com.tsm.cards.definition.service.KnownWordService;
import com.tsm.cards.definition.service.OxfordService;
import com.tsm.cards.definition.service.SynonymsBuildDefResourceService;
import com.tsm.cards.definition.service.SynonymsDefinitionService;
import com.tsm.cards.definition.service.SynonymsManageWordService;
import com.tsm.cards.documents.SynonymsDefinition;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;

@RestController
@RequestMapping(value = "/api/v1/definitions")
public class SynonymsDefinitionsController extends BaseDefinitionController<SynonymsDefinition> {

    @Autowired
    private KnownWordService knownWordService;

    @Autowired
    private SynonymsDefinitionService definitionSynonymsService;

    @Autowired
    private SynonymsManageWordService manageWordService;

    @Autowired
    private TrackWordsService trackWordsService;

    @Autowired
    private SynonymsBuildDefResourceService buildDefinitionsResourceService;

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
        return OxfordService.SYNONYMS;
    }

    @Override
    protected SynonymsDefinitionService getDefinitionService() {
        return definitionSynonymsService;
    }

    @Override
    protected BaseManageWordService<SynonymsDefinition> getManageWordService() {
        return manageWordService;
    }

    @Override
    protected BaseBuildDefResourceService getBuildDefinitionsResourceService() {
        return buildDefinitionsResourceService;
    }

    @RequestMapping(path = "/{word}/synonyms", method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public ResultResource findSynonyms(@PathVariable final String word) throws Exception {
        return findDefinition(word);
    }

}
