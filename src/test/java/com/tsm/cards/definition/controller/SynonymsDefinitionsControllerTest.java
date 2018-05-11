package com.tsm.cards.definition.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.definition.service.*;
import com.tsm.cards.documents.*;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class SynonymsDefinitionsControllerTest {

    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "synonyms_response.json";

    @Mock
    private KnownWordService mockKnowService;

    @Mock
    private SynonymsOxfordService mockOxfordService;

    @Mock
    private SynonymsDefinitionService mockDefinitionService;

    @Mock
    private SynonymsManageWordService mockManageWordService;

    @Mock
    private TrackWordsService mockTrackWordsService;

    @Mock
    private SynonymsBuildDefResourceService mockBuildDefinitionsResourceService;

    @InjectMocks
    private SynonymsDefinitionsController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void findByWord_UnknownWordGiven_ShouldReturnError() throws Exception {
        // Set up
        String word = "home";

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findSynonyms(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());

        verifyZeroInteractions(mockOxfordService, mockDefinitionService, mockTrackWordsService);
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
            controller.findSynonyms(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockManageWordService).createDefinition(word.toLowerCase());
        verifyZeroInteractions(mockOxfordService, mockTrackWordsService);
    }

    @Test
    public void findByWord_CachedWordGiven_ShouldReturnDefinition() throws Exception {
        // Set up
        String word = "home";
        SynonymsDefinition definition = buidDefinitionFromFile();
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));
        Optional<SynonymsDefinition> optional = Optional.of(definition);
        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase()))
            .thenReturn(optional);
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition)))
            .thenReturn(resources);

        // Do test
        ResultResource result = controller.findSynonyms(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockBuildDefinitionsResourceService).loadResource(Collections.singletonList(definition));
        verifyZeroInteractions(mockManageWordService);
        assertNotNull(result);

        assertThat(result.getDefinitions().isEmpty(), is(false));
        assertThat(result.getGivenWords().iterator().next(), is(word));
    }

    @Test
    public void findByWord_NotCachedWordGiven_ShouldReturnDefinition() throws Exception {
        // Set up
        String word = "home";
        SynonymsDefinition definition = buidDefinitionFromFile();
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase())).thenReturn(Optional.empty());
        when(mockManageWordService.createDefinition(word)).thenReturn(definition);
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition)))
            .thenReturn(resources);

        // Do test
        ResultResource result = controller.findSynonyms(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockDefinitionService).findOptionalDefinitionById(word.toLowerCase());
        verify(mockManageWordService).createDefinition(word);
        verify(mockBuildDefinitionsResourceService).loadResource(Collections.singletonList(definition));

        assertNotNull(result);

        assertThat(result.getDefinitions().isEmpty(), is(false));
        assertThat(result.getGivenWords().iterator().next(), is(word));
    }

    private List<DefinitionResource> buildDefinitionsResource(List<BaseDefinition> notCachedDefinition) {
        List<DefinitionResource> resource = new ArrayList<>();
        notCachedDefinition.forEach(c -> {
            DefinitionResource definitionsResource = new DefinitionResource();
            c.getResults().forEach(r -> {
                r.getLexicalEntries().forEach(l -> {
                    l.getEntries().forEach(e -> {
                        e.getSenses().forEach(s -> {
                            definitionsResource.setWord(c.getId());
                            HashMap<String, String> def = new HashMap<>();
                            s.getSynonyms().forEach(sy -> def.put(sy.getId(), sy.getText()));
                            definitionsResource.setSynonyms(def);
                            if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
                                HashMap<String, String> def2 = new HashMap<>();
                                s.getSynonyms().forEach(sy -> def.put(sy.getId(), sy.getText()));
                                definitionsResource.setSynonyms(def2);
                            }
                        });
                    });
                });
            });
            resource.add(definitionsResource);
        });
        return resource;
    }

    private List<SynonymsDefinition> buildDefinitions(Set<String> words) {
        List<SynonymsDefinition> calls = new ArrayList<>();
        words.forEach(w -> {
            SynonymsDefinition call = new SynonymsDefinition();
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

    public SynonymsDefinition buidDefinitionFromFile() {
        Gson gson = new GsonBuilder().create();

        SynonymsDefinition definition = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME), SynonymsDefinition.class);
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
