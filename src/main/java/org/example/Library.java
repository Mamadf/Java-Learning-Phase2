package org.example;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;

    public Library() {
        items = new ArrayList<>();
        itemById = new HashMap<>();
    }


    public void addItem(LibraryItem item) {
        items.add(item);
        itemById.put(item.getId(), item);
    }


    public void deleteItem(int id) {
        LibraryItem item = itemById.remove(id);
        if (item != null) {
            items.remove(item);
            System.out.println("Item with id " + id + " removed successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }
    public LibraryItem searchById(int id) {
        return itemById.get(id);
    }

    public List<LibraryItem> searchByTitle(String title) {
        List <LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.display();
                result.add(item);
            }
        }
        return result;
    }
    public List<LibraryItem> searchByAuthor(String author) {
        List <LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getAuthor().equalsIgnoreCase(author)) {
                item.display();
                result.add(item);
            }
        }
        return result;
    }
    public void sortByPublicationYear() {
        items.sort(Comparator.comparingInt(LibraryItem::getPublicationYear));
    }

    public void printAll() {
        for (LibraryItem item : items) {
            item.display();
        }
    }

    public void printBorrowedItems() {
        for (LibraryItem item : items) {
            if (!item.isAvailable()) {
                item.display();
            }
        }
    }
    public LibraryItem borrowItem(int id) {
        LibraryItem item = itemById.get(id);
        if (item != null) {
            if (item.isAvailable()) {
                item.setAvailable(false);
                System.out.println("Item '" + item.getTitle() + "' borrowed successfully.");
            } else {
                System.out.println("Item is already borrowed.");
                return null;
            }
        } else {
            System.out.println("Item not found.");
            return null;
        }
        return item;
    }

    public LibraryItem returnItem(int id) {
        LibraryItem item = itemById.get(id);
        if (item != null) {
            if (!item.isAvailable()) {
                item.setAvailable(true);
                item.setReturnTime(LocalDate.now());
                System.out.println("Item '" + item.getTitle() + "' returned successfully.");
            } else {
                System.out.println("Item was not borrowed.");
                return null;
            }
        } else {
            System.out.println("Item not found.");
            return null;
        }
        return item;
    }

    public LibraryItem editReturnTime(int id , String date) {
        LibraryItem item = itemById.get(id);
        if (item != null) {
            if (item.isAvailable()) {
                item.setReturnTime(LocalDate.parse(date));
                System.out.println("Item '" + item.getTitle() + "' return time has been set successfully.");
            } else {
                System.out.println("Item is borrowed.");
                return null;
            }
        } else {
            System.out.println("Item not found.");
            return null;
        }
        return item;
    }
}
