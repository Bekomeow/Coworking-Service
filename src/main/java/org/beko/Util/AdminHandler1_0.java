//import org.beko.Entity.Place;
//import org.beko.Service.PlaceService;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class AdminHandler {
//    private Scanner scanner;
//    private PlaceService placeService;
//
//    public AdminHandler(Scanner scanner,PlaceService placeService) {
//        this.scanner = scanner;
//        this.placeService = placeService;
//    }
//
//    public AdminHandler() {
//        this(new Scanner(System.in), new PlaceService());
//    }
//
//    public void handleAdminActions() {
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
//}
