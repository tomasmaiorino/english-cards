package com.tsm.cards.definition.repository;

import com.tsm.cards.documents.KnownWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
public interface KnownWordRepository extends MongoRepository<KnownWord, Long> {

	@Query("{ 'word': ?0 }")
	Optional<KnownWord> findByWord(final String word);

	List<KnownWord> findFirst20ByIdNotIn(final List<String> ids);

}
