package com.tsm.cards.service;

import static com.tsm.cards.util.ErrorCodes.CLIENT_NOT_FOUND;
import static com.tsm.cards.util.ErrorCodes.DUPLICATED_TOKEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Client;
import com.tsm.cards.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional
	public Client save(final Client client) {
		Assert.notNull(client, "The client must not be null.");
		log.info("Saving client [{}] .", client);

		repository.findByToken(client.getToken()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_TOKEN);
		});

		repository.save(client);

		log.info("Saved client [{}].", client);
		return client;
	}

	public Client findByToken(final String token) {
		Assert.hasText(token, "The token must not be null or empty.");
		log.info("Finding client by token [{}] .", token);

		Client client = repository.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND));

		log.info("Client found [{}].", client);

		return client;
	}
}
