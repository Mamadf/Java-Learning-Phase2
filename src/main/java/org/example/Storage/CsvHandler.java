package org.example.Storage;

import org.example.Model.*;
import org.example.Service.LibraryManagerService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvHandler {

    public void loadData(LibraryManagerService libraryManagerService) {
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
                    ItemStatus status = ItemStatus.valueOf(values[4]);
                    String privateField = values[5];

                    switch (type) {
                        case "Book":
                            int pages = Integer.parseInt(values[6]);
                            libraryManagerService.addItem(new Book(title, author, year, status, privateField, pages));
                            break;
                        case "Magazine":
                            int issue = Integer.parseInt(values[6]);
                            libraryManagerService.addItem(new Magazine(title, author, year, status, privateField, issue));
                            break;
                        case "ReferenceBook":
                            String edition = values[6];
                            libraryManagerService.addItem(new ReferenceBook(title, author, year, status, privateField, edition));
                            break;
                        case "Thesis":
                            String supervisor = values[6];
                            libraryManagerService.addItem(new Thesis(title, author, year, status, privateField, supervisor));
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
    public void writeLibrary(String operation, List<LibraryItem> items) {
        try (FileWriter writer = new FileWriter("Log.csv", true)) {

            writer.append("\nOperation: ").append(operation).append("\n");
            writer.append("Title,Author,PublicationYear,Status\n");

            for (LibraryItem item : items) {
                try {
                    writer.append(item.getTitle()).append(",")
                            .append(item.getAuthor()).append(",")
                            .append(String.valueOf(item.getPublicationYear())).append(",")
                            .append(String.valueOf(item.getStatus()))
                            .append("\n");
                } catch (Exception e) {
                    System.err.println("Error writing item: " + item + " -> " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error writing to Log.csv: " + e.getMessage());
        }
    }
}
