package com.tsm.cards.definition.repository;

import java.io.Serializable;
import java.util.Optional;

public interface BaseDefinitionRepository<T, ID extends Serializable> {

    Optional<T> findById(final String id);

    Optional<T> findByDefinitionId(final String definitionId);

    T save(T model);

}
