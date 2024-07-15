package org.beko.service.impl;

import lombok.AllArgsConstructor;
import org.beko.dao.PlaceDAO;
import org.beko.dao.impl.PlaceDAOImpl;
import org.beko.exception.PlaceAlreadyExistException;
import org.beko.model.Place;
import org.beko.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling place operations.
 */
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceDAO PLACE_DAO;

    /**
     * Adds a new place with the specified name and type.
     *
     * @param name the name of the place
     * @param type the type of the place (workspace or conference room)
     * @return the created Place object
     */
    public Place addPlace(String name, String type) {
        Optional<Place> maybePlace = Optional.ofNullable(PLACE_DAO.findByPlaceName(name));

        if(maybePlace.isPresent()) {
            throw new PlaceAlreadyExistException("A place with that name already exists");
        }

        Place place = Place.builder()
                .name(name)
                .type(type)
                .build();

        PLACE_DAO.save(place);
        return place;
    }

    public Optional<Place> getPlaceByName(String name) {
        return Optional.ofNullable(PLACE_DAO.findByPlaceName(name));
    }

    /**
     * Updates a place with the specified ID, name, and type.
     *
     * @param id   the place ID
     * @param name the new name of the place
     * @param type the new type of the place
     */
    public void updatePlace(Long id, String name, String type) {
        Place place = new Place(id, name, type);
        PLACE_DAO.update(place);
    }

    /**
     * Checks if a place exists by its ID.
     *
     * @param id the place ID
     * @return true if the place exists, false otherwise
     */
    public boolean hasPlace(Long id) {
        Optional<Place> maybePlace = Optional.ofNullable(PLACE_DAO.findById(id));
        return maybePlace.isPresent();
    }

    /**
     * Deletes a place by its ID.
     *
     * @param id the place ID
     */
    public void deletePlace(Long id) {
        PLACE_DAO.deleteById(id);
    }

    /**
     * Lists all places.
     *
     * @return a list of all places
     */
    public List<Place> listPlaces() {
        return PLACE_DAO.findAll();
    }

    /**
     * Retrieves a place by its ID.
     *
     * @param id the place ID
     * @return an Optional containing the place if found, or an empty Optional if not found
     */
    public Optional<Place> getPlaceById(Long id) {
        return Optional.ofNullable(PLACE_DAO.findById(id));
    }
}
