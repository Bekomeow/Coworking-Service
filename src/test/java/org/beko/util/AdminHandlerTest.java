//package org.beko.util;
//
//import org.beko.model.Place;
//import org.beko.handler.AdminHandler;
//import org.beko.service.PlaceService;
//import org.beko.wrapper.ScannerWrapper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class AdminHandlerTest {
//
//    private ScannerWrapper scanner;
//    private PlaceService placeService;
//    private AdminHandler adminHandler;
//
//    @BeforeEach
//    public void setUp() {
//        scanner = Mockito.mock(ScannerWrapper.class);
//        placeService = Mockito.mock(PlaceService.class);
//        adminHandler = new AdminHandler(scanner, placeService);
//    }
//
//    @Test
//    public void testViewPlaces() {
//        List<Place> places = Arrays.asList(new Place("1", "Meeting Room", "conference room"));
//        Mockito.when(placeService.listPlaces()).thenReturn(places);
//
//        adminHandler.viewPlaces();
//
//        Mockito.verify(placeService, Mockito.times(1)).listPlaces();
//    }
//
//    @Test
//    public void testAddPlaceWorkspace() {
//        Mockito.when(scanner.nextLine()).thenReturn("New Place", "1");
//
//        adminHandler.addPlace();
//
//        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(placeService).addPlace(nameCaptor.capture(), typeCaptor.capture());
//
//        Assertions.assertEquals("New Place", nameCaptor.getValue());
//        Assertions.assertEquals("workspace", typeCaptor.getValue());
//    }
//
//    @Test
//    public void testAddPlaceConferenceRoom() {
//        Mockito.when(scanner.nextLine()).thenReturn("New Place", "2");
//
//        adminHandler.addPlace();
//
//        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(placeService).addPlace(nameCaptor.capture(), typeCaptor.capture());
//
//        Assertions.assertEquals("New Place", nameCaptor.getValue());
//        Assertions.assertEquals("conference room", typeCaptor.getValue());
//    }
//
//    @Test
//    public void testUpdatePlace() {
//        Mockito.when(scanner.nextLine()).thenReturn("1", "Updated Place", "2");
//        Mockito.when(placeService.hasPlace("1")).thenReturn(true);
//
//        adminHandler.updatePlace();
//
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(placeService).updatePlace(idCaptor.capture(), nameCaptor.capture(), typeCaptor.capture());
//
//        Assertions.assertEquals("1", idCaptor.getValue());
//        Assertions.assertEquals("Updated Place", nameCaptor.getValue());
//        Assertions.assertEquals("conference room", typeCaptor.getValue());
//    }
//
//    @Test
//    public void testDeletePlace() {
//        Mockito.when(scanner.nextLine()).thenReturn("1");
//        Mockito.when(placeService.hasPlace("1")).thenReturn(true);
//
//        adminHandler.deletePlace();
//
//        Mockito.verify(placeService).deletePlace("1");
//    }
//
//    @Test
//    public void testHandleAdminActionsLogout() {
//        Mockito.when(scanner.nextLine()).thenReturn("5");
//
//        adminHandler.handleAdminActions();
//
//        Mockito.verify(scanner, Mockito.times(1)).nextLine();
//    }
//
//    @Test
//    public void testHandleAdminActionsInvalidOption() {
//        Mockito.when(scanner.nextLine()).thenReturn("invalid", "5");
//
//        adminHandler.handleAdminActions();
//
//        Mockito.verify(scanner, Mockito.times(2)).nextLine();
//    }
//}
