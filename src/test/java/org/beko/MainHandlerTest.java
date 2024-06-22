package org.beko;

import org.beko.Entity.User;
import org.beko.Handler.MainHandler;
import org.beko.Service.AdminService;
import org.beko.Service.BookingService;
import org.beko.Service.PlaceService;
import org.beko.Service.UserService;
import org.beko.Util.ScannerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MainHandlerTest {

    private MainHandler mainHandler;
    private ScannerWrapper scanner;
    private UserService userService;
    private AdminService adminService;
    private PlaceService placeService;
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        scanner = Mockito.mock(ScannerWrapper.class);
        userService = Mockito.mock(UserService.class);
        adminService = Mockito.mock(AdminService.class);
        placeService = Mockito.mock(PlaceService.class);
        bookingService = Mockito.mock(BookingService.class);

        mainHandler = new MainHandler(scanner, userService, placeService, bookingService, adminService);
    }

    @Test
    public void testUserRegistration_Successful() {
        when(scanner.nextLine()).thenReturn("testuser", "password");

        when(userService.register("testuser", "password")).thenReturn(new User("testuser", "password"));

        mainHandler.handleUserRegistration();

        verify(userService, times(1)).register("testuser", "password");
    }


    @Test
    public void testUserRegistration_WithException() {
        when(scanner.nextLine()).thenReturn("testuser", "password");

        String errorMessage = "Username already exists.";
        doThrow(new IllegalArgumentException(errorMessage)).when(userService).register(anyString(), anyString());

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mainHandler.handleUserRegistration();

        System.setOut(System.out);

        verify(userService, times(1)).register("testuser", "password");

        assertTrue(outContent.toString().contains(errorMessage));
    }
}

