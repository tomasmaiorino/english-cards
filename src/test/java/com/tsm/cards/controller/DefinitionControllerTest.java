package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
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
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Entries;
import com.tsm.cards.model.LexicalEntries;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.model.Results;
import com.tsm.cards.model.Senses;
import com.tsm.cards.model.Subsenses;
import com.tsm.cards.resources.DefinitionsResource;
import com.tsm.cards.service.BuildDefinitionsResourceService;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OriginalCallService;
import com.tsm.cards.service.OxfordService;
import com.tsm.cards.service.ProcessWordsService;

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
    private OriginalCallService mockOriginalCallService;

    @Mock
    private ManageWordService mockManageWordService;

    @Mock
    private ProcessWordsService mockProcessWordsService;

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

        verifyZeroInteractions(mockOxfordService);
        verifyZeroInteractions(mockOriginalCallService);
    }

    @Test
    public void findByWord_InvalidWordGiven_ShouldReturnNotFoundError() throws Exception {
        // Set up
        String word = "home";

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockOriginalCallService.findById(word.toLowerCase()))
            .thenThrow(ResourceNotFoundException.class);
        when(mockManageWordService.createOriginalCall(word.toLowerCase())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findByWord(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockOriginalCallService).findById(word.toLowerCase());
        verify(mockManageWordService).createOriginalCall(word.toLowerCase());
    }

    @Test
    public void findByWord_CachedWordGiven() throws Exception {
        // Set up
        String word = "home";
        OriginalCall originalCall = new OriginalCall();

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockOriginalCallService.findById(word.toLowerCase())).thenReturn(originalCall);

        // Do test
        OriginalCall result = controller.findByWord(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockOriginalCallService).findById(word.toLowerCase());
        assertThat(result, is(originalCall));
        verifyZeroInteractions(mockManageWordService);
    }

    @Test
    public void findByWord_CreatedNewOriginalCall() throws Exception {
        // Set up
        String word = "home";
        OriginalCall originalCall = new OriginalCall();

        // Expectations
        when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
        when(mockOriginalCallService.findById(word.toLowerCase()))
            .thenThrow(ResourceNotFoundException.class);
        when(mockManageWordService.createOriginalCall(word.toLowerCase())).thenReturn(originalCall);

        // Do test
        OriginalCall result = controller.findByWord(word);

        // Assertions
        verify(mockKnowService).findByWord(word.toLowerCase());
        verify(mockOriginalCallService).findById(word.toLowerCase());
        verify(mockManageWordService).createOriginalCall(word.toLowerCase());
        assertThat(result, is(originalCall));
    }

    @Test
    public void getDefinitions_NullWordsGiven_ShouldReturnNotFoundError() throws Exception {
        // Set up
        String words = null;

        // Expectations
        when(mockProcessWordsService.splitWords(words)).thenThrow(IllegalArgumentException.class);

        // Do test
        try {
            controller.getDefinitions(words);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockManageWordService);
        verify(mockProcessWordsService).splitWords(words);
    }

    @Test
    public void getDefinitions_InvalidWordsGiven_ShouldReturnNotFoundError() throws Exception {
        // Set up
        String word = "home,car";
        Set<String> words = new HashSet<>();
        words.add("home");
        words.add("car");

        // Expectations
        when(mockProcessWordsService.splitWords(word)).thenReturn(words);
        when(mockProcessWordsService.getValidWords(words)).thenThrow(IllegalArgumentException.class);

        // Do test
        try {
            controller.getDefinitions(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verify(mockProcessWordsService).splitWords(word);
        verify(mockProcessWordsService).getValidWords(words);
        verify(mockProcessWordsService, never()).getCachedWords(words);
        verifyZeroInteractions(mockManageWordService);
    }

    @Test
    public void getDefinitions_CachedWordsGiven_OriginalCall() throws Exception {
        // Set up
        String word = "home,car";
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        List<OriginalCall> cachedWords = buildOriginalCalls(validWords);
        List<DefinitionsResource> definitionsResources = buildDefinitionsResource(cachedWords, null);

        // Expectations
        when(mockProcessWordsService.splitWords(word)).thenReturn(validWords);
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);

        // Do test
        List<DefinitionsResource> result = controller.getDefinitions(word);

        // Assertions
        verify(mockProcessWordsService).splitWords(word);
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService, never()).getNotCachedWords(cachedWords, validWords);
        verifyZeroInteractions(mockManageWordService);

        assertThat(result, is(definitionsResources));
    }

    @Test
    public void getDefinitions_CachedNotCachedWordsGiven_OriginalCall() throws Exception {
        // Set up
        String words = "home,car";
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");

        Set<String> cachedWord = new HashSet<>();
        cachedWord.add("home");

        String word = "car";

        Set<String> notCached = new HashSet<>();
        notCached.add(word);

        List<OriginalCall> cachedWords = buildOriginalCalls(cachedWord);
        List<OriginalCall> notCachedOriginalCall = buildOriginalCalls(notCached);
        List<DefinitionsResource> definitionsResources = buildDefinitionsResource(cachedWords, notCachedOriginalCall);

        // Expectations
        when(mockProcessWordsService.splitWords(words)).thenReturn(validWords);
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockProcessWordsService.getNotCachedWords(cachedWords, validWords)).thenReturn(notCached);
        when(mockManageWordService.createOriginalCall(word)).thenReturn(notCachedOriginalCall.iterator().next());
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);

        // Do test
        List<DefinitionsResource> result = controller.getDefinitions(words);

        // Assertions
        verify(mockProcessWordsService).splitWords(words);
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService).getNotCachedWords(cachedWords, validWords);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        assertThat(result, is(definitionsResources));
    }

    @Test
    public void getDefinitions_CachedNotCachedWordsGiven_OriginalCall_ExternalCallError() throws Exception {
        // Set up
        String words = "home,car,drive";
        Set<String> validWords = new HashSet<>();
        validWords.add("home");
        validWords.add("car");
        validWords.add("drive");

        Set<String> cachedWord = new HashSet<>();
        cachedWord.add("home");

        String word = "car";
        String wordTwo = "drive";

        Set<String> notCached = new HashSet<>();
        notCached.add(word);
        notCached.add(wordTwo);

        Set<String> expectedCacheWords = new HashSet<>();
        expectedCacheWords.add(word);

        List<OriginalCall> cachedWords = buildOriginalCalls(cachedWord);
        List<OriginalCall> notCachedOriginalCall = buildOriginalCalls(expectedCacheWords);
        List<DefinitionsResource> definitionsResources = buildDefinitionsResource(cachedWords, notCachedOriginalCall);

        // Expectations
        when(mockProcessWordsService.splitWords(words)).thenReturn(validWords);
        when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
        when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
        when(mockProcessWordsService.getNotCachedWords(cachedWords, validWords)).thenReturn(notCached);
        when(mockManageWordService.createOriginalCall(word)).thenReturn(notCachedOriginalCall.iterator().next());
        when(mockManageWordService.createOriginalCall(wordTwo)).thenThrow(Exception.class);
        when(mockBuildDefinitionsResourceService.loadResource(cachedWords)).thenReturn(definitionsResources);

        // Do test
        List<DefinitionsResource> result = controller.getDefinitions(words);

        // Assertions
        verify(mockProcessWordsService).splitWords(words);
        verify(mockProcessWordsService).getValidWords(validWords);
        verify(mockProcessWordsService).getCachedWords(validWords);
        verify(mockProcessWordsService).getNotCachedWords(cachedWords, validWords);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        assertThat(result, is(definitionsResources));
    }

    private List<DefinitionsResource> buildDefinitionsResource(List<OriginalCall> notCachedOriginalCall) {
        List<DefinitionsResource> resource = new ArrayList<>();
        notCachedOriginalCall.forEach(c -> {
            DefinitionsResource definitionsResource = new DefinitionsResource();
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

    public List<OriginalCall> buidOriginalCallFromFile() {
        Gson gson = new GsonBuilder().create();
        OriginalCall originalCall = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME),
            OriginalCall.class);
        return Collections.singletonList(originalCall);
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

    public void debugResourceReturn(List<DefinitionsResource> result) {
        result.forEach(r -> {
            r.getDefinitions().forEach((k, v) -> {
                System.out.print(k);
                System.out.print(" " + v);
                System.out.println();
            });
        });
    }

    private List<DefinitionsResource> buildDefinitionsResource(List<OriginalCall>... originalCalls) {
        List<OriginalCall> result = new ArrayList<>();
        for (List<OriginalCall> calls : originalCalls) {
            if (calls != null && !calls.isEmpty()) {
                result.addAll(calls);
            }
        }
        List<DefinitionsResource> definitionsResources = buildDefinitionsResource(result);
        return definitionsResources;
    }
}
