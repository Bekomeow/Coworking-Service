package org.beko.service;

import org.beko.DAO.impl.PlaceDAOImpl;
import org.beko.model.Place;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class PlaceService {
    private static final PlaceDAOImpl PLACE_DAO = new PlaceDAOImpl();

    public Place addPlace(String name, String type) {
        Place place = new Place(name, type);
        PLACE_DAO.save(place);
        return place;
    }

    public void updatePlace(Long id, String name, String type) {
        Place place = new Place(id, name, type);
        PLACE_DAO.update(place);
    }

    public boolean hasPlace(Long id) {
        Optional<Place> maybePlace = Optional.ofNullable(PLACE_DAO.findById(id));
        return maybePlace.isPresent();
    }

    public void deletePlace(Long id) {
        PLACE_DAO.deleteById(id);
    }

    public List<Place> listPlaces() {
        return PLACE_DAO.findAll();
    }

    public Optional<Place> getPlaceById(Long id) {
        return Optional.ofNullable(PLACE_DAO.findById(id));
    }
}
