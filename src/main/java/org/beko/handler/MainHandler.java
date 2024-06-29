package org.beko.handler;

import lombok.RequiredArgsConstructor;
import org.beko.controller.ServiceController;
import org.beko.model.User;
import org.beko.wrapper.ScannerWrapper;

@RequiredArgsConstructor
public class MainHandler {
    private final ScannerWrapper scanner;
    private final ServiceController serviceController;
    private final AdminHandler adminHandler;
    private final UserHandler userHandler;

    public void handleUserRegistration() {
        System.out.println("Register");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            serviceController.register(username, password);
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
            User user = serviceController.loginAsUser(username, password);
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
            serviceController.loginAsAdmin(adminName, password);
            System.out.println("Login successful.");
            adminHandler.handleAdminActions();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}