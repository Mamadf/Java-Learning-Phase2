package org.example;


import org.example.Library.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryLoanService;
import org.example.Service.LibraryManagerService;
import org.example.Storage.CsvHandler;
import org.example.Storage.LibraryJsonHandler;
import org.example.Storage.ProtobufHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        LibraryData library = new LibraryData();
        LibraryManagerService libraryManagerService = new LibraryManagerService(library);
        LibraryLoanService libraryLoanService = new LibraryLoanService(library);
        LibraryJsonHandler libraryJsonHandler = new LibraryJsonHandler(library);
        ProtobufHandler protobufHandler = new ProtobufHandler(library);
        CsvHandler csvHandler = new CsvHandler();
        csvHandler.loadData(libraryManagerService); //Load from CSV file
//        protobufHandler.loadFromProto("library_data.bin");

//        libraryJsonHandler.loadFromJson("test.json"); //Load from Json file


        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "add":
                    addToLibrary(scanner,libraryManagerService);
                    break;
                case "remove":
                    removeFromLibrary(scanner , libraryManagerService);
                    break;
                case "borrow":
                    borrowFromLibrary(scanner, libraryLoanService);
                    break;
                case "return":
                    returnToLibrary(scanner ,libraryLoanService);
                    break;
                case "status":
                    libraryManagerService.printAll();
                    break;
                case "search by author":
                    authorSearch(scanner , libraryManagerService);
                    break;
                case "search by title":
                    titleSearch(scanner , libraryManagerService);
                    break;
                case "sort":
                    libraryManagerService.sortByPublicationYear();
                    break;
                case "return time":
                    returnTime(scanner , libraryLoanService);
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
//        libraryJsonHandler.saveToJson("test.json");
        protobufHandler.saveToProto("library_data.bin");

    }

    private static void titleSearch(Scanner scanner, LibraryManagerService libraryManagerService) {
        System.out.println("Enter title: ");
        String title = scanner.nextLine();
        List<LibraryItem> searchRes = libraryManagerService.searchByTitle(title);
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search by title", searchRes);
        }else {
            System.out.println("Title not found");
        }
    }

    private static void authorSearch(Scanner scanner, LibraryManagerService libraryManagerService) {
        System.out.println("Enter Author: ");
        String author = scanner.nextLine();
        List<LibraryItem> searchResult = libraryManagerService.searchByAuthor(author);
        if(!searchResult.isEmpty()) {
            CsvHandler.writeLibrary("search by author", searchResult);
        }else {
            System.out.println("Author not found");
        }
    }

    private static void returnToLibrary(Scanner scanner, LibraryLoanService libraryLoanService) {
        System.out.println("Enter Item ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        LibraryItem returnItem = libraryLoanService.returnItem(id);
        if(returnItem != null) {
            List<LibraryItem> items = new ArrayList<>();
            items.add(returnItem);
            CsvHandler.writeLibrary("return", items);
        }
    }

    private static void borrowFromLibrary(Scanner scanner, LibraryLoanService libraryLoanService) {
        System.out.println("Enter Item ID: ");
        int id = scanner.nextInt();
        LibraryItem item = libraryLoanService.borrowItem(id);
        if ( item != null ) {
            List<LibraryItem> items = new ArrayList<>();
            items.add(item);
            CsvHandler.writeLibrary("borrow", items);
        }
        scanner.nextLine();
    }

    private static void removeFromLibrary(Scanner scanner, LibraryManagerService libraryManagerService) {
        System.out.print("Please enter the Item's ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        LibraryItem item = libraryManagerService.deleteItem(id);
        List<LibraryItem> items = new ArrayList<>();
        items.add(item);
        CsvHandler.writeLibrary("remove" , items);
    }



    public static void returnTime(Scanner scanner , LibraryLoanService libraryLoanService) {
        String dateReg = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        System.out.println("Enter Book ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Time (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        boolean reg = Pattern.matches(dateReg, date);
        if(reg) {
            LibraryItem editItem = libraryLoanService.editReturnTime(id, date);
            if (editItem != null) {
                List<LibraryItem> items = new ArrayList<>();
                items.add(editItem);
                CsvHandler.writeLibrary("change return time", items);
            }
        }else{
            System.out.println("Invalid date format");
        }
    }
    public static void addToLibrary(Scanner scanner , LibraryManagerService libraryManagerService) {
        System.out.print("Please enter the item type: (Book, Magazine, ReferenceBook, Thesis): )");
        String type = scanner.nextLine();
        System.out.print("Please enter the item title: ");
        String title = scanner.nextLine();
        System.out.print("Please enter the item author: ");
        String author = scanner.nextLine();
        System.out.print("Please enter the publication year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Please enter the availability of the item: ");
        boolean available = Boolean.parseBoolean(scanner.nextLine());
        switch (type) {
            case "Book":
                System.out.print("Please enter the genre: ");
                String genre =  scanner.nextLine();
                System.out.print("Please enter the number of pages: ");
                int pages = scanner.nextInt();
                scanner.nextLine();
                libraryManagerService.addItem(new Book(title, author, year, available, genre, pages));
                break;
            case "Magazine":
                System.out.print("Please enter the publisher: ");
                String publisher = scanner.nextLine();
                System.out.print("Please enter the issue number: ");
                int issue =scanner.nextInt();
                scanner.nextLine();
                libraryManagerService.addItem(new Magazine(title, author, year, available, publisher, issue));
                break;
            case "ReferenceBook":
                System.out.print("Please enter the subject: ");
                String subject = scanner.nextLine();
                System.out.print("Please enter the edition: ");
                String edition = scanner.nextLine();
                libraryManagerService.addItem(new ReferenceBook(title, author, year, available, subject, edition));
                break;
            case "Thesis":
                System.out.print("Please enter the university: ");
                String university = scanner.nextLine();
                System.out.print("Please enter the supervisor: ");
                String supervisor = scanner.nextLine();
                libraryManagerService.addItem(new Thesis(title, author, year, available, university, supervisor));
                break;
            default:
                System.err.println("Unknown item type: " + type);
        }
    }
}
