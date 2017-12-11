package com.tsm.cards.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;

@Transactional(propagation = Propagation.MANDATORY)
public interface ContentTypeRepository extends Repository<ContentType, Integer> {

	ContentType save(ContentType contentType);

	Optional<ContentType> findById(final Integer id);

	@Query("SELECT ct FROM ContentType ct WHERE UPPER(ct.name) = UPPER(?1)")
	Optional<ContentType> findByName(final String name);

	Set<ContentType> findAllByStatus(final ContentTypeStatus status);
}
