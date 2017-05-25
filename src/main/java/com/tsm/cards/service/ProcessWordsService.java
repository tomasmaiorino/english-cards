package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessWordsService {

	private static final String WORDS_SEPARATOR = ",";

	@Autowired
	@Getter
	@Setter
	private KnownWordService knownWordService;

	@Autowired
	@Getter
	@Setter
	private DefinitionService definitionService;

	public Set<String> getNotCachedWords(List<Definition> calls, Set<String> words) {
		Assert.notEmpty(calls, "The calls must not be empty.");
		Assert.notEmpty(words, "The words must not be empty.");

		Set<String> notCachedWords = new HashSet<>();
		notCachedWords = words.stream().filter(w -> calls.stream().filter(c -> c.getId().equals(w)).count() == 0)
				.collect(Collectors.toSet());

		return notCachedWords;
	}

	public List<Definition> getCachedWords(Set<String> words) {
		Assert.notEmpty(words, "The words must not be empty.");
		log.info("get cached words [{}]", words);
		List<Definition> calls = new ArrayList<>();
		words.forEach(w -> {
			try {
				calls.add(definitionService.findById(w));
			} catch (ResourceNotFoundException e) {
			}
		});
		log.info("cached words [{}]", calls.size());
		return calls;
	}

	public Set<String> splitWords(String words) {
		Assert.notNull(words, "The words must not be empty.");
		log.info("spliint words [{}]", words);
		if (words.contains(WORDS_SEPARATOR)) {
			List<String> result = createWordsList(words);
			return new HashSet<>(result);
		} else {
			return new HashSet<>(Collections.singletonList(words));
		}
	}

	private List<String> createWordsList(String words) {
		String [] content = words.split(WORDS_SEPARATOR);
		List<String> result = new ArrayList<>();
		for (String s : content) {
			if (Pattern.matches("\\w+", s)) {
				result.add(s.toLowerCase());
			}
		}
		return result;
	}

	public Set<String> getValidWords(Set<String> words) {
		Assert.notEmpty(words, "The words must not be empty.");
		log.info("get valid words [{}]", words);
		Set<String> result = new HashSet<>();
		words.forEach(w -> {
			try {
				knownWordService.findByWord(w);
				result.add(w);
			} catch (ResourceNotFoundException e) {
			}
		});
		return result;
	}
}
