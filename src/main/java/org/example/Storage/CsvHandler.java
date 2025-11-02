package org.example.Storage;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Model.*;
import org.example.Repository.LibraryData;
import org.example.Service.LibraryManagerService;

import java.io.*;
import java.util.List;

public class CsvHandler implements  StorageHandler {


    private final LibraryData library;
    private LibraryManagerService libraryManagerService;


    public CsvHandler() {
        this.library = LibraryData.getInstance();
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

                String[] values = line.split(",", -1);

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
                        case "Book"-> {
                            int pages = extra2.isEmpty() ? 0 : Integer.parseInt(extra2);
                            Book book = new Book( title, author, publicationYear, status, extra1, pages);
                            if (!returnDate.isEmpty()) book.setReturnTime(returnDate);
                            libraryManagerService.addItem(book);
                        }
                        case "Magazine"-> {
                            int issue = extra2.isEmpty() ? 0 : Integer.parseInt(extra2);
                            Magazine magazine = new Magazine( title, author, publicationYear, status, extra1, issue);
                            if (!returnDate.isEmpty()) magazine.setReturnTime(returnDate);
                            libraryManagerService.addItem(magazine);
                        }
                        case "ReferenceBook"-> {
                            ReferenceBook ref = new ReferenceBook( title, author, publicationYear, status, extra1, extra2);
                            if (!returnDate.isEmpty()) ref.setReturnTime(returnDate);
                            libraryManagerService.addItem(ref);
                        }
                        case "Thesis"-> {
                            Thesis thesis = new Thesis( title, author, publicationYear, status, extra1, extra2);
                            if (!returnDate.isEmpty()) thesis.setReturnTime(returnDate);
                            libraryManagerService.addItem(thesis);
                        }
                        default->
                            System.err.println("Unknown item type: " + type);
                    }

                } catch (Exception e) {
                    GlobalExceptionHandler.handle(e);
                }
            }

        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }


    @Override
    public void saveData(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("Type,Id,Title,Author,PublicationYear,Status,ReturnDate,Extra1,Extra2");
            bw.newLine();

            for (LibraryItem item : library.getItems()) {
                StringBuilder sb = new StringBuilder();

                if (item instanceof Book b) {
                    sb.append("Book").append(",");
                    sb.append(b.getId()).append(",");
                    sb.append(b.getTitle()).append(",");
                    sb.append(b.getAuthor()).append(",");
                    sb.append(b.getPublicationYear()).append(",");
                    sb.append(b.getStatus()).append(",");
                    sb.append(b.getReturnTime() != null ? b.getReturnTime() : "").append(",");
                    sb.append(b.getGenre()).append(",");
                    sb.append(b.getPages());

                } else if (item instanceof Magazine m) {
                    sb.append("Magazine").append(",");
                    sb.append(m.getId()).append(",");
                    sb.append(m.getTitle()).append(",");
                    sb.append(m.getAuthor()).append(",");
                    sb.append(m.getPublicationYear()).append(",");
                    sb.append(m.getStatus()).append(",");
                    sb.append(m.getReturnTime() != null ? m.getReturnTime() : "").append(",");
                    sb.append(m.getPublisher()).append(",");
                    sb.append(m.getIssue());

                } else if (item instanceof ReferenceBook r) {
                    sb.append("ReferenceBook").append(",");
                    sb.append(r.getId()).append(",");
                    sb.append(r.getTitle()).append(",");
                    sb.append(r.getAuthor()).append(",");
                    sb.append(r.getPublicationYear()).append(",");
                    sb.append(r.getStatus()).append(",");
                    sb.append(r.getReturnTime() != null ? r.getReturnTime() : "").append(",");
                    sb.append(r.getSubject()).append(",");
                    sb.append(r.getEdition());

                } else if (item instanceof Thesis t) {
                    sb.append("Thesis").append(",");
                    sb.append(t.getId()).append(",");
                    sb.append(t.getTitle()).append(",");
                    sb.append(t.getAuthor()).append(",");
                    sb.append(t.getPublicationYear()).append(",");
                    sb.append(t.getStatus()).append(",");
                    sb.append(t.getReturnTime() != null ? t.getReturnTime() : "").append(",");
                    sb.append(t.getUniversity()).append(",");
                    sb.append(t.getSupervisor());
                }

                bw.write(sb.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
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
                    GlobalExceptionHandler.handle(e);
                }
            }

        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }
}
