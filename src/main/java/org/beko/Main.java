package org.beko;

import org.beko.Handler.MainHandler;
import org.beko.Service.AdminService;
import org.beko.Service.BookingService;
import org.beko.Service.PlaceService;
import org.beko.Service.UserService;
import org.beko.Util.ScannerWrapper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScannerWrapper scanner = new ScannerWrapper(new Scanner(System.in));
        UserService userService = new UserService();
        PlaceService placeService = new PlaceService();
        BookingService bookingService = new BookingService();
        AdminService adminService = new AdminService();

        MainHandler mainHandler = new MainHandler(scanner, userService, placeService, bookingService, adminService);

        mainHandler.handleMainActions();
    }
}
