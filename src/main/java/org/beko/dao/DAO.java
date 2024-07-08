package org.beko.dao;

import java.util.List;

/**
 * Generic DAO interface for CRUD operations.
 *
 * @param <K> the type of the primary key
 * @param <E> the type of the entity
 */
public interface DAO<K, E> {
    /**
     * Saves an entity.
     *
     * @param entity the entity to save
     */
    E save(E entity);

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity
     * @return the entity with the specified ID
     */
    E findById(K id);

    /**
     * Finds all entities.
     *
     * @return a list of all entities
     */
    List<E> findAll();

    /**
     * Updates an entity.
     *
     * @param entity the entity to update
     */
    void update(E entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(K id);
}