package com.tsm.cards.definition.controller;

import com.tsm.cards.controller.BaseController;
import com.tsm.cards.definition.service.BaseBuildDefResourceService;
import com.tsm.cards.definition.service.BaseDefinitionService;
import com.tsm.cards.definition.service.BaseManageWordService;
import com.tsm.cards.definition.service.KnownWordService;
import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseDefinitionController<T extends BaseDefinition> extends BaseController {

    protected abstract KnownWordService getKnownWordService();

    protected abstract TrackWordsService getTrackWordsService();

    protected abstract String getServiceName();

    protected abstract BaseDefinitionService<T, ?> getDefinitionService();

    protected abstract BaseManageWordService<T> getManageWordService();

    protected abstract BaseBuildDefResourceService getBuildDefinitionsResourceService();

    protected Optional<T> findCachedWord(final String word) {
        return getDefinitionService().findOptionalDefinitionById(word);
    }

    public ResultResource findDefinition(@PathVariable final String word) throws Exception {
        log.debug("Recieved a request to find a word [{}] for the service [{}].", word, getServiceName());

        getKnownWordService().findByWord(word.toLowerCase());

        String lowerWord = word.toLowerCase();

        Optional<T> optionalDefinition = findCachedWord(lowerWord);

        T definition = null;

        if (!optionalDefinition.isPresent()) {
            log.info("Word not cached [{}] :(", lowerWord);
            definition = getManageWordService().createDefinition(lowerWord);
        } else {
            log.info("Word cached [{}] :)", lowerWord);
            definition = optionalDefinition.get();
        }

        List<DefinitionResource> resourceRet = getBuildDefinitionsResourceService().loadResource(Collections.singletonList(definition));

        ResultResource result = new ResultResource();
        result.setDefinitions(new HashSet<>(resourceRet));
        result.setGivenWords(Collections.singleton(word));

        log.debug("Sending response with result resource: [{}].", result);

        return result;
    }

}
