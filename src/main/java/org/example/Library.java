package org.example;

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

    public void searchByTitle(String title) {
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.display();
            }
        }
    }
    public void searchByAuthor(String author) {
        for (LibraryItem item : items) {
            if (item.getAuthor().equalsIgnoreCase(author)) {
                item.display();
            }
        }
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
}
