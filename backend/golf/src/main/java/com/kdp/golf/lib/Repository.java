package com.kdp.golf.lib;

import java.util.Collection;
import java.util.Optional;

/**
 * @param <I> The type of the entity's id.
 * @param <T> The type of entity being stored in the repository.
 */
public interface Repository<I, T> {

    Collection<T> findAll();

    Optional<T> findById(I id);

    Optional<T> create(T t);

    boolean update(T t);

    boolean delete(I id);
}
