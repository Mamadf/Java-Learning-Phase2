package org.example.Service;

import org.example.Model.ItemStatus;
import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LibraryLoanService {
    private LibraryData library;

    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;
    public LibraryLoanService(LibraryData library) {
        this.library = library;
        items = library.getItems();
        itemById = library.getItemById();
    }
    public synchronized void printBorrowedItems() {
        for (LibraryItem item : items) {
            if (item.getStatus() == ItemStatus.BORROWED) {
                item.display();
            }
        }
    }
    public LibraryItem borrowItem(int id) {
        synchronized (library) {
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.EXIST) {
                    item.setStatus(ItemStatus.BORROWED);
                    System.out.println("Item '" + item.getTitle() + "' borrowed successfully.");
                } else if (item.getStatus() == ItemStatus.BORROWED) {
                    System.out.println("Item is already borrowed.");
                    return null;
                } else {
                    System.out.println("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }

    public LibraryItem returnItem(int id) {
        synchronized (library) {
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.BORROWED) {
                    item.setStatus(ItemStatus.EXIST);
                    item.setReturnTime(LocalDate.now().toString());
                    System.out.println("Item '" + item.getTitle() + "' returned successfully.");
                } else if (item.getStatus() == ItemStatus.EXIST) {
                    System.out.println("Item was not borrowed.");
                    return null;
                } else {
                    System.out.println("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }

    public LibraryItem editReturnTime(int id , String date) {
        synchronized (library) {
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.EXIST) {
                    item.setReturnTime(date);
                    System.out.println("Item '" + item.getTitle() + "' return time has been set successfully.");
                } else if (item.getStatus() == ItemStatus.BORROWED) {
                    System.out.println("Item is borrowed, you can't change return time");
                    return null;
                } else {
                    System.out.println("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }
}
