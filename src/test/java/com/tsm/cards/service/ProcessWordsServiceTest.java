package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.model.KnownWord;

@SuppressWarnings("unchecked")
@FixMethodOrder(MethodSorters.JVM)
public class ProcessWordsServiceTest {

    @Mock
    private KnownWordService mockKnownWordService;

    @Mock
    private DefinitionService mockDefinitionService;

    @InjectMocks
    private ProcessWordsService service;

    private static final String TEST_WORD_WORD = "word";

    private static final String TEST_WORD_HOME = "home";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNotCachedWords_NullDefinitionGiven_ShouldReturnValidWords() {
        // Set up
        List<Definition> definitions = null;
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);

        // Do test
        Set<String> result = service.getNotCachedWords(definitions, words);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(words.size()));
        assertThat(result, is(words));
    }

    @Test
    public void getNotCached_emptyDefinitionGiven_ShouldThrowException() {
        // Set up
        List<Definition> definitions = Collections.emptyList();
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);

        // Do test
        Set<String> result = service.getNotCachedWords(definitions, words);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(words.size()));
        assertThat(result, is(words));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCachedWords_NullWordsGiven_ShouldThrowException() {
        // Set up
        List<Definition> definitions = buildDefinitions();
        Set<String> words = null;

        // Do test
        service.getNotCachedWords(definitions, words);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCached_emptyWordsGiven_ShouldThrowException() {
        // Set up
        List<Definition> definitions = buildDefinitions();
        Set<String> words = Collections.emptySet();

        // Do test
        service.getNotCachedWords(definitions, words);
    }

    @Test
    public void getNotCached_ValidDataGiven_ShouldReturnValidSet() {
        // Set up
        List<Definition> definitions = buildDefinitions();
        Set<String> words = new HashSet<>();
        words.add("java");
        words.add(TEST_WORD_WORD);

        // Do test
        Set<String> result = service.getNotCachedWords(definitions, words);

        // Assertions
        assertThat(result, notNullValue());
        assertThat(result.size(), is(1));
        assertThat(result.contains("home"), is(false));
        assertThat(result.contains("java"), is(true));
    }

    @Test
    public void getNotCached_ValidDataGiven_ShouldReturnEmptySet() {
        // Set up
        List<Definition> definitions = buildDefinitions();
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);
        words.add(TEST_WORD_HOME);

        // Do test
        Set<String> result = service.getNotCachedWords(definitions, words);

        // Assertions
        assertThat(result, notNullValue());
        assertThat(result.size(), is(0));
    }

    private List<Definition> buildDefinitions() {
        List<Definition> calls = new ArrayList<>();
        Definition call = new Definition();
        call.setId(TEST_WORD_WORD);
        calls.add(call);
        call = new Definition();
        call.setId(TEST_WORD_HOME);
        calls.add(call);
        return calls;
    }

    @Test
    public void getCachedWords_NullWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> words = null;

        // Do test
        try {
            service.getCachedWords(words);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockDefinitionService);
    }

    @Test
    public void getCachedWords_emptylWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> words = Collections.emptySet();

        // Do test
        try {
            service.getCachedWords(words);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockDefinitionService);
    }

    @Test
    public void getCachedWords_ValidlWordsGiven_ShouldReturnDefinition() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);
        Definition home = buidDefinition(TEST_WORD_HOME);

        // Expectations
        when(mockDefinitionService.findById(TEST_WORD_HOME)).thenReturn(home);
        when(mockDefinitionService.findById(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        List<Definition> result = null;
        // Do test
        result = service.getCachedWords(words);

        // Assertions
        verify(mockDefinitionService).findById(TEST_WORD_HOME);
        verify(mockDefinitionService).findById(TEST_WORD_WORD);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(home));
    }

    @Test
    public void getCachedWords_ValidlWordsGiven_NotCachedWordFound() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);

        // Expectations
        when(mockDefinitionService.findById(TEST_WORD_HOME)).thenThrow(ResourceNotFoundException.class);
        when(mockDefinitionService.findById(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        List<Definition> result = null;
        // Do test
        result = service.getCachedWords(words);

        // Assertions
        verify(mockDefinitionService).findById(TEST_WORD_HOME);
        verify(mockDefinitionService).findById(TEST_WORD_WORD);
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    private Definition buidDefinition(final String word) {
        Definition call = new Definition();
        call.setId(word);
        return call;
    }

    @Test
    public void getValidWords_NullWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> words = null;

        // Do test
        try {
            service.getValidWords(words);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockDefinitionService);
    }

    @Test
    public void getValidWords_emptylWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> words = Collections.emptySet();

        // Do test
        try {
            service.getValidWords(words);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockDefinitionService);
    }

    @Test
    public void getValidWords_ValidlWordsGiven_ShouldReturnSetWords() {
        // Set up
        String home = "HomE";
        String word = "WoRd";
        String lowerHome = "home";
        Set<String> words = new HashSet<>();
        words.add(home);
        words.add(word);

        // Expectations
        when(mockKnownWordService.findByWord(home.toLowerCase())).thenReturn(mock(KnownWord.class));
        when(mockKnownWordService.findByWord(word.toLowerCase())).thenThrow(ResourceNotFoundException.class);

        Set<String> result = null;
        // Do test
        result = service.getValidWords(words);

        // Assertions
        verify(mockKnownWordService).findByWord(home.toLowerCase());
        verify(mockKnownWordService).findByWord(word.toLowerCase());
        assertThat(result, notNullValue());
        assertThat(lowerHome, is(result.iterator().next()));
        assertThat(result.size(), is(1));
    }

    @Test
    public void getValidWords_ValidlWordsGiven_NoneWordFound() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);

        // Expectations
        when(mockKnownWordService.findByWord(TEST_WORD_HOME)).thenThrow(ResourceNotFoundException.class);
        when(mockKnownWordService.findByWord(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        Set<String> result = null;
        // Do test
        result = service.getValidWords(words);

        // Assertions
        verify(mockKnownWordService).findByWord(TEST_WORD_HOME);
        verify(mockKnownWordService).findByWord(TEST_WORD_WORD);
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void splitWords_emptylWordsGiven_ShouldThrowException() {
        // Set up
        String words = null;
        // Do Test
        service.splitWords(words);
    }

    @Test
    public void splitWords_OneWordGiven_ShouldReturnSet() {
        // Set up
        String words = "word";
        // Do Test
        Set<String> result = service.splitWords(words);

        // Assertions
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(1));
        assertThat(words, is(result.iterator().next()));
    }

    @Test
    public void splitWords_MoreThanWordGiven_ShouldReturnSet() {
        // Set up
        String words = "word,car,";
        // Do Test
        Set<String> result = service.splitWords(words);

        // Assertions
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInvalidWords_EmptyReceivedWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> receivedWords = Collections.emptySet();
        Set<String> validWords = new HashSet<>();
        validWords.add("home");

        // Do test
        service.getInvalidWords(validWords, receivedWords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInvalidWords_EmptyValidWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> receivedWords = new HashSet<>();
        receivedWords.add("home");
        Set<String> validWords = Collections.emptySet();

        // Do test
        service.getInvalidWords(validWords, receivedWords);
    }

    @Test
    public void getInvalidWords_InvalidWordsGiven_ShouldThrowException() {
        // Set up
        String invalidWord = "xpto";
        Set<String> receivedWords = new HashSet<>();
        receivedWords.add("home");
        receivedWords.add(invalidWord);
        Set<String> validWords = new HashSet<>();
        validWords.add("home");

        // Do test
        Set<String> result = service.getInvalidWords(validWords, receivedWords);
        assertThat(receivedWords, notNullValue());
        assertThat(result.isEmpty(), is(false));
        assertThat(validWords.size(), is(1));
        assertThat(result.contains(invalidWord), is(true));
    }

    @Test
    public void getInvalidWords_OnlyValidWordsGiven_ShouldThrowException() {
        // Set up
        Set<String> receivedWords = new HashSet<>();
        receivedWords.add("home");
        Set<String> validWords = new HashSet<>();
        validWords.add("home");

        // Do test
        Set<String> result = service.getInvalidWords(validWords, receivedWords);
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
        assertThat(result.size(), is(0));
    }
}
