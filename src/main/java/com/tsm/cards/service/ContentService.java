package com.tsm.cards.service;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.model.Content;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.repository.ContentRepository;
import com.tsm.cards.repository.IBaseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ContentService extends BaseService<Content, Integer> {

	@Autowired
	private ContentRepository repository;

	@Override
	protected void merge(final Content origin, final Content model) {
		origin.setName(model.getName());
		if (StringUtils.isNotBlank(model.getImgUrl())) {
			origin.setImgUrl(model.getImgUrl());
		}
		origin.setStatus(model.getStatus());
		origin.setContentType(model.getContentType());
		origin.setContent(model.getContent());
		origin.setIsHtml(model.getIsHtml());
	}

	public Set<Content> findAllByContentTypeAndStatus(final ContentType contentType, final ContentStatus status) {
		Assert.notNull(contentType, "The contentType must not be null!");
		Assert.isTrue(!contentType.isNew(), "The contentType must not be new!");
		Assert.notNull(status, "The status must not be null!");

		log.info("Finding all by content type [{}] and status [{}].", contentType, status);

		Set<Content> contents = repository.findAllByContentTypeAndStatus(contentType, status);

		log.info("Contents found [{}].", contents.size());

		return contents;
	}

	@Override
	public IBaseRepository<Content, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Content.class.getSimpleName();
	}
}
