package org.beko;

import org.beko.liquibase.LiquibaseDemo;
import org.beko.service.AdminService;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.beko.util.ConnectionManager;
import org.beko.util.ScannerWrapper;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScannerWrapper scanner = new ScannerWrapper(new Scanner(System.in));
        UserService userService = new UserService();
        PlaceService placeService = new PlaceService();
        BookingService bookingService = new BookingService();
        AdminService adminService = new AdminService();

        try (var connection = ConnectionManager.getConnection()) {
            LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
            liquibaseDemo.runMigrations(connection);
            System.out.println(connection.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        MainHandler mainHandler = new MainHandler(scanner, userService, placeService, bookingService, adminService);

//        mainHandler.handleMainActions();
    }
}
