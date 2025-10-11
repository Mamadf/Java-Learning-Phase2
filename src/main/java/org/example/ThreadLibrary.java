package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class ThreadLibrary {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, LibraryItem> libraryMap = new ConcurrentHashMap<>();
        Library library = new Library();
        LibraryLoanService loanService = new LibraryLoanService(library);

        ProtobufHandler protobufHandler = new ProtobufHandler(library);
        protobufHandler.loadFromProto("library_data.bin");
        BlockingQueue<String> requestQueue = new LinkedBlockingQueue<>();

        Thread userThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running) {
                System.out.println("Enter command: add, remove, borrow, return, status, exit");
                String command = scanner.nextLine();
                switch (command) {
                    case "add" :
                        addToLibrary(scanner ,library);
                        break;
                    case "remove":
                        removeFromLibrary(scanner ,library);
                        break;
                    case "borrow":
                        System.out.print("Enter Book ID: ");
                        int num = Integer.parseInt(scanner.nextLine());
                        requestQueue.offer(num + ":" + command);
                        break;
                    case "return":
                        System.out.print("Enter Book ID: ");
                        int returnId = Integer.parseInt(scanner.nextLine());
                        requestQueue.offer(returnId + ":" + command);
                        break;
                    case "status":
                        library.printAll();
                        break;
                    case "exit" :
                        requestQueue.offer("exit");
                        running = false;
                    default :
                        System.out.println("Unknown command");
                }
            }
        });

        Thread managerThread = new Thread(() -> {
            while (true) {
                try {
                    String request = requestQueue.take();
                    if (request.equals("exit")) {
                        protobufHandler.saveToProto("library_data.bin");
                        break;
                    }
                    String[] parts = request.split(":");
                    int bookId = Integer.parseInt(parts[0]);
                    String action = parts[1];

                    switch (action) {
                        case "borrow" :
                            loanService.borrowItem(bookId);
                            break;
                        case "return" :
                            loanService.returnItem(bookId);
                            break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        userThread.start();
        managerThread.start();

        try {
            userThread.join();
            managerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addToLibrary(Scanner scanner , Library library) {
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
    private static void removeFromLibrary(Scanner scanner, Library library) {
        System.out.print("Please enter the Item's ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        LibraryItem item = library.deleteItem(id);
        List<LibraryItem> items = new ArrayList<>();
        items.add(item);
        writeLibrary("remove" , items);
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
}
