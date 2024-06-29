package org.beko;

import lombok.RequiredArgsConstructor;
import org.beko.controller.ServiceController;
import org.beko.handler.AdminHandler;
import org.beko.handler.MainHandler;
import org.beko.handler.UserHandler;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.util.ConnectionManager;
import org.beko.wrapper.ScannerWrapper;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Runner class that starts the application and handles the main menu.
 */
@RequiredArgsConstructor
public class Runner {
    private static final ScannerWrapper scanner = new ScannerWrapper(new Scanner(System.in));
    private static final ServiceController serviceController = new ServiceController();
    private static final AdminHandler adminHandler = new AdminHandler(scanner ,serviceController);
    private static final UserHandler userHandler = new UserHandler(scanner, serviceController);
    private static final MainHandler mainHandler = new MainHandler(scanner, serviceController, adminHandler, userHandler);

    /**
     * Runs the application, including initializing the database and displaying the main menu.
     */
    public static void run() {
        try (var connection = ConnectionManager.getConnection()) {
            LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
            liquibaseDemo.runMigrations(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                case "1" -> mainHandler.handleUserRegistration();
                case "2" -> mainHandler.handleUserLogin();
                case "3" -> mainHandler.handleAdminLogin();
                case "4" -> {
                    System.out.println("Exit");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
