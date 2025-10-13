package org.example.Service;

import org.example.Library.LibraryData;
import org.example.Model.LibraryItem;

import java.util.*;

public class LibraryManagerService {
    private final LibraryData libraryData;

    public LibraryManagerService(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    public void addItem(LibraryItem item) {
        libraryData.getItems().add(item);
        libraryData.getItemById().put(item.getId(), item);
    }

    public LibraryItem deleteItem(int id) {
        LibraryItem item = libraryData.getItemById().remove(id);
        if (item != null) {
            libraryData.getItems().remove(item);
        } else {
            System.out.println("Item not found.");
        }
        return item;
    }

    public LibraryItem searchById(int id) {
        return libraryData.getItemById().get(id);
    }

    public List<LibraryItem> searchByTitle(String title) {
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : libraryData.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.display();
                result.add(item);
            }
        }
        return result;
    }

    public List<LibraryItem> searchByAuthor(String author) {
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : libraryData.getItems()) {
            if (item.getAuthor().equalsIgnoreCase(author)) {
                item.display();
                result.add(item);
            }
        }
        return result;
    }

    public void sortByPublicationYear() {
        libraryData.getItems().sort(Comparator.comparingInt(LibraryItem::getPublicationYear));
    }

    public void printAll() {
        for (LibraryItem item : libraryData.getItems()) {
            item.display();
        }
    }
}
