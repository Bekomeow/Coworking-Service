package org.beko;

import org.beko.Entity.Place;
import org.beko.Handler.AdminHandler;
import org.beko.Service.PlaceService;
import org.beko.Util.ScannerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminHandlerTest {

    private ScannerWrapper scanner;
    private PlaceService placeService;
    private AdminHandler adminHandler;

    @BeforeEach
    public void setUp() {
        scanner = mock(ScannerWrapper.class);
        placeService = mock(PlaceService.class);
        adminHandler = new AdminHandler(scanner, placeService);
    }

    @Test
    public void testViewPlaces() {
        List<Place> places = Arrays.asList(new Place("1", "Meeting Room", "conference room"));
        when(placeService.listPlaces()).thenReturn(places);

        adminHandler.viewPlaces();

        verify(placeService, times(1)).listPlaces();
    }

    @Test
    public void testAddPlaceWorkspace() {
        when(scanner.nextLine()).thenReturn("New Place", "1");

        adminHandler.addPlace();

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
        verify(placeService).addPlace(nameCaptor.capture(), typeCaptor.capture());

        assertEquals("New Place", nameCaptor.getValue());
        assertEquals("workspace", typeCaptor.getValue());
    }

    @Test
    public void testAddPlaceConferenceRoom() {
        when(scanner.nextLine()).thenReturn("New Place", "2");

        adminHandler.addPlace();

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
        verify(placeService).addPlace(nameCaptor.capture(), typeCaptor.capture());

        assertEquals("New Place", nameCaptor.getValue());
        assertEquals("conference room", typeCaptor.getValue());
    }

    @Test
    public void testUpdatePlace() {
        when(scanner.nextLine()).thenReturn("1", "Updated Place", "2");
        when(placeService.hasPlace("1")).thenReturn(true);

        adminHandler.updatePlace();

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
        verify(placeService).updatePlace(idCaptor.capture(), nameCaptor.capture(), typeCaptor.capture());

        assertEquals("1", idCaptor.getValue());
        assertEquals("Updated Place", nameCaptor.getValue());
        assertEquals("conference room", typeCaptor.getValue());
    }

    @Test
    public void testDeletePlace() {
        when(scanner.nextLine()).thenReturn("1");
        when(placeService.hasPlace("1")).thenReturn(true);

        adminHandler.deletePlace();

        verify(placeService).deletePlace("1");
    }

    @Test
    public void testHandleAdminActionsLogout() {
        when(scanner.nextLine()).thenReturn("5");

        adminHandler.handleAdminActions();

        verify(scanner, times(1)).nextLine();
    }

    @Test
    public void testHandleAdminActionsInvalidOption() {
        when(scanner.nextLine()).thenReturn("invalid", "5");

        adminHandler.handleAdminActions();

        verify(scanner, times(2)).nextLine();
    }
}
