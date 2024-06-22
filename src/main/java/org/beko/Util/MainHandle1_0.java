//package org.beko.Handler;
//
//import org.beko.Entity.Booking;
//import org.beko.Entity.Place;
//import org.beko.Entity.User;
//import org.beko.Service.AdminService;
//import org.beko.Service.BookingService;
//import org.beko.Service.PlaceService;
//import org.beko.Service.UserService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//
//public class MainHandler {
//    private Scanner scanner;
//    private UserService userService;
//    private PlaceService placeService;
//    private BookingService bookingService;
//    private AdminService adminService;
//
//    public MainHandler(Scanner scanner, UserService userService, PlaceService placeService, BookingService bookingService, AdminService adminService) {
//        this.scanner = scanner;
//        this.userService = userService;
//        this.placeService = placeService;
//        this.bookingService = bookingService;
//        this.adminService = adminService;
//    }
//
//    public void handleMainActions() {
//        label:
//        while (true) {
//            System.out.println("----------------------");
//            System.out.println("1. Register");
//            System.out.println("2. Login");
//            System.out.println("3. Login as Admin");
//            System.out.println("4. Exit");
//            System.out.println("----------------------");
//            System.out.print("Choose an option: ");
//            String option = scanner.nextLine();
//
//            switch (option) {
//                case "1": {
//                    System.out.println("Register");
//                    System.out.print("Enter username: ");
//                    String username = scanner.nextLine();
//                    System.out.print("Enter password: ");
//                    String password = scanner.nextLine();
//                    try {
//                        userService.register(username, password);
//                        System.out.println("Registration successful.");
//                    } catch (IllegalArgumentException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                }
//                case "2": {
//                    System.out.println("Login");
//                    System.out.print("Enter username: ");
//                    String username = scanner.nextLine();
//                    System.out.print("Enter password: ");
//                    String password = scanner.nextLine();
//                    try {
//                        User user = userService.login(username, password);
//                        System.out.println("Login successful.");
//                        handleUserActions(scanner, userService, placeService, bookingService, user);
//                    } catch (IllegalArgumentException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                }
//                case "3": {
//                    System.out.println("Login as Admin");
//                    System.out.print("Enter admin name: ");
//                    String adminName = scanner.nextLine();
//                    System.out.print("Enter password: ");
//                    String password = scanner.nextLine();
//                    try {
//                        adminService.login(adminName, password);
//                        System.out.println("Login successful.");
//                        handleAdminActions(scanner, placeService);
//                    } catch (IllegalArgumentException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                }
//                case "4":
//                    System.out.println("Exit");
//                    break label;
//                default:
//                    System.out.println("Invalid option. Try again.");
//                    break;
//            }
//        }
//    }
//    private static void handleAdminActions(Scanner scanner, PlaceService placeService) {
//        label:
//        while (true) {
//            System.out.println("------ADMIN MODE------");
//            System.out.println("1. View Places");
//            System.out.println("2. Add Place");
//            System.out.println("3. Update Place");
//            System.out.println("4. Delete Place");
//            System.out.println("5. Logout");
//            System.out.println("----------------------");
//            System.out.print("Choose an option: ");
//            String option = scanner.nextLine();
//
//            switch (option) {
//                case "1":
//                    System.out.println("View Places");
//                    List<Place> places = placeService.listPlaces();
//                    for (Place place : places) {
//                        System.out.println(place);
//                    }
//                    break;
//                case "2":
//                    while (true) {
//                        System.out.println("Add Place");
//                        System.out.print("Enter place name: ");
//                        String name = scanner.nextLine();
//                        System.out.println("1. workspace");
//                        System.out.println("2. conference room");
//                        System.out.print("Enter place type (1/2): ");
//                        String type = scanner.nextLine();
//                        if (type.equals("1")) {
//                            placeService.addPlace(name, "workspace");
//                            System.out.println("Place added successfully.");
//                            break;
//                        } else if (type.equals("2")) {
//                            placeService.addPlace(name, "conference room");
//                            System.out.println("Place added successfully.");
//                            break;
//                        } else {
//                            System.out.println("Invalid type. Try again.");
//                            System.out.println("----------------------");
//                        }
//                    }
//                    break;
//                case "3": {
//                    while (true) {
//                        System.out.println("Update Place");
//                        System.out.print("Enter place ID: ");
//                        String id = scanner.nextLine();
//                        if (!placeService.hasPlace(id)) {
//                            System.out.println("Place not found.");
//                            continue;
//                        }
//                        System.out.print("Enter new place name: ");
//                        String name = scanner.nextLine();
//                        System.out.println("1. workspace");
//                        System.out.println("2. conference room");
//                        System.out.print("Enter place type (1/2): ");
//                        String type = scanner.nextLine();
//                        if (type.equals("1")) {
//                            placeService.updatePlace(id, name, "workspace");
//                            System.out.println("Place updated successfully.");
//                            break;
//                        } else if (type.equals("2")) {
//                            placeService.updatePlace(id, name, "conference room");
//                            System.out.println("Place updated successfully.");
//                            break;
//                        } else {
//                            System.out.println("Invalid type. Try again.");
//                            System.out.println("----------------------");
//                        }
//                    }
//                    break;
//                }
//                case "4": {
//                    System.out.println("Delete Resource");
//                    System.out.print("Enter place ID: ");
//                    String id = scanner.nextLine();
//                    if (!placeService.hasPlace(id)) {
//                        System.out.println("Place not found.");
//                        continue;
//                    }
//                    placeService.deletePlace(id);
//                    System.out.println("Place deleted successfully.");
//                    break;
//                }
//                case "5":
//                    System.out.println("Logout");
//                    break label;
//                default:
//                    System.out.println("Invalid option. Try again.");
//                    break;
//            }
//        }
//    }
//
//    private static void handleUserActions(Scanner scanner,UserService userService, PlaceService placeService, BookingService bookingService, User user) {
//        label:
//        while (true) {
//            System.out.println("-------USER MODE-------");
//            System.out.println("1. View Places");
//            System.out.println("2. Book Place");
//            System.out.println("3. Cancel Booking");
//            System.out.println("4. View Bookings");
//            System.out.println("5. View Bookings By User");
//            System.out.println("6. View Bookings By Place");
//            System.out.println("7. View Bookings By Date");
//            System.out.println("8. Logout");
//            System.out.println("-----------------------");
//            System.out.print("Choose an option: ");
//            String option = scanner.nextLine();
//
//            switch (option) {
//                case "1":
//                    System.out.println("View Places");
//                    List<Place> places = placeService.listPlaces();
//                    for (Place place : places) {
//                        System.out.println(place);
//                    }
//                    break;
//                case "2":
//                    System.out.println("Book Place");
//                    System.out.print("Enter place ID: ");
//                    String placeId = scanner.nextLine();
//                    if (!placeService.hasPlace(placeId)) {
//                        System.out.println("Place not found.");
//                        continue;
//                    }
//                    Place place = placeService.getPlaceById(placeId);
//                    System.out.print("Enter start time (YYYY-MM-DDTHH:MM): ");
//                    LocalDateTime startTime = LocalDateTime.parse(scanner.nextLine());
//                    System.out.print("Enter end time (YYYY-MM-DDTHH:MM): ");
//                    LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());
//                    bookingService.bookPlace(user, place, startTime, endTime);
//                    System.out.println("Place booked successfully.");
//                    break;
//                case "3":
//                    System.out.println("Cancel Booking");
//                    System.out.print("Enter booking ID: ");
//                    String id = scanner.nextLine();
//                    if (!bookingService.hasBooking(id)) {
//                        System.out.println("Booking not found.");
//                        continue;
//                    }
//                    bookingService.cancelBooking(id);
//                    System.out.println("Booking cancelled successfully.");
//                    break;
//                case "4":
//                    System.out.println("View Bookings");
//                    List<Booking> bookings = bookingService.listBookings();
//                    for (Booking booking : bookings) {
//                        System.out.println(booking);
//                    }
//                    break;
//                case "5":
//                    System.out.println("View Bookings By User");
//                    System.out.print("Enter user name: ");
//                    String username = scanner.nextLine();
//                    if (!userService.hasUser(username)) {
//                        System.out.println("User not found.");
//                        continue;
//                    }
//                    List<Booking> bookingsByUser = bookingService.listBookingsByUser(username);
//                    for (Booking booking : bookingsByUser) {
//                        System.out.println(booking);
//                    }
//                    break;
//                case "6":
//                    System.out.println("View Bookings By Place");
//                    System.out.print("Enter place ID: ");
//                    String placeId2 = scanner.nextLine();
//                    if (!placeService.hasPlace(placeId2)) {
//                        System.out.println("Place not found.");
//                        continue;
//                    }
//                    List<Booking> bookingsByPlace = bookingService.listBookingsByPlace(placeId2);
//                    for (Booking booking : bookingsByPlace) {
//                        System.out.println(booking);
//                    }
//                    break;
//                case "7":
//                    System.out.println("View Bookings By Date");
//                    System.out.print("Enter place Date (YYYY-MM-DD): ");
//                    LocalDate date = LocalDate.parse(scanner.nextLine());
//                    List<Booking> bookingsByDate = bookingService.listBookingsByDate(date);
//                    for (Booking booking : bookingsByDate) {
//                        System.out.println(booking);
//                    }
//                    break;
//                case "8":
//                    System.out.println("Logout");
//                    break label;
//                default:
//                    System.out.println("Invalid option. Try again.");
//                    break;
//            }
//        }
//    }
//}