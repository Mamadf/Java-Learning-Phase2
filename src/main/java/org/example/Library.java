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
    public List<LibraryItem> getItems() {
        return items;
    }

    public Map<Integer, LibraryItem> getItemById() {
        return itemById;
    }

}
