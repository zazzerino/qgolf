package com.kdp.golf;

import java.util.Collection;
import java.util.Optional;

/**
 * @param <I> The type of the entity's id.
 * @param <T> The type of entity being stored in the repository.
 */
public interface Repository<I, T> {

    Collection<T> findAll();

    Optional<T> findById(I id);

    T create(T t);

    void update(T t);

    void delete(I id);
}
