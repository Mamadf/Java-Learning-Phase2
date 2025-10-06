package org.example;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        int id;
        List<LibraryItem> items;
        String title , author;
        loadData(library);



        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "borrow":
                    System.out.println("Enter Book ID: ");
                    id = scanner.nextInt();
                    LibraryItem item = library.borrowItem(id);
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
                    LibraryItem returnItem = library.returnItem(id);
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
                    writeLibrary("search by author", library.searchByAuthor(author));
                    break;
                case "search by title":
                    System.out.println("Enter title: ");
                    title = scanner.nextLine();
                    writeLibrary("search by title", library.searchByTitle(title));
                    break;
                case "sort":
                    library.sortByPublicationYear();
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
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
                        library.addItem(new ReferenceBook(title , author , year, available, privateField, edition));
                        break;
                    case "Thesis":
                        String supervisor = values[6];
                        library.addItem(new Thesis(title, author, year, available, privateField, supervisor));
                        break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeLibrary(String operation, List<LibraryItem> items) {
        try (FileWriter writer = new FileWriter("Log.csv" , true)) {
            writer.append("\nOperation: ").append(operation).append("\n");

            writer.append("Title,Author,PublicationYear,Status\n");
            for (LibraryItem item : items) {
                writer.append(item.getTitle()).append(",")
                        .append(item.getAuthor()).append(",")
                        .append(String.valueOf(item.getPublicationYear())).append(",")
                        .append(String.valueOf(item.isAvailable())).append(",")
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
