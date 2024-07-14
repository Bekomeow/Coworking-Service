package org.beko.service;

import org.beko.model.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PlaceService {
    /**
     * Adds a new place with the specified name and type.
     *
     * @param name the name of the place
     * @param type the type of the place (workspace or conference room)
     * @return the created Place object
     */
    Place addPlace(String name, String type);

    /**
     * gets a place with the specified name.
     *
     * @param name the name of the place
     * @return the Place object
     */
    Optional<Place> getPlaceByName(String name);

    /**
     * Updates a place with the specified ID, name, and type.
     *
     * @param id   the place ID
     * @param name the new name of the place
     * @param type the new type of the place
     */
    void updatePlace(Long id, String name, String type);

    /**
     * Checks if a place exists by its ID.
     *
     * @param id the place ID
     * @return true if the place exists, false otherwise
     */
    boolean hasPlace(Long id);

    /**
     * Deletes a place by its ID.
     *
     * @param id the place ID
     */
    void deletePlace(Long id);

    /**
     * Lists all places.
     *
     * @return a list of all places
     */
    List<Place> listPlaces();

    /**
     * Retrieves a place by its ID.
     *
     * @param id the place ID
     * @return an Optional containing the place if found, or an empty Optional if not found
     */
    Optional<Place> getPlaceById(Long id);
}
