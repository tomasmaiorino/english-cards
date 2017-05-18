package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.Entries;
import com.tsm.cards.model.LexicalEntries;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.model.Results;
import com.tsm.cards.model.Senses;
import com.tsm.cards.model.Subsenses;
import com.tsm.cards.resources.DefinitionsResource;

@FixMethodOrder(MethodSorters.JVM)
public class BuildDefinitionsResourceServiceTest {

    @InjectMocks
    private BuildDefinitionsResourceService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadResource_NullOriginalCallGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCall = null;

        // Do test
        service.loadResource(originalCall);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadResource_EmptyOriginalCallGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCall = Collections.emptyList();

        // Do test
        service.loadResource(originalCall);
    }

    @Test
    public void findByWord_validWordGiven_KnownWordFound() {
        // Set up
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        List<OriginalCall> originalCall = buildOriginalCalls(validWords);

        // Do test
        List<DefinitionsResource> result = service.loadResource(originalCall);

        // Assertions
        Assert.assertNotNull(result);
        ;
        Assert.assertTrue(!result.isEmpty());
        ;
    }

    private List<OriginalCall> buildOriginalCalls(Set<String> words) {
        List<OriginalCall> calls = new ArrayList<>();
        words.forEach(w -> {
            OriginalCall call = new OriginalCall();
            Results results = new Results();
            LexicalEntries lexicalEntries = new LexicalEntries();
            Entries entries = new Entries();

            Senses senses = new Senses();
            senses.setDefinitions(Collections.singletonList(w + " " + w));
            senses.setId(UUID.randomUUID().toString());

            Subsenses subsenses = new Subsenses();
            subsenses.setDefinitions(Collections.singletonList(w + " " + w));
            subsenses.setId(UUID.randomUUID().toString());
            senses.setSubsenses(Collections.singletonList(subsenses));

            entries.setSenses(Collections.singletonList(senses));

            lexicalEntries.setEntries(Collections.singletonList(entries));
            results.setLexicalEntries(Collections.singletonList(lexicalEntries));
            results.setId(w);
            call.setResults(Collections.singletonList(results));
            call.setId(w);
            calls.add(call);
        });
        return calls;
    }

}
