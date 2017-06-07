package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.builders.DefinitionResourceBuilder;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.model.Entries;
import com.tsm.cards.model.LexicalEntries;
import com.tsm.cards.model.Results;
import com.tsm.cards.model.Senses;
import com.tsm.cards.model.Subsenses;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.DefinitionService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OxfordService;
import com.tsm.cards.service.ProcessWordsService;
import com.tsm.cards.service.TrackWordsService;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionControllerTest {

    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "oxford.json";

    @Mock
    private KnownWordService mockKnowService;

    @Mock
    private OxfordService mockOxfordService;

    @InjectMocks
    private DefinitionController controller;

    @Mock
    private DefinitionService mockDefinitionService;

    @Mock
    private ManageWordService mockManageWordService;

    @Mock
    private ProcessWordsService mockProcessWordsService;

    @Mock
    private TrackWordsService mockTrackWordsService;

    @Mock
    private BuildDefinitionsResourceService mockBuildDefinitionsResourceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByWord_InvalidWordGiven_ShouldReturnError() throws Exception {
        // Set up
        String word = "home";

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findByWord(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());

        verifyZeroInteractions(mockOxfordService, mockDefinitionService, mockTrackWordsService,
            mockProcessWordsService);
    }

    @Test
    public void findByWord_InvalidWordGiven_ShouldReturnNotFoundError() throws Exception {
        // Set up
        String word = "home";

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase())).thenReturn(Optional.empty());
        when(mockManageWordService.createDefinition(word.toLowerCase())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findByWord(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockManageWordService).createDefinition(word.toLowerCase());
        verifyZeroInteractions(mockOxfordService, mockTrackWordsService, mockProcessWordsService);
    }

    @Test
    public void findByWord_CreatedNewDefinition() throws Exception {
        // Set up
        String word = "home";
        Definition definition = new Definition();

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word)).thenReturn(Optional.empty());
        when(mockManageWordService.createDefinition(word.toLowerCase())).thenReturn(definition);

        // Do test
        Definition result = controller.findByWord(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word);
        verify(mockManageWordService).createDefinition(word.toLowerCase());
        verifyZeroInteractions(mockOxfordService, mockTrackWordsService, mockProcessWordsService);
        assertThat(result, is(definition));
    }

    @Test
    public void getDefinitions_InvalidWordsGiven_ShouldReturnNotFoundError() throws Exception {
        // Set up
        Set<String> words = new HashSet<>();
        words.add("home");
        words.add("car");
        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();

        // Expectations
        when(mockProcessWordsService.getValidWords(resource.getWords())).thenThrow(IllegalArgumentException.class);

        // Do test
        try {
            controller.getDefinitions(resource);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verify(mockProcessWordsService).getValidWords(words);
        verify(mockProcessWordsService, never()).getCachedWords(words);
        verifyZeroInteractions(mockManageWordService, mockOxfordService, mockTrackWordsService,
            mockProcessWordsService);
    }

    @Test
    public void getDefinitions_CachedWordsGiven_Definition() throws Exception {
        // Set up
        Set<String> validWords = new HashSet<>();
        validWords.add("car");
        validWords.add("home");
        DefinitionResource resource = new DefinitionResourceBuilder().words(validWords).build();

        List<Definition> cachedWords = buildDefinitions(validWords);
        List<DefinitionResource> definitionsResources = buildDefinitionsResource(cachedWords, null);

        // Expectations
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);
        when(mockProcessWordsService.getInvalidWords(validWords, validWords)).thenReturn(Collections.emptySet());

        // Do test
        ResultResource result = controller.getDefinitions(resource);

        // Assertions
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService, never()).getNotCachedWords(cachedWords, validWords);
        verifyZeroInteractions(mockManageWordService);
        assertThat(result.getDefinitions().containsAll(definitionsResources), is(true));
    }

    @Test
    public void getDefinitions_CachedNotCachedWordsGiven_Definition() throws Exception {
        // Set up
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        validWords.add("drive");

        DefinitionResource resource = new DefinitionResourceBuilder().words(validWords).build();

        Set<String> invalidWords = new HashSet<>();
        invalidWords.add("xpto");

        Set<String> cachedWord = new HashSet<>();
        cachedWord.add("home");

        String word = "car";

        Set<String> notCached = new HashSet<>();
        notCached.add(word);

        List<Definition> cachedWords = buildDefinitions(cachedWord);
        List<Definition> notCachedDefinition = buildDefinitions(notCached);
        List<DefinitionResource> definitionsResources = buildDefinitionsResource(cachedWords, notCachedDefinition);

        // Expectations
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockProcessWordsService.getNotCachedWords(cachedWords, validWords)).thenReturn(notCached);
        when(mockManageWordService.createDefinition(word)).thenReturn(notCachedDefinition.iterator().next());
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);
        when(mockProcessWordsService.getInvalidWords(validWords, validWords)).thenReturn(invalidWords);

        // Do test
        ResultResource result = controller.getDefinitions(resource);

        // Assertions
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService).getNotCachedWords(cachedWords, validWords);
        assertThat(result, notNullValue());
        assertThat(result.getDefinitions().size(), is(2));
        assertThat(result.getDefinitions().containsAll(definitionsResources), is(true));
        assertThat(result.getInvalidWords(), is(invalidWords));
    }

    @Test
    public void getDefinitions_CachedNotCachedWordsGiven_Definition_ExternalCallError() throws Exception {
        // Set up
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        validWords.add("drive");
        validWords.add("women");
        DefinitionResource resource = new DefinitionResourceBuilder().words(validWords).build();

        Set<String> cachedWord = new HashSet<>();
        cachedWord.add("home");

        String word = "car";
        String wordTwo = "drive";
        String women = "women";

        Set<String> notCached = new HashSet<>();
        notCached.add(word);
        notCached.add(wordTwo);
        notCached.add(women);

        Set<String> expectedCacheWords = new HashSet<>();
        expectedCacheWords.add(word);

        List<Definition> cachedWords = buildDefinitions(cachedWord);
        List<Definition> notCachedDefinition = buildDefinitions(expectedCacheWords);
        List<DefinitionResource> definitionsResources = buildDefinitionsResource(cachedWords, notCachedDefinition);

        // Expectations
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockProcessWordsService.getNotCachedWords(cachedWords, validWords)).thenReturn(notCached);
        when(mockManageWordService.createDefinition(word)).thenReturn(notCachedDefinition.iterator().next());
        when(mockManageWordService.createDefinition(wordTwo)).thenThrow(Exception.class);
        when(mockManageWordService.createDefinition(women)).thenThrow(ResourceNotFoundException.class);
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);
        when(mockProcessWordsService.getInvalidWords(validWords, validWords)).thenReturn(Collections.emptySet());

        // Do test
        ResultResource result = controller.getDefinitions(resource);

        // Assertions
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService).getNotCachedWords(cachedWords, validWords);
        assertThat(result, notNullValue());
        assertThat(result.getDefinitions().size(), is(2));
        assertThat(result.getDefinitions().containsAll(definitionsResources), is(true));
        assertThat(result.getNotFoundWords().contains(women), is(true));

    }

    private List<DefinitionResource> buildDefinitionsResource(List<Definition> notCachedDefinition) {
        List<DefinitionResource> resource = new ArrayList<>();
        notCachedDefinition.forEach(c -> {
            DefinitionResource definitionsResource = new DefinitionResource();
            c.getResults().forEach(r -> {
                r.getLexicalEntries().forEach(l -> {
                    l.getEntries().forEach(e -> {
                        e.getSenses().forEach(s -> {
                            definitionsResource.setWord(c.getId());
                            HashMap<String, String> def = new HashMap<>();
                            def.put(s.getId(), s.getDefinitions().get(0));
                            definitionsResource.setDefinitions(def);
                            if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
                                HashMap<String, String> def2 = new HashMap<>();
                                def2.put(s.getId(), s.getDefinitions().get(0));
                                definitionsResource.setDefinitions(def2);
                            }
                        });
                    });
                });
            });
            resource.add(definitionsResource);
        });
        return resource;
    }

    private List<Definition> buildDefinitions(Set<String> words) {
        List<Definition> calls = new ArrayList<>();
        words.forEach(w -> {
            Definition call = new Definition();
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

    public List<Definition> buidDefinitionFromFile() {
        Gson gson = new GsonBuilder().create();
        Definition definition = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME),
            Definition.class);
        return Collections.singletonList(definition);
    }

    private String readingTemplateContent(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            return new String(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void debugResourceReturn(List<DefinitionResource> result) {
        result.forEach(r -> {
            r.getDefinitions().forEach((k, v) -> {
                System.out.print(k);
                System.out.print(" " + v);
                System.out.println();
            });
        });
    }

    private List<DefinitionResource> buildDefinitionsResource(List<Definition>... definitions) {
        List<Definition> result = new ArrayList<>();
        for (List<Definition> calls : definitions) {
            if (calls != null && !calls.isEmpty()) {
                result.addAll(calls);
            }
        }
        List<DefinitionResource> definitionsResources = buildDefinitionsResource(result);
        return definitionsResources;
    }
}
