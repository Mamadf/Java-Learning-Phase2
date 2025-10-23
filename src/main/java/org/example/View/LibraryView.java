package org.example.View;

import org.example.Factory.LibraryItemFactory;
import org.example.Factory.LibraryItemFactoryProducer;
import org.example.Model.ItemStatus;
import org.example.Model.LibraryItem;
import org.example.Service.LibraryManagerService;
import org.example.Service.LibraryLoanService;
import org.example.Service.SearchStrategies.AuthorSearchStrategy;
import org.example.Service.SearchStrategies.PublicationYearSearch;
import org.example.Service.SearchStrategies.StatusSearchStrategy;
import org.example.Service.SearchStrategies.TitleSearchStrategy;
import org.example.Storage.CsvHandler;
import org.example.utils.CheckValidation;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class LibraryView {
    private final LibraryManagerService managerService;
    private final LibraryLoanService loanService;


    public LibraryView(LibraryManagerService managerService, LibraryLoanService loanService) {
        this.managerService = managerService;
        this.loanService = loanService;
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
        var item = managerService.searchById(id);
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
        var removed = managerService.deleteItem(id);
        if (removed != null) {
            CsvHandler.writeLibrary("remove", List.of(removed));
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
        managerService.setSearchStrategy(new TitleSearchStrategy());
        var searchRes = managerService.search(title);
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search by title", searchRes);
        }else {
            System.out.println("❌ Title not found");
        }
    }
    public void authorSearch(Scanner scanner) {
        System.out.println("Enter Author: ");
        String author = CheckValidation.getNonEmptyString(scanner);
        managerService.setSearchStrategy(new AuthorSearchStrategy());
        var searchRes = managerService.search(author);
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search by author", searchRes);
        }else {
            System.out.println("❌ Author not found");
        }
    }


    public void yearSearch(Scanner scanner) {
        System.out.println("Enter Publication Year: ");
        int year = CheckValidation.getValidInt(scanner);
        managerService.setSearchStrategy(new PublicationYearSearch());
        var searchRes = managerService.search(String.valueOf(year));
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search by year", searchRes);
        }else {
            System.out.println("❌ There is no Item in this year");
        }
    }


    public void statusSearch(Scanner scanner) {
        boolean running = true;
        String itemStatus = null;
        while (running) {
            System.out.print("Enter item status (EXIST / BORROWED / BANNED): ");
            itemStatus = scanner.nextLine().trim().toUpperCase();
            if(CheckValidation.isValidItemStatus(itemStatus)){
                running = false;
            }else {
                System.out.println("❌ Invalid status! Please enter one of: EXIST, BORROWED, BANNED");

            }
        }
        managerService.setSearchStrategy(new StatusSearchStrategy());
        var searchRes = managerService.search(itemStatus);
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search by author", searchRes);
        }else {
            System.out.println("❌ There is no Item with this status");
        }
    }
    public void sort() {
        managerService.sortByPublicationYear();
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
                        "search by year   → Search items by publication year\n" +
                        "search by status → Search items by status(exist, borrowed, banned)\n" +
                        "sort             → Sort all items by publication year\n" +
                        "borrowed item    → Show all borrowed items\n" +
                        "help             → Show this help message\n" +
                        "exit             → Save data and exit the program\n" +
                        "---------------------------------\n" +
                        "Tip: Type command names exactly as shown above."
        );
    }

}
