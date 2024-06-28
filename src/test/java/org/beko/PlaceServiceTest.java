package org.beko;

import org.beko.model.Place;
import org.beko.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
    private PlaceService placeService;

    @BeforeEach
    public void setUp() {
        placeService = new PlaceService();
    }

    @Test
    public void testAddPlace() {
        Place place = placeService.addPlace("Test Place", "workspace");
        assertNotNull(place);
        assertEquals("Test Place", place.getName());
        assertEquals("workspace", place.getType());
    }

    @Test
    public void testUpdatePlace() {
        Place place = placeService.addPlace("Test Place", "workspace");
        placeService.updatePlace(place.getId(), "Updated Place", "conference room");
        Place updatedPlace = placeService.getPlaceById(place.getId());
        assertEquals("Updated Place", updatedPlace.getName());
        assertEquals("conference room", updatedPlace.getType());
    }

    @Test
    public void testDeletePlace() {
        Place place = placeService.addPlace("Test Place", "workspace");
        placeService.deletePlace(place.getId());
        assertFalse(placeService.hasPlace(place.getId()));
    }

    @Test
    public void testListPlaces() {
        placeService.addPlace("Place 1", "workspace");
        placeService.addPlace("Place 2", "conference room");
        List<Place> places = placeService.listPlaces();
        assertEquals(2, places.size());
    }
}
