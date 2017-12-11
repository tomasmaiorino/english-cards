package com.tsm.cards.service;

import static com.tsm.cards.util.ErrorCodes.CONTENT_TYPE_NOT_FOUND;
import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CONTENT_TYPE;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.repository.ContentTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ContentTypeService {

	@Autowired
	private ContentTypeRepository repository;

	@Transactional
	public ContentType save(final ContentType contentType) {
		Assert.notNull(contentType, "The contentType must not be null!");
		log.info("Saving contentType [{}] .", contentType);

		repository.findByName(contentType.getName()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_CONTENT_TYPE);
		});

		repository.save(contentType);

		log.info("Saved contentType [{}].", contentType);

		return contentType;
	}

	@Transactional
	public ContentType update(final ContentType origin, ContentType model) {
		Assert.notNull(origin, "The origin must not be null!");
		Assert.notNull(origin.getId(), "The origin must not be new!");
		Assert.notNull(model, "The model must not be null!");
		log.info("Updating content type origin[{}] to model [{}].", origin, model);

		repository.findByName(model.getName()).ifPresent(c -> {
			if (!c.getId().equals(origin.getId())) {
				throw new BadRequestException(DUPLICATED_CONTENT_TYPE);
			}
		});

		merge(origin, model);

		repository.save(origin);

		log.info("Updated contentType [{}].", origin);

		return origin;
	}

	private void merge(final ContentType origin, final ContentType model) {
		origin.setName(model.getName());
	}

	public ContentType findById(final Integer id) {
		Assert.notNull(id, "The id must not be null!");
		log.info("Finding content type id [{}] .", id);

		ContentType contentType = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CONTENT_TYPE_NOT_FOUND));

		log.info("ContentType found [{}].", contentType);

		return contentType;
	}

	public Set<ContentType> findAllByStatus(final ContentTypeStatus status) {
		Assert.notNull(status, "The status must not be null!");
		log.info("Finding all by statys [{}].", status);

		Set<ContentType> contentTypes = repository.findAllByStatus(status);

		log.info("ContentTypes found [{}].", contentTypes.size());

		return contentTypes;
	}
}
