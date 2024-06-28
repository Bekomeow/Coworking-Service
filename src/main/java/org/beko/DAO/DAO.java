package org.beko.DAO;

import org.beko.model.Place;

import java.util.List;

public interface DAO <K, E>{
    void save(E entity);

    E findById(K id);

    List<E> findAll();

    void update(E entity);

    void deleteById(K id);
}
