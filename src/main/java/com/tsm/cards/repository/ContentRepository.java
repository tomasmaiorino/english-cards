package com.tsm.cards.repository;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface ContentRepository extends Repository<Content, Integer>, IBaseRepository<Content, Integer> {

	Set<Content> findAllByContentTypeAndStatus(final ContentType contentType, final ContentStatus status);

}
