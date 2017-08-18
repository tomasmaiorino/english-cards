package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.google.gson.reflect.TypeToken;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.model.Entries;
import com.tsm.cards.model.LexicalEntries;
import com.tsm.cards.model.Results;
import com.tsm.cards.model.Senses;
import com.tsm.cards.model.Subsenses;
import com.tsm.cards.resources.DefinitionResource;
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
    public void findByWord_UnknownWordGiven_ShouldReturnError() throws Exception {
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
    public void findByWord_CachedWordGiven_ShouldReturnDefinition() throws Exception {
        // Set up
        String word = "home";
        Definition definition = buidDefinitionFromFile().get(0);
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase())).thenReturn(Optional.of(definition));
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition))).thenReturn(resources);

        // Do test
        DefinitionResource result = controller.findByWord(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockBuildDefinitionsResourceService).loadResource(Collections.singletonList(definition));
        verifyZeroInteractions(mockManageWordService);
        assertNotNull(result);

        assertThat(result.getWord(), is(definition.getId()));
        assertThat(result.getWord(), is(word));
        assertThat(result.getDefinitions().isEmpty(), is(false));
    }

    @Test
    public void findByWord_NotCachedWordGiven_ShouldReturnDefinition() throws Exception {
        // Set up
        String word = "home";
        Definition definition = buidDefinitionFromFile().get(0);
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase())).thenReturn(Optional.empty());
        when(mockManageWordService.createDefinition(word)).thenReturn(definition);
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition))).thenReturn(resources);

        // Do test
        DefinitionResource result = controller.findByWord(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockManageWordService).createDefinition(word);
        verify(mockBuildDefinitionsResourceService).loadResource(Collections.singletonList(definition));

        assertNotNull(result);

        assertThat(result.getWord(), is(definition.getId()));
        assertThat(result.getWord(), is(word));
        assertThat(result.getDefinitions().isEmpty(), is(false));
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

        Type listType = new TypeToken<ArrayList<Definition>>() {
        }.getType();
        List<Definition> definition = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME), listType);
        return definition;
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
