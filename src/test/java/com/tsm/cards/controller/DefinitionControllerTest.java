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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import com.tsm.cards.service.KnownWordService;
import com.tsm.cards.service.ManageWordService;
import com.tsm.cards.service.OriginalCallService;
import com.tsm.cards.service.OxfordService;
import com.tsm.cards.service.ProcessWordsService;

@SuppressWarnings("unchecked")
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
		Set<String> validWords = new HashSet<>();
		validWords.add("home");
		validWords.add("car");
		List<OriginalCall> cachedWords = buildOriginalCalls(validWords);
		// Expectations
		when(mockProcessWordsService.splitWords(word)).thenReturn(validWords);
		when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
		when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);

		// Do test
		List<DefinitionsResource> result = controller.getDefinitions(word);

		// Assertions
		verify(mockProcessWordsService).splitWords(word);
		verify(mockProcessWordsService).getValidWords(validWords);
		verify(mockProcessWordsService).getCachedWords(validWords);
		verify(mockProcessWordsService, never()).getNotCachedWords(cachedWords, validWords);
		verifyZeroInteractions(mockManageWordService);

		List<String> resultsWords = result.stream().map(DefinitionsResource::getWord)
				.filter(s -> validWords.contains(s)).collect(Collectors.toList());

		Assert.assertTrue(resultsWords.containsAll(validWords));
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
		// Expectations
		when(mockProcessWordsService.splitWords(words)).thenReturn(validWords);
		when(mockProcessWordsService.getValidWords(validWords)).thenReturn(validWords);
		when(mockProcessWordsService.getCachedWords(validWords)).thenReturn(cachedWords);
		when(mockProcessWordsService.getNotCachedWords(cachedWords, validWords)).thenReturn(notCached);
		when(mockManageWordService.createOriginalCall(word)).thenReturn(notCachedOriginalCall.iterator().next());

		// Do test
		List<DefinitionsResource> result = controller.getDefinitions(words);

		// Assertions
		verify(mockProcessWordsService).splitWords(words);
		verify(mockProcessWordsService).getValidWords(validWords);
		verify(mockProcessWordsService).getCachedWords(validWords);
		verify(mockProcessWordsService).getNotCachedWords(cachedWords, validWords);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());

		List<String> resultsWords = result.stream().map(DefinitionsResource::getWord)
				.filter(s -> validWords.contains(s)).collect(Collectors.toList());

		Assert.assertTrue(resultsWords.containsAll(validWords));
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

	private List<OriginalCall> buidOriginalCallFromFile() {
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

	private void debugResourceReturn(List<DefinitionsResource> result) {
		result.forEach(r -> {
			r.getDefinitions().forEach((k, v) -> {
				System.out.print(k);
				System.out.print(" " + v);
				System.out.println();
			});
		});
	}

}
