package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.resources.DefinitionsResource;
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OriginalCallService;
import com.tsm.cards.service.OxfordService;
import com.tsm.cards.service.ProcessWordsService;

@SuppressWarnings("unchecked")
public class DefinitionControllerTest {

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
		when(mockOriginalCallService.findOriginalCallById(word.toLowerCase()))
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
		verify(mockOriginalCallService).findOriginalCallById(word.toLowerCase());
		verify(mockManageWordService).createOriginalCall(word.toLowerCase());
	}

	@Test
	public void findByWord_CachedWordGiven() throws Exception {
		// Set up
		String word = "home";
		OriginalCall originalCall = new OriginalCall();
		// Expectations
		when(mockKnowService.findByWord(word.toLowerCase())).thenReturn(null);
		when(mockOriginalCallService.findOriginalCallById(word.toLowerCase())).thenReturn(originalCall);

		// Do test
		OriginalCall result = controller.findByWord(word);

		// Assertions
		verify(mockKnowService).findByWord(word.toLowerCase());
		verify(mockOriginalCallService).findOriginalCallById(word.toLowerCase());
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
		when(mockOriginalCallService.findOriginalCallById(word.toLowerCase()))
				.thenThrow(ResourceNotFoundException.class);
		when(mockManageWordService.createOriginalCall(word.toLowerCase())).thenReturn(originalCall);

		// Do test
		OriginalCall result = controller.findByWord(word);

		// Assertions
		verify(mockKnowService).findByWord(word.toLowerCase());
		verify(mockOriginalCallService).findOriginalCallById(word.toLowerCase());
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
		Set<String> words = new HashSet<>();
		words.add("home");
		words.add("car");
		List<OriginalCall> originalCalls = buildOriginalCalls(words);
		// Expectations
		when(mockProcessWordsService.splitWords(word)).thenReturn(words);
		when(mockProcessWordsService.getValidWords(words)).thenReturn(words);
		when(mockProcessWordsService.getCachedWords(words)).thenReturn(originalCalls);

		// Do test
		List<DefinitionsResource> result = controller.getDefinitions(word);

		// Assertions
		verify(mockProcessWordsService).splitWords(word);
		verify(mockProcessWordsService).getValidWords(words);
		verify(mockProcessWordsService).getCachedWords(words);
		verify(mockProcessWordsService, never()).getNotCachedWords(originalCalls, words);
		verifyZeroInteractions(mockManageWordService);
		assertThat(result, is(originalCalls));
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

		List<OriginalCall> originalCalls = buildOriginalCalls(cachedWord);
		List<OriginalCall> notCachedOriginalCall = buildOriginalCalls(notCached);
		// Expectations
		when(mockProcessWordsService.splitWords(words)).thenReturn(validWords);
		when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
		when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(originalCalls);
		when(mockProcessWordsService.getNotCachedWords(originalCalls, validWords)).thenReturn(notCached);
		when(mockManageWordService.createOriginalCall(word)).thenReturn(notCachedOriginalCall.iterator().next());
		
		// Do test
		List<DefinitionsResource> result = controller.getDefinitions(word);

		// Assertions
		verify(mockProcessWordsService).splitWords(word);
		verify(mockProcessWordsService).getValidWords(validWords);
		verify(mockProcessWordsService).getCachedWords(validWords);
		verify(mockProcessWordsService).getNotCachedWords(originalCalls, validWords);
		verifyZeroInteractions(mockManageWordService);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains(validWords));
	}

	private List<OriginalCall> buildOriginalCalls(Set<String> words) {
		List<OriginalCall> calls = new ArrayList<>();
		words.forEach(w -> {
			OriginalCall call = new OriginalCall();
			call.setId(w);
			calls.add(call);
		});
		return calls;
	}

}
