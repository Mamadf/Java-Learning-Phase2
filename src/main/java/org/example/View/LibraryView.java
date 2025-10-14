package org.example.View;

import org.example.Factory.LibraryItemFactory;
import org.example.Factory.LibraryItemFactoryProducer;
import org.example.Model.LibraryItem;
import org.example.Service.LibraryManagerService;
import org.example.Service.LibraryLoanService;
import org.example.Storage.CsvHandler;

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
        } else {
            System.out.println("❌ Unknown item type!");
        }
    }

    public void updateItem(Scanner scanner) {
        System.out.print("Enter item ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
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
        int id = Integer.parseInt(scanner.nextLine());
        LibraryItem removed = managerService.deleteItem(id);
        if (removed != null)
            CsvHandler.writeLibrary("remove", List.of(removed));
    }

    public void printAll() {
        managerService.printAll();
    }

    public void updateReturnTime(Scanner scanner) {
        System.out.print("Enter book ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter new return date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        if (!Pattern.matches("^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", date)) {
            System.out.println("❌ Invalid date format");
            return;
        }
        loanService.editReturnTime(id, date);
    }

    public void borrowItem(Scanner scanner, BlockingQueue<String> requestQueue) {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        requestQueue.offer(id + ":borrow");
    }

    public void returnItem(Scanner scanner, BlockingQueue<String> requestQueue) {
        System.out.print("Enter Book ID: ");
        String returnId = scanner.nextLine();
        requestQueue.offer(returnId + ":return");
    }
    public static synchronized void println(String msg) {
        System.out.println(msg);
    }
}
