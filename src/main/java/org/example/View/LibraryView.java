package org.example.View;

import org.example.Factory.LibraryItemFactory;
import org.example.Factory.LibraryItemFactoryProducer;
import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;
import org.example.Repository.OperationRepository;
import org.example.Service.LibraryManagerService;
import org.example.Service.LibraryLoanService;
import org.example.Storage.CsvHandler;
import org.example.utils.CheckValidation;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

public class LibraryView {
    private final LibraryManagerService managerService;
    private final LibraryLoanService loanService;
    private OperationRepository operationRepository;

    public LibraryView(LibraryManagerService managerService, LibraryLoanService loanService) {
        this.managerService = managerService;
        this.loanService = loanService;
        operationRepository = new OperationRepository();
    }

    public void addItem(Scanner scanner) {
        System.out.print("Enter item type (Book, Magazine, ReferenceBook, Thesis): ");
        String type = scanner.nextLine();
        LibraryItemFactory factory = LibraryItemFactoryProducer.getFactory(type);
        if (factory != null) {
            LibraryItem item = factory.createItem(scanner);
            operationRepository.add(item);
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
        if (factory != null){
            factory.updateItem(item, scanner);
            operationRepository.update(item);
        }

    }

    public void removeItem(Scanner scanner) {
        System.out.print("Enter item ID: ");
        int id = CheckValidation.getValidInt(scanner);
        var removed = managerService.deleteItem(id);
        if (removed != null) {
            CsvHandler.writeLibrary("remove", List.of(removed));
            operationRepository.delete(removed);
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

    public void sort() {
        managerService.sortByPublicationYear();
    }


    public void help() {
        System.out.println(
                "Available commands:\n" +
                        "---------------------------------\n" +
                        "add                    → Add a new item to the library\n" +
                        "remove                 → Remove an existing item by ID\n" +
                        "update                 → Update the details of an existing item\n" +
                        "get all assets         → Display all items in the library\n" +
                        "get all assets --short → Display summary of items in the library\n" +
                        "borrow                 → Borrow an item by ID\n" +
                        "return                 → Return a borrowed item\n" +
                        "return time            → Update the return time for a existing item\n" +
                        "search items           → Search items by title, author and publication year(choose strategy in config file)\n" +
                        "sort                   → Sort all items by publication year\n" +
                        "borrowed item          → Show all borrowed items\n" +
                        "help                   → Show this help message\n" +
                        "exit                   → Save data and exit the program\n" +
                        "---------------------------------\n" +
                        "Tip: Type command names exactly as shown above."
        );
    }

    public void showTitleAndAuthor() {
        BiConsumer<LibraryItem, StringBuilder> titleAuthorPrinter = (item, sb) ->
                sb.append("Title: ").append(item.getTitle())
                        .append(", Author: ").append(item.getAuthor())
                        .append("\n");

        StringBuilder output = new StringBuilder();
        LibraryData.getInstance().getItems().stream()
                .forEach(item -> {titleAuthorPrinter.accept(item, output);});
        System.out.print(output.toString());
    }

    public void searchItems(Scanner scanner) {
        System.out.println("Enter the query, Your input should be: <title>,<author>,<publicationYear>");
        String query = CheckValidation.getValidQuery(scanner);
        var searchRes = managerService.search(query);
        if(!searchRes.isEmpty()) {
            CsvHandler.writeLibrary("search ", searchRes);
        }else {
            System.out.println("❌ There is no Item with this information!");
        }
    }
}
