package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.Card;
import com.tsm.cards.repository.CardRepository;
import com.tsm.cards.repository.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.tsm.cards.util.ErrorCodes.DUPLICATED_CARD;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CardService extends BaseService<Card, Integer> {

	@Autowired
	private CardRepository repository;

	@Override
	protected void saveValidation(final Card model) {
		repository.findByImgUrl(model.getImgUrl()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_CARD);
		});
	}

	@Override
	protected void updateValidation(final Card origin, final Card model) {
		repository.findByImgUrl(model.getImgUrl()).ifPresent(c -> {
			if (!c.getId().equals(origin.getId())) {
				throw new BadRequestException(DUPLICATED_CARD);
			}
		});

	}

	protected void merge(final Card origin, final Card model) {
		origin.setCardStatus(model.getStatus());
		origin.setCardType(model.getCardType());
		origin.setImgUrl(model.getImgUrl());
		origin.setName(model.getName());
	}

	@Override
	public IBaseRepository<Card, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Card.class.getSimpleName();
	}
}
