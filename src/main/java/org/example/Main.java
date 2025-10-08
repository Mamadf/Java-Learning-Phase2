package org.example;


import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        LibraryLoanService libraryLoanService = new LibraryLoanService(library);
        LibraryJsonHandler libraryJsonHandler = new LibraryJsonHandler(library);
        int id;
        List<LibraryItem> items;
        String title , author;
//        loadData(library); //Load from CSV file
        libraryJsonHandler.loadFromJson("test.json");


        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "add":
                    getAddInputData(scanner,library);
                    break;
                case "borrow":
                    System.out.println("Enter Book ID: ");
                    id = scanner.nextInt();
                    LibraryItem item = libraryLoanService.borrowItem(id);
                    if ( item != null ) {
                        items = new ArrayList<>();
                        items.add(item);
                        writeLibrary("borrow", items);
                    }
                    scanner.nextLine();

                    break;
                case "return":
                    System.out.println("Enter Book ID: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    LibraryItem returnItem = libraryLoanService.returnItem(id);
                    if(returnItem != null) {
                        items = new ArrayList<>();
                        items.add(returnItem);
                        writeLibrary("return", items);
                    }
                    break;
                case "status":
                    library.printAll();
                    break;
                case "search by author":
                    System.out.println("Enter Author: ");
                    author = scanner.nextLine();
                    List<LibraryItem> searchResult = library.searchByAuthor(author);
                    if(!searchResult.isEmpty()) {
                        writeLibrary("search by author", searchResult);
                    }else {
                        System.out.println("Author not found");
                    }
                    break;
                case "search by title":
                    System.out.println("Enter title: ");
                    title = scanner.nextLine();
                    List<LibraryItem> searchRes = library.searchByTitle(title);
                    if(!searchRes.isEmpty()) {
                        writeLibrary("search by title", searchRes);
                    }else {
                        System.out.println("Title not found");
                    }
                    break;
                case "sort":
                    library.sortByPublicationYear();
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
        libraryJsonHandler.saveToJson("test.json");
    }

    public static void loadData(Library library) {
        String fileName = "data.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 7) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                try {
                    String type = values[0];
                    String title = values[1];
                    String author = values[2];
                    int year = Integer.parseInt(values[3]);
                    boolean available = Boolean.parseBoolean(values[4]);
                    String privateField = values[5];

                    switch (type) {
                        case "Book":
                            int pages = Integer.parseInt(values[6]);
                            library.addItem(new Book(title, author, year, available, privateField, pages));
                            break;
                        case "Magazine":
                            int issue = Integer.parseInt(values[6]);
                            library.addItem(new Magazine(title, author, year, available, privateField, issue));
                            break;
                        case "ReferenceBook":
                            String edition = values[6];
                            library.addItem(new ReferenceBook(title, author, year, available, privateField, edition));
                            break;
                        case "Thesis":
                            String supervisor = values[6];
                            library.addItem(new Thesis(title, author, year, available, privateField, supervisor));
                            break;
                        default:
                            System.err.println("Unknown item type: " + type);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in line: " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Could not read file: " + fileName);
        }
    }



    public static void writeLibrary(String operation, List<LibraryItem> items) {
        try (FileWriter writer = new FileWriter("Log.csv", true)) {

            writer.append("\nOperation: ").append(operation).append("\n");
            writer.append("Title,Author,PublicationYear,Status\n");

            for (LibraryItem item : items) {
                try {
                    writer.append(item.getTitle()).append(",")
                            .append(item.getAuthor()).append(",")
                            .append(String.valueOf(item.getPublicationYear())).append(",")
                            .append(String.valueOf(item.isAvailable()))
                            .append("\n");
                } catch (Exception e) {
                    System.err.println("Error writing item: " + item + " -> " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error writing to Log.csv: " + e.getMessage());
        }
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
                writeLibrary("change return time", items);
            }
        }else{
            System.out.println("Invalid date format");
        }
    }
    public static void getAddInputData(Scanner scanner , Library library) {
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
                library.addItem(new Book(title, author, year, available, genre, pages));
                break;
            case "Magazine":
                System.out.print("Please enter the publisher: ");
                String publisher = scanner.nextLine();
                System.out.print("Please enter the issue number: ");
                int issue =scanner.nextInt();
                scanner.nextLine();
                library.addItem(new Magazine(title, author, year, available, publisher, issue));
                break;
            case "ReferenceBook":
                System.out.print("Please enter the subject: ");
                String subject = scanner.nextLine();
                System.out.print("Please enter the edition: ");
                String edition = scanner.nextLine();
                library.addItem(new ReferenceBook(title, author, year, available, subject, edition));
                break;
            case "Thesis":
                System.out.print("Please enter the university: ");
                String university = scanner.nextLine();
                System.out.print("Please enter the supervisor: ");
                String supervisor = scanner.nextLine();
                library.addItem(new Thesis(title, author, year, available, university, supervisor));
                break;
            default:
                System.err.println("Unknown item type: " + type);
        }
    }

}
