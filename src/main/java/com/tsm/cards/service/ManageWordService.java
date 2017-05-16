package com.tsm.cards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.OriginalCall;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManageWordService {

    @Autowired
    @Getter
    @Setter
    private OriginalCallService originalCallService;

    @Autowired
    @Getter
    @Setter
    private OxfordService oxfordService;

    public OriginalCall createOriginalCall(String word) throws Exception {
        Assert.hasText(word, "The word must not be empty or null.");
        log.debug("creating original call for known word [{}] .", word);

        OriginalCall originalCall = oxfordService.findWordDefinition(word);
        originalCall.setId(word);

        return originalCallService.save(originalCall);
    }

}
