package com.tsm.cards.service;

import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CONTENT_TYPE;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.repository.ContentTypeRepository;
import com.tsm.cards.repository.IBaseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ContentTypeService extends BaseService<ContentType, Integer> {

	@Autowired
	private ContentTypeRepository repository;

	@Override
	protected void saveValidation(final ContentType model) {
		repository.findByName(model.getName()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_CONTENT_TYPE);
		});
	}

	@Override
	protected void updateValidation(final ContentType origin, final ContentType model) {

		repository.findByName(model.getName()).ifPresent(c -> {
			if (!c.getId().equals(origin.getId())) {
				throw new BadRequestException(DUPLICATED_CONTENT_TYPE);
			}
		});

	}

	@Override
	protected void merge(final ContentType origin, final ContentType model) {
		origin.setName(model.getName());
		origin.setImgUrl(model.getImgUrl());
		origin.setStatus(model.getStatus());
		origin.setRules(model.getRules());
	}

	public Set<ContentType> findAllByStatus(final ContentTypeStatus status) {
		Assert.notNull(status, "The status must not be null!");
		log.info("Finding all by statys [{}].", status);

		Set<ContentType> contentTypes = repository.findAllByStatus(status);

		log.info("ContentTypes found [{}].", contentTypes.size());

		return contentTypes;
	}

	@Override
	public IBaseRepository<ContentType, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return ContentType.class.getSimpleName();
	}
}
