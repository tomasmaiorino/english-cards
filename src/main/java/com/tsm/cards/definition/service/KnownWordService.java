package com.tsm.cards.definition.service;
import static com.tsm.cards.util.ErrorCodes.UNKNOWN_WORD_GIVEN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.definition.repository.KnownWordRepository;
import com.tsm.cards.documents.KnownWord;
import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class KnownWordService {

	@Autowired
	private KnownWordRepository repository;

	public KnownWord findByWord(final String word) {
		Assert.hasText(word, "The word must not be empty or null.");
		log.info("Searching for known word [{}] .", word);

		KnownWord knownWord = repository.findByWord(word).orElseThrow(() -> new BadRequestException(UNKNOWN_WORD_GIVEN));

		log.info("Found [{}] knownWord.", knownWord);

		return knownWord;
	}

	public List<KnownWord> findByIdsNotIn(final List<String> ids) {
		Assert.notNull(ids, "The ids must not be null.");
		Assert.isTrue(!ids.isEmpty(), "The ids must not be empty.");
		log.info("Searching for known word by ids [{}] .", ids);

		List<KnownWord> knownWords = repository.findFirst20ByIdNotIn(ids);

		if (knownWords.isEmpty()) {
			throw new ResourceNotFoundException("not found");
		}

		log.info("Found [{}] knownWord.", knownWords);

		return knownWords;
	}

	@Transactional
	public KnownWord save(final KnownWord knownWord) {
		Assert.notNull(knownWord, "The knownWord must not be empty or null.");
		log.debug("Saving knownWord [{}]", knownWord);

		repository.save(knownWord);

		log.debug("Saved [{}] knownWord.", knownWord);

		return knownWord;
	}

}
