package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.model.OriginalCall;

@SuppressWarnings("unchecked")
public class ProcessWordsServiceTest {

    @Mock
    private KnownWordService mockKnownWordService;

    @Mock
    private OriginalCallService mockOriginalCallService;

    @InjectMocks
    private ProcessWordsService service;

    private static final String TEST_WORD_WORD = "word";

    private static final String TEST_WORD_HOME = "home";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCachedWords_NullOriginalCallGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCalls = null;
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);

        // Do test
        service.getNotCachedWords(originalCalls, words);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCached_emptyOriginalCallGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCalls = Collections.emptyList();
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);

        // Do test
        service.getNotCachedWords(originalCalls, words);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCachedWords_NullWordsGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCalls = buildOriginalCalls();
        Set<String> words = null;

        // Do test
        service.getNotCachedWords(originalCalls, words);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotCached_emptyWordsGiven_ShouldThrowException() {
        // Set up
        List<OriginalCall> originalCalls = buildOriginalCalls();
        Set<String> words = Collections.emptySet();

        // Do test
        service.getNotCachedWords(originalCalls, words);
    }

    @Test
    public void getNotCached_ValidDataGiven_ShouldReturnValidSet() {
        // Set up
        List<OriginalCall> originalCalls = buildOriginalCalls();
        Set<String> words = new HashSet<>();
        words.add("java");
        words.add(TEST_WORD_WORD);

        // Do test
        Set<String> result = service.getNotCachedWords(originalCalls, words);

        // Assertions
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertFalse(result.contains("home"));
        Assert.assertTrue(result.contains("java"));
    }

    @Test
    public void getNotCached_ValidDataGiven_ShouldReturnEmptySet() {
        // Set up
        List<OriginalCall> originalCalls = buildOriginalCalls();
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_WORD);
        words.add(TEST_WORD_HOME);

        // Do test
        Set<String> result = service.getNotCachedWords(originalCalls, words);

        // Assertions
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    private List<OriginalCall> buildOriginalCalls() {
        List<OriginalCall> calls = new ArrayList<>();
        OriginalCall call = new OriginalCall();
        call.setId(TEST_WORD_WORD);
        calls.add(call);
        call = new OriginalCall();
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
        verifyZeroInteractions(mockOriginalCallService);
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
        verifyZeroInteractions(mockOriginalCallService);
    }

    @Test
    public void getCachedWords_ValidlWordsGiven_ShouldReturnOriginalCall() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);
        OriginalCall home = buidOriginalCall(TEST_WORD_HOME);

        // Expectations
        when(mockOriginalCallService.findById(TEST_WORD_HOME)).thenReturn(home);
        when(mockOriginalCallService.findById(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        List<OriginalCall> result = null;
        // Do test
        result = service.getCachedWords(words);

        // Assertions
        verify(mockOriginalCallService).findById(TEST_WORD_HOME);
        verify(mockOriginalCallService).findById(TEST_WORD_WORD);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        assertThat(result.get(0), is(home));
    }

    @Test
    public void getCachedWords_ValidlWordsGiven_NotCachedWordFound() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);

        // Expectations
        when(mockOriginalCallService.findById(TEST_WORD_HOME)).thenThrow(ResourceNotFoundException.class);
        when(mockOriginalCallService.findById(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        List<OriginalCall> result = null;
        // Do test
        result = service.getCachedWords(words);

        // Assertions
        verify(mockOriginalCallService).findById(TEST_WORD_HOME);
        verify(mockOriginalCallService).findById(TEST_WORD_WORD);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    private OriginalCall buidOriginalCall(final String word) {
        OriginalCall call = new OriginalCall();
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
        verifyZeroInteractions(mockOriginalCallService);
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
        verifyZeroInteractions(mockOriginalCallService);
    }

    @Test
    public void getValidWords_ValidlWordsGiven_ShouldReturnSetWords() {
        // Set up
        Set<String> words = new HashSet<>();
        words.add(TEST_WORD_HOME);
        words.add(TEST_WORD_WORD);

        // Expectations
        when(mockKnownWordService.findByWord(TEST_WORD_HOME)).thenReturn(mock(KnownWord.class));
        when(mockKnownWordService.findByWord(TEST_WORD_WORD)).thenThrow(ResourceNotFoundException.class);

        Set<String> result = null;
        // Do test
        result = service.getValidWords(words);

        // Assertions
        verify(mockKnownWordService).findByWord(TEST_WORD_HOME);
        verify(mockKnownWordService).findByWord(TEST_WORD_WORD);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(result.iterator().next(), TEST_WORD_HOME);
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
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
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
        Assert.assertNotNull(result);
        Assert.assertTrue(!result.isEmpty());
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(words, result.iterator().next());
    }

    @Test
    public void splitWords_MoreThanWordGiven_ShouldReturnSet() {
        // Set up
        String words = "word,car,";
        // Do Test
        Set<String> result = service.splitWords(words);

        // Assertions
        Assert.assertNotNull(result);
        Assert.assertTrue(!result.isEmpty());
        Assert.assertEquals(2, result.size());
    }

}
