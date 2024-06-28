package org.beko.handler;

import org.beko.model.Place;
import org.beko.service.PlaceService;
import org.beko.util.ScannerWrapper;

import java.util.List;

public class AdminHandler {
    private final ScannerWrapper scanner;
    private final PlaceService placeService;

    public AdminHandler(ScannerWrapper scanner, PlaceService placeService) {
        this.scanner = scanner;
        this.placeService = placeService;
    }

    public void handleAdminActions() {
        while (true) {
            displayAdminMenu();
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> viewPlaces();
                case "2" -> addPlace();
                case "3" -> updatePlace();
                case "4" -> deletePlace();
                case "5" -> {
                    System.out.println("Logout");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void displayAdminMenu() {
        System.out.println("------ADMIN MODE------");
        System.out.println("1. View Places");
        System.out.println("2. Add Place");
        System.out.println("3. Update Place");
        System.out.println("4. Delete Place");
        System.out.println("5. Logout");
        System.out.println("----------------------");
        System.out.print("Choose an option: ");
    }

    public void viewPlaces() {
        System.out.println("View Places");
        List<Place> places = placeService.listPlaces();
        places.forEach(System.out::println);
    }

    public void addPlace() {
        while (true) {
            System.out.println("Add Place");
            System.out.print("Enter place name: ");
            String name = scanner.nextLine();
            System.out.println("1. workspace");
            System.out.println("2. conference room");
            System.out.print("Enter place type (1/2): ");
            String type = scanner.nextLine();
            if (type.equals("1")) {
                placeService.addPlace(name, "workspace");
                System.out.println("Place added successfully.");
                break;
            } else if (type.equals("2")) {
                placeService.addPlace(name, "conference room");
                System.out.println("Place added successfully.");
                break;
            } else {
                System.out.println("Invalid type. Try again.");
                System.out.println("----------------------");
            }
        }
    }

    public void updatePlace() {
        while (true) {
            System.out.println("Update Place");
            System.out.print("Enter place ID: ");
            String id = scanner.nextLine();
            if (!placeService.hasPlace(id)) {
                System.out.println("Place not found.");
                continue;
            }
            System.out.print("Enter new place name: ");
            String name = scanner.nextLine();
            System.out.println("1. workspace");
            System.out.println("2. conference room");
            System.out.print("Enter place type (1/2): ");
            String type = scanner.nextLine();
            if (type.equals("1")) {
                placeService.updatePlace(id, name, "workspace");
                System.out.println("Place updated successfully.");
                break;
            } else if (type.equals("2")) {
                placeService.updatePlace(id, name, "conference room");
                System.out.println("Place updated successfully.");
                break;
            } else {
                System.out.println("Invalid type. Try again.");
                System.out.println("----------------------");
            }
        }
    }

    public void deletePlace() {
        System.out.println("Delete Resource");
        System.out.print("Enter place ID: ");
        String id = scanner.nextLine();
        if (!placeService.hasPlace(id)) {
            System.out.println("Place not found.");
            return;
        }
        placeService.deletePlace(id);
        System.out.println("Place deleted successfully.");
    }
}

