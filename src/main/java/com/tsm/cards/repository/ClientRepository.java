package com.tsm.cards.repository;

import com.tsm.cards.model.Client;
import com.tsm.cards.model.Client.ClientStatus;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientRepository extends Repository<Client, Integer>, IBaseRepository<Client, Integer> {

	Optional<Client> findByToken(final String token);

	Set<Client> findByStatus(final ClientStatus status);

}
