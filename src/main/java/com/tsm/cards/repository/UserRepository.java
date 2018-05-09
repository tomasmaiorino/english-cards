
package com.tsm.cards.repository;


import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.cards.model.User;

@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends Repository<User, Integer>, IBaseRepository<User, Integer> {

	Optional<User> findByEmail(final String email);

}
