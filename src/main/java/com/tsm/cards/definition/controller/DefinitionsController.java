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
import com.tsm.cards.definition.service.DefinitionBuildDefResourceService;
import com.tsm.cards.definition.service.DefinitionManageWordService;
import com.tsm.cards.definition.service.DefinitionService;
import com.tsm.cards.definition.service.KnownWordService;
import com.tsm.cards.definition.service.OxfordService;
import com.tsm.cards.documents.Definition;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;

@RestController
@RequestMapping(value = "/api/v1/definitions")
public class DefinitionsController extends BaseDefinitionController<Definition> {

    @Autowired
    private KnownWordService knownWordService;

    @Autowired
    private DefinitionService definitionSynonymsService;

    @Autowired
    private DefinitionManageWordService manageWordService;

    @Autowired
    private TrackWordsService trackWordsService;

    @Autowired
    private DefinitionBuildDefResourceService buildDefinitionsResourceService;

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
    protected DefinitionService getDefinitionService() {
        return definitionSynonymsService;
    }

    @Override
    protected BaseManageWordService<Definition> getManageWordService() {
        return manageWordService;
    }

    @Override
    protected BaseBuildDefResourceService getBuildDefinitionsResourceService() {
        return buildDefinitionsResourceService;
    }

    @RequestMapping(path = "/{word}", method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public ResultResource find(@PathVariable final String word) throws Exception {
        return findDefinition(word);
    }

}
