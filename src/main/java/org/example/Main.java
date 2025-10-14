//package org.example;
//
//
//import org.example.Library.LibraryData;
//import org.example.Model.*;
//import org.example.Service.LibraryLoanService;
//import org.example.Service.LibraryManagerService;
//import org.example.Storage.CsvHandler;
//import org.example.Storage.LibraryJsonHandler;
//import org.example.Storage.ProtobufHandler;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.regex.Pattern;
//
//public class Main {
//    public static void main(String[] args) {
//        LibraryData library = new LibraryData();
//        LibraryManagerService libraryManagerService = new LibraryManagerService(library);
//        LibraryLoanService libraryLoanService = new LibraryLoanService(library);
//        LibraryJsonHandler libraryJsonHandler = new LibraryJsonHandler(library);
//        ProtobufHandler protobufHandler = new ProtobufHandler(library);
//        CsvHandler csvHandler = new CsvHandler();
//        csvHandler.loadData(libraryManagerService); //Load from CSV file
////        protobufHandler.loadFromProto("library_data.bin");
//
////        libraryJsonHandler.loadFromJson("test.json"); //Load from Json file
//
//
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//        while (running) {
//            String command = scanner.nextLine();
//            switch (command) {
//                case "add":
//                    addToLibrary(scanner,libraryManagerService);
//                    break;
//                case "remove":
//                    removeFromLibrary(scanner , libraryManagerService);
//                    break;
//                case "borrow":
//                    borrowFromLibrary(scanner, libraryLoanService);
//                    break;
//                case "return":
//                    returnToLibrary(scanner ,libraryLoanService);
//                    break;
//                case "status":
//                    libraryManagerService.printAll();
//                    break;
//                case "search by author":
//                    authorSearch(scanner , libraryManagerService);
//                    break;
//                case "search by title":
//                    titleSearch(scanner , libraryManagerService);
//                    break;
//                case "sort":
//                    libraryManagerService.sortByPublicationYear();
//                    break;
//                case "return time":
//                    returnTime(scanner , libraryLoanService);
//                    break;
//                case "exit":
//                    running = false;
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//        }
////        libraryJsonHandler.saveToJson("test.json");
//        protobufHandler.saveToProto("library_data.bin");
//
//    }
//
//    private static void titleSearch(Scanner scanner, LibraryManagerService libraryManagerService) {
//        System.out.println("Enter title: ");
//        String title = scanner.nextLine();
//        List<LibraryItem> searchRes = libraryManagerService.searchByTitle(title);
//        if(!searchRes.isEmpty()) {
//            CsvHandler.writeLibrary("search by title", searchRes);
//        }else {
//            System.out.println("Title not found");
//        }
//    }
//
//    private static void authorSearch(Scanner scanner, LibraryManagerService libraryManagerService) {
//        System.out.println("Enter Author: ");
//        String author = scanner.nextLine();
//        List<LibraryItem> searchResult = libraryManagerService.searchByAuthor(author);
//        if(!searchResult.isEmpty()) {
//            CsvHandler.writeLibrary("search by author", searchResult);
//        }else {
//            System.out.println("Author not found");
//        }
//    }
//
//    private static void returnToLibrary(Scanner scanner, LibraryLoanService libraryLoanService) {
//        System.out.println("Enter Item ID: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        LibraryItem returnItem = libraryLoanService.returnItem(id);
//        if(returnItem != null) {
//            List<LibraryItem> items = new ArrayList<>();
//            items.add(returnItem);
//            CsvHandler.writeLibrary("return", items);
//        }
//    }
//
//    private static void borrowFromLibrary(Scanner scanner, LibraryLoanService libraryLoanService) {
//        System.out.println("Enter Item ID: ");
//        int id = scanner.nextInt();
//        LibraryItem item = libraryLoanService.borrowItem(id);
//        if ( item != null ) {
//            List<LibraryItem> items = new ArrayList<>();
//            items.add(item);
//            CsvHandler.writeLibrary("borrow", items);
//        }
//        scanner.nextLine();
//    }
//
//    private static void removeFromLibrary(Scanner scanner, LibraryManagerService libraryManagerService) {
//        System.out.print("Please enter the Item's ID: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        LibraryItem item = libraryManagerService.deleteItem(id);
//        List<LibraryItem> items = new ArrayList<>();
//        items.add(item);
//        CsvHandler.writeLibrary("remove" , items);
//    }
//
//
//
//    public static void returnTime(Scanner scanner , LibraryLoanService libraryLoanService) {
//        String dateReg = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
//        System.out.println("Enter Book ID: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        System.out.println("Enter New Time (yyyy-MM-dd): ");
//        String date = scanner.nextLine();
//        boolean reg = Pattern.matches(dateReg, date);
//        if(reg) {
//            LibraryItem editItem = libraryLoanService.editReturnTime(id, date);
//            if (editItem != null) {
//                List<LibraryItem> items = new ArrayList<>();
//                items.add(editItem);
//                CsvHandler.writeLibrary("change return time", items);
//            }
//        }else{
//            System.out.println("Invalid date format");
//        }
//    }
//}
package org.example;

import org.example.View.LibraryView;
import org.example.Repository.LibraryData;
import org.example.Service.*;
import org.example.Storage.ProtobufHandler;
import org.example.Threads.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        LibraryData library = new LibraryData();
        LibraryManagerService managerService = new LibraryManagerService(library);
        LibraryLoanService loanService = new LibraryLoanService(library);

        ProtobufHandler protobufHandler = new ProtobufHandler(library);
        protobufHandler.loadFromProto("library_data.bin");

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        LibraryView view = new LibraryView(managerService, loanService);

        Thread userThread = new UserInputThread(queue, view);
        Thread managerThread = new ManagerThread(queue, loanService, protobufHandler);

        userThread.start();
        managerThread.start();

    }
}
