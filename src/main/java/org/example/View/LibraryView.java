package org.example.View;

import org.example.Factory.LibraryItemFactory;
import org.example.Factory.LibraryItemFactoryProducer;
import org.example.Model.LibraryItem;
import org.example.Service.LibraryManagerService;
import org.example.Service.LibraryLoanService;
import org.example.Storage.CsvHandler;
import org.example.utils.CheckValidation;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class LibraryView {
    private final LibraryManagerService managerService;
    private final LibraryLoanService loanService;
    private CsvHandler csvHandler;

    public LibraryView(LibraryManagerService managerService, LibraryLoanService loanService) {
        this.managerService = managerService;
        this.loanService = loanService;
        csvHandler = new CsvHandler();
    }

    public void addItem(Scanner scanner) {
        System.out.print("Enter item type (Book, Magazine, ReferenceBook, Thesis): ");
        String type = scanner.nextLine();
        LibraryItemFactory factory = LibraryItemFactoryProducer.getFactory(type);
        if (factory != null) {
            LibraryItem item = factory.createItem(scanner);
            managerService.addItem(item);
            System.out.println("✅ Item added successfully!");
        } else {
            System.out.println("❌ Unknown item type!");
        }
    }

    public void updateItem(Scanner scanner) {
        System.out.print("Enter item ID to update: ");
        int id = CheckValidation.getValidInt(scanner);
        LibraryItem item = managerService.searchById(id);
        if (item == null) {
            System.out.println("❌ Item not found!");
            return;
        }
        LibraryItemFactory factory = LibraryItemFactoryProducer.getFactory(item.getClass().getSimpleName());
        if (factory != null) factory.updateItem(item, scanner);
    }

    public void removeItem(Scanner scanner) {
        System.out.print("Enter item ID: ");
        int id = CheckValidation.getValidInt(scanner);
        LibraryItem removed = managerService.deleteItem(id);
        if (removed != null) {
            csvHandler.writeLibrary("remove", List.of(removed));
            System.out.println("✅ Item removed successfully!");
        }
    }

    public void printAll() {
        managerService.printAll();
    }

    public void updateReturnTime(Scanner scanner) {
        System.out.print("Enter Item ID: ");
        int id = CheckValidation.getValidInt(scanner);
        System.out.print("Enter new return date (yyyy-MM-dd): ");
        String date = CheckValidation.getValidDate(scanner);
        loanService.editReturnTime(id, date);
    }

    public void borrowItem(Scanner scanner, BlockingQueue<String> requestQueue) {
        System.out.print("Enter Item ID: ");
        int id = CheckValidation.getValidInt(scanner);
        requestQueue.offer(id + ":borrow");
    }

    public void returnItem(Scanner scanner, BlockingQueue<String> requestQueue) {
        System.out.print("Enter Item ID: ");
        int id = CheckValidation.getValidInt(scanner);
        requestQueue.offer(id + ":return");
    }

    public void titleSearch(Scanner scanner) {
        System.out.println("Enter title: ");
        String title = CheckValidation.getNonEmptyString(scanner);
        List<LibraryItem> searchRes = managerService.searchByTitle(title);
        if(!searchRes.isEmpty()) {
            csvHandler.writeLibrary("search by title", searchRes);
        }else {
            System.out.println("❌ Title not found");
        }
    }
    public void authorSearch(Scanner scanner) {
        System.out.println("Enter Author: ");
        String author = CheckValidation.getNonEmptyString(scanner);
        List<LibraryItem> searchResult = managerService.searchByAuthor(author);
        if(!searchResult.isEmpty()) {
            csvHandler.writeLibrary("search by author", searchResult);
        }else {
            System.out.println("❌ Author not found");
        }
    }

    public void sort() {
        managerService.sortByPublicationYear();
    }

    public void showBorrowedItems() {
        loanService.printBorrowedItems();
    }

    public void help() {
        System.out.println(
                "Available commands:\n" +
                        "---------------------------------\n" +
                        "add              → Add a new item to the library\n" +
                        "remove           → Remove an existing item by ID\n" +
                        "update           → Update the details of an existing item\n" +
                        "get all assets   → Display all items in the library\n" +
                        "borrow           → Borrow an item by ID\n" +
                        "return           → Return a borrowed item\n" +
                        "return time      → Update the return time for a existing item\n" +
                        "search by title  → Search items by title\n" +
                        "search by author → Search items by author\n" +
                        "sort             → Sort all items by publication year\n" +
                        "borrowed item    → Show all borrowed items\n" +
                        "help             → Show this help message\n" +
                        "exit             → Save data and exit the program\n" +
                        "---------------------------------\n" +
                        "Tip: Type command names exactly as shown above."
        );
    }
}
