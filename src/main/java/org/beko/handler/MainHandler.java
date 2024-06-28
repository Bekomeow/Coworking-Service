package org.beko.handler;

import org.beko.model.User;
import org.beko.service.AdminService;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.beko.util.ScannerWrapper;

public class MainHandler {
    private final ScannerWrapper scanner;
    private final UserService userService;
    private final AdminService adminService;
    private final AdminHandler adminHandler;
    private final UserHandler userHandler;

    public MainHandler(ScannerWrapper scanner, UserService userService, PlaceService placeService, BookingService bookingService, AdminService adminService) {
        this.scanner = scanner;
        this.userService = userService;
        this.adminService = adminService;
        adminHandler = new AdminHandler(scanner, placeService);
        userHandler = new UserHandler(scanner, userService, placeService, bookingService);
    }

    public void handleMainActions() {
        label:
        while (true) {
            System.out.println("----------------------");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.println("----------------------");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    handleUserRegistration();
                    break;
                case "2":
                    handleUserLogin();
                    break;
                case "3":
                    handleAdminLogin();
                    break;
                case "4":
                    System.out.println("Exit");
                    break label;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    public void handleUserRegistration() {
        System.out.println("Register");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            userService.register(username, password);
            System.out.println("Registration successful.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public void handleUserLogin() {
        System.out.println("Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            User user = userService.login(username, password);
            System.out.println("Login successful.");
            userHandler.handleUserActions(user);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleAdminLogin() {
        System.out.println("Login as Admin");
        System.out.print("Enter admin name: ");
        String adminName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            adminService.login(adminName, password);
            System.out.println("Login successful.");
            adminHandler.handleAdminActions();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}