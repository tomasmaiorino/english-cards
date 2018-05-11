package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.CardType;
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.repository.CardTypeRepository;
import com.tsm.cards.repository.IBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Set;

import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CARD_TYPE;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CardTypeService extends BaseService<CardType, Integer> {

	@Autowired
	private CardTypeRepository repository;

	@Override
	protected void saveValidation(CardType model) {
		repository.findByName(model.getName()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_CARD_TYPE);
		});

	}

	@Override
	protected void updateValidation(final CardType origin, final CardType model) {
		repository.findByName(model.getName()).ifPresent(c -> {
			if (!c.getId().equals(origin.getId())) {
				throw new BadRequestException(DUPLICATED_CARD_TYPE);
			}
		});
	}

	protected void merge(final CardType origin, final CardType model) {
		origin.setName(model.getName());
		origin.setCardTypeStatus(model.getStatus());
		origin.setImgUrl(model.getImgUrl());
	}

	public Set<CardType> findAllByStatus(final CardTypeStatus status) {
		Assert.notNull(status, "The status must not be null!");
		log.info("Finding all by statys [{}].", status);

		Set<CardType> cardTypes = repository.findAllByStatus(status);

		log.info("CardTypes found [{}].", cardTypes.size());

		return cardTypes;
	}

	@Override
	public IBaseRepository<CardType, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return CardType.class.getSimpleName();
	}
}
