package org.example.Threads;

import org.example.Factory.LibraryItemFactory;
import org.example.Factory.LibraryItemFactoryProducer;
import org.example.Library.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryLoanService;
import org.example.Service.LibraryManagerService;
import org.example.Storage.CsvHandler;
import org.example.Storage.ProtobufHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class ThreadLibrary {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, LibraryItem> libraryMap = new ConcurrentHashMap<>();
        LibraryData library = new LibraryData();
        LibraryManagerService libraryManagerService = new LibraryManagerService(library);
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
                        addToLibrary(scanner ,libraryManagerService);
                        break;
                    case "remove":
                        removeFromLibrary(scanner ,libraryManagerService);
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
                    case "update":
                        updateLibrary(scanner , libraryManagerService);
                        break;
                    case "status":
                        libraryManagerService.printAll();
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

    private static void updateLibrary(Scanner scanner, LibraryManagerService libraryManagerService) {
        System.out.print("Enter item ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        LibraryItem item = libraryManagerService.searchById(id);

        if (item != null) {
            String type = item.getClass().getSimpleName();
            LibraryItemFactory factory = LibraryItemFactoryProducer.getFactory(type);
            factory.updateItem(item, scanner);
        } else {
            System.out.println("❌ Item not found!");
        }


    }

    public static void addToLibrary(Scanner scanner , LibraryManagerService libraryManagerService) {
        System.out.print("Enter item type (Book, Magazine, ReferenceBook, Thesis): ");
        String type = scanner.nextLine();
        LibraryItemFactory factory = LibraryItemFactoryProducer.getFactory(type);
        if (factory != null) {
            LibraryItem item = factory.createItem(scanner);
            libraryManagerService.addItem(item);
        } else {
            System.out.println("Unknown item type: " + type);
        }
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
}
