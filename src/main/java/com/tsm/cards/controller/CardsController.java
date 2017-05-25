package com.tsm.cards.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.cards.model.Definition;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.ProcessDefinitionsService;

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
    private ProcessDefinitionsService processDefinitionsService;

    @Autowired
    @Getter
    @Setter
    private BuildDefinitionsResourceService buildDefinitionsResourceService;

    @RequestMapping(method = POST)
    @ResponseStatus(OK)
    public List<DefinitionResource> getCards(final String word, final String definitionsIds) throws Exception {
        log.debug("Recieved a request to process these words [{}] and definitions [{}].", word, definitionsIds);

        Set<String> ids = processDefinitionsService.splitDefinitionsIds(definitionsIds);

        List<Definition> foundWords = processDefinitionsService.getDefinitionByDefinitionsIds(ids);

        return null;
    }

}
