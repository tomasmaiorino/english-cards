package com.tsm.cards.repository;

import com.tsm.cards.model.Client.ClientStatus;
import com.tsm.cards.model.ClientHosts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientHostsRepository extends Repository<ClientHosts, Integer> {

    @Query("SELECT c FROM ClientHosts c WHERE c.client.status = ?1")
    Set<ClientHosts> findByClientStatus(final ClientStatus status);

}
