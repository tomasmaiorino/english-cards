package com.tsm.cards.definition.service;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.Entries;
import com.tsm.cards.documents.LexicalEntries;
import com.tsm.cards.documents.Results;
import com.tsm.cards.documents.Senses;
import com.tsm.cards.documents.Subsenses;
import com.tsm.cards.documents.Synonyms;
import com.tsm.cards.resources.DefinitionResource;

@FixMethodOrder(MethodSorters.JVM)
public class SynonymsBuildDefResourceServiceTest {

    @InjectMocks
    private SynonymsBuildDefResourceService service;
    private static final String SYNONYMS_TEXT_1 = random(20, true, true);
    private static final String SYNONYMS_TEXT_2 = random(20, true, true);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service.setMaxDefinitionSynonymsList(6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadResource_NullBaseDefinitionGiven_ShouldThrowException() {
        // Set up
        List<BaseDefinition> definition = null;

        // Do test
        service.loadResource(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadResource_EmptyBaseDefinitionGiven_ShouldThrowException() {
        // Set up
        List<BaseDefinition> definition = Collections.emptyList();

        // Do test
        service.loadResource(definition);
    }

    @Test
    public void findByWord_validWordGiven_KnownWordFound() {
        // Set up
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        List<BaseDefinition> definition = buildBaseDefinitions(validWords);

        // Do test
        List<DefinitionResource> result = service.loadResource(definition);

        // Gson gson = new GsonBuilder().create();
        //
        // Type listType = new TypeToken<ArrayList<BaseDefinitionResource>>() {
        // }.getType();
        //
        // System.out.println(gson.toJson(result, listType));

        // Assertions
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(false));
        assertThat(result.stream().map(s -> s.getSynonyms().containsValue(SYNONYMS_TEXT_1)).count() > 0l, is(true));
    }

    private List<BaseDefinition> buildBaseDefinitions(Set<String> words) {
        List<BaseDefinition> calls = new ArrayList<>();
        words.forEach(w -> {
            BaseDefinition call = new BaseDefinition();
            Results results = new Results();
            LexicalEntries lexicalEntries = new LexicalEntries();
            Entries entries = new Entries();
            Synonyms synonyms = new Synonyms();
            synonyms.setText(SYNONYMS_TEXT_1);
            synonyms.setId(SYNONYMS_TEXT_1);

            Senses senses = new Senses();
            senses.setSynonyms(Collections.singletonList(synonyms));
            senses.setId(UUID.randomUUID().toString());

            synonyms = new Synonyms();
            synonyms.setText(SYNONYMS_TEXT_2);
            synonyms.setId(SYNONYMS_TEXT_2);
            Subsenses subsenses = new Subsenses();
            senses.setSynonyms(Collections.singletonList(synonyms));

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
