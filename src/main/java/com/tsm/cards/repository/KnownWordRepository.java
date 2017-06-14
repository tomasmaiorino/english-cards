package com.tsm.cards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.KnownWord;

@Transactional(propagation = Propagation.MANDATORY)
public interface KnownWordRepository extends MongoRepository<KnownWord, Long> {

	@Query("{ 'word': ?0 }")
	Optional<KnownWord> findByWord(final String word);

	List<KnownWord> findFirst20ByIdNotIn(final List<String> ids);

}
