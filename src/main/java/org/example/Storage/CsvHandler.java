package org.example.Storage;

import org.example.Model.*;
import org.example.Repository.LibraryData;
import org.example.Service.LibraryManagerService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvHandler implements  StorageHandler {


    private final LibraryData library;
    private LibraryManagerService libraryManagerService;


    public CsvHandler(LibraryData library) {
        this.library = library;
        this.libraryManagerService = new LibraryManagerService(library);
    }

    @Override
    public void loadData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",", -1); // keep empty cells

                if (values.length < 8) {
                    System.err.println("Invalid line: " + line);
                    continue;
                }

                try {
                    String type = values[0].trim();
                    int id = Integer.parseInt(values[1].trim());
                    String title = values[2].trim();
                    String author = values[3].trim();
                    int publicationYear = Integer.parseInt(values[4].trim());
                    ItemStatus status = ItemStatus.valueOf(values[5].trim());
                    String returnDate = values[6].trim();
                    String extra1 = values[7].trim();
                    String extra2 = values.length > 8 ? values[8].trim() : "";

                    switch (type) {
                        case "Book": {
                            int pages = extra2.isEmpty() ? 0 : Integer.parseInt(extra2);
                            Book book = new Book( title, author, publicationYear, status, extra1, pages);
                            if (!returnDate.isEmpty()) book.setReturnTime(returnDate);
                            libraryManagerService.addItem(book);
                            break;
                        }
                        case "Magazine": {
                            int issue = extra2.isEmpty() ? 0 : Integer.parseInt(extra2);
                            Magazine magazine = new Magazine( title, author, publicationYear, status, extra1, issue);
                            if (!returnDate.isEmpty()) magazine.setReturnTime(returnDate);
                            libraryManagerService.addItem(magazine);
                            break;
                        }
                        case "ReferenceBook": {
                            ReferenceBook ref = new ReferenceBook( title, author, publicationYear, status, extra1, extra2);
                            if (!returnDate.isEmpty()) ref.setReturnTime(returnDate);
                            libraryManagerService.addItem(ref);
                            break;
                        }
                        case "Thesis": {
                            Thesis thesis = new Thesis( title, author, publicationYear, status, extra1, extra2);
                            if (!returnDate.isEmpty()) thesis.setReturnTime(returnDate);
                            libraryManagerService.addItem(thesis);
                            break;
                        }
                        default:
                            System.err.println("Unknown item type: " + type);
                    }

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Could not read file: " + filename);
        }
    }


    @Override
    public void saveData(String fileName) {
        if (library == null) {
            System.out.println("❌ Library data is null, cannot save CSV.");
            return;
        }

        List<LibraryItem> items = library.getItems();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Type,Id,Title,Author,PublicationYear,Status,Extra1,Extra2,ReturnDate\n");

            for (LibraryItem item : items) {
                if (item instanceof Book) {
                    Book b = (Book) item;
                    writer.append("Book,")
                            .append(String.valueOf(b.getId())).append(",")
                            .append(b.getTitle()).append(",")
                            .append(b.getAuthor()).append(",")
                            .append(String.valueOf(b.getPublicationYear())).append(",")
                            .append(b.getStatus().name()).append(",")
                            .append(b.getGenre()).append(",")
                            .append(String.valueOf(b.getPages())).append(",")
                            .append(b.getReturnTime() == null ? "" : b.getReturnTime())
                            .append("\n");

                } else if (item instanceof Magazine) {
                    Magazine m = (Magazine) item;
                    writer.append("Magazine,")
                            .append(String.valueOf(m.getId())).append(",")
                            .append(m.getTitle()).append(",")
                            .append(m.getAuthor()).append(",")
                            .append(String.valueOf(m.getPublicationYear())).append(",")
                            .append(m.getStatus().name()).append(",")
                            .append(m.getPublisher()).append(",")
                            .append(String.valueOf(m.getIssue())).append(",")
                            .append(m.getReturnTime() == null ? "" : m.getReturnTime())
                            .append("\n");

                } else if (item instanceof ReferenceBook) {
                    ReferenceBook r = (ReferenceBook) item;
                    writer.append("ReferenceBook,")
                            .append(String.valueOf(r.getId())).append(",")
                            .append(r.getTitle()).append(",")
                            .append(r.getAuthor()).append(",")
                            .append(String.valueOf(r.getPublicationYear())).append(",")
                            .append(r.getStatus().name()).append(",")
                            .append(r.getSubject()).append(",")
                            .append(r.getEdition()).append(",")
                            .append(r.getReturnTime() == null ? "" : r.getReturnTime())
                            .append("\n");

                } else if (item instanceof Thesis) {
                    Thesis t = (Thesis) item;
                    writer.append("Thesis,")
                            .append(String.valueOf(t.getId())).append(",")
                            .append(t.getTitle()).append(",")
                            .append(t.getAuthor()).append(",")
                            .append(String.valueOf(t.getPublicationYear())).append(",")
                            .append(t.getStatus().name()).append(",")
                            .append(t.getReturnTime() == null ? "" : t.getReturnTime()).append(",")
                            .append(t.getUniversity()).append(",")
                            .append(t.getSupervisor())
                            .append("\n");
                }
            }

            System.out.println("✅ Data successfully saved to CSV file: " + fileName);

        } catch (IOException e) {
            System.err.println("❌ Failed to save CSV: " + e.getMessage());
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
