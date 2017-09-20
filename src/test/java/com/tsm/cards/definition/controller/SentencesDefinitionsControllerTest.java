package com.tsm.cards.definition.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.definition.service.KnownWordService;
import com.tsm.cards.definition.service.SentencesBuildDefResourceService;
import com.tsm.cards.definition.service.SentencesDefinitionService;
import com.tsm.cards.definition.service.SentencesManageWordService;
import com.tsm.cards.definition.service.SentencesOxfordService;
import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.SentencesDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.resources.DefinitionResource;
import com.tsm.cards.resources.ResultResource;
import com.tsm.cards.service.TrackWordsService;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class SentencesDefinitionsControllerTest {

    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "sentences_response.json";

    @Mock
    private KnownWordService mockKnowService;

    @Mock
    private SentencesOxfordService mockOxfordService;

    @Mock
    private SentencesDefinitionService mockDefinitionService;

    @Mock
    private SentencesManageWordService mockManageWordService;

    @Mock
    private TrackWordsService mockTrackWordsService;

    @Mock
    private SentencesBuildDefResourceService mockBuildDefinitionsResourceService;

    @InjectMocks
    private SentencesDefinitionsController controller;

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
            controller.findSentences(word);
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
            controller.findSentences(word);
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
        SentencesDefinition definition = buidDefinitionFromFile();
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));
        Optional<SentencesDefinition> optional = Optional.of(definition);
        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase()))
            .thenReturn(optional);
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition)))
            .thenReturn(resources);

        // Do test
        ResultResource result = controller.findSentences(word);

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
        SentencesDefinition definition = buidDefinitionFromFile();
        List<DefinitionResource> resources = buildDefinitionsResource(Collections.singletonList(definition));

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockDefinitionService.findOptionalDefinitionById(word.toLowerCase())).thenReturn(Optional.empty());
        when(mockManageWordService.createDefinition(word)).thenReturn(definition);
        when(mockBuildDefinitionsResourceService.loadResource(Collections.singletonList(definition)))
            .thenReturn(resources);

        // Do test
        ResultResource result = controller.findSentences(word);

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
            List<String> sentences = new ArrayList<>();
            c.getResults().forEach(r -> {
                r.getLexicalEntries().forEach(l -> {
                    if (!CollectionUtils.isEmpty(l.getSentences())) {
                        l.getSentences().forEach(s -> {
                            sentences.add(s.getText());
                        });
                    }
                });
            });
            definitionsResource.setSentences(sentences);
            resource.add(definitionsResource);
        });
        return resource;
    }

    public SentencesDefinition buidDefinitionFromFile() {
        Gson gson = new GsonBuilder().create();

        SentencesDefinition definition = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME), SentencesDefinition.class);
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
}
