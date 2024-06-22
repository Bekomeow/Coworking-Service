package org.beko.Service;

import org.beko.Entity.Place;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceService {
    private HashMap<String, Place> places = new HashMap<>();
    private int placeIgCounter = 0;

    public Place addPlace(String name, String type) {
        String id = String.valueOf(placeIgCounter++);
        Place place = new Place(id, name, type);
        places.put(id, place);
        return place;
    }

    public void updatePlace(String id, String name, String type) {
        Place place = new Place(id, name, type);
        places.put(id, place);
    }

    public boolean hasPlace(String id) {
        return places.containsKey(id);
    }

    public void deletePlace(String id) {
        places.remove(id);
    }

    public List<Place> listPlaces() {
        return new ArrayList<>(places.values());
    }

    public Place getPlaceById(String id) {
        return places.get(id);
    }
}
