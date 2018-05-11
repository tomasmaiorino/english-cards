package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Client;
import com.tsm.cards.repository.ClientRepository;
import com.tsm.cards.repository.IBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.tsm.cards.util.ErrorCodes.CLIENT_NOT_FOUND;
import static com.tsm.cards.util.ErrorCodes.DUPLICATED_TOKEN;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ClientService extends BaseService<Client, Integer> {

	@Autowired
	private ClientRepository repository;

	@Override
	protected void saveValidation(final Client model) {
		repository.findByToken(model.getToken()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_TOKEN);
		});

	}

	public Client findByToken(final String token) {
		Assert.hasText(token, "The token must not be null or empty.");
		log.info("Finding client by token [{}] .", token);

		Client client = repository.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND));

		log.info("Client found [{}].", client);

		return client;
	}

	@Override
	public IBaseRepository<Client, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Client.class.getSimpleName();
	}
}
