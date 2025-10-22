package org.example.Service;

import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;

import java.util.*;

public class LibraryManagerService {
    private final LibraryData libraryData;

    public LibraryManagerService(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    public void addItem(LibraryItem item) {
        synchronized (libraryData) {
            libraryData.getItems().add(item);
            libraryData.getItemById().put(item.getId(), item);
        }
    }

    public LibraryItem deleteItem(int id) {
        synchronized (libraryData) {
            LibraryItem item = libraryData.getItemById().remove(id);
            if (item != null) {
                libraryData.getItems().remove(item);
            } else {
                System.out.println("Item not found.");
            }
            return item;
        }
    }

    public LibraryItem searchById(int id) {
        synchronized (libraryData) {
            return libraryData.getItemById().get(id);
        }
    }

    public List<LibraryItem> searchByTitle(String title) {
        synchronized(libraryData) {
            List<LibraryItem> result = new ArrayList<>();
            for (LibraryItem item : libraryData.getItems()) {
                if (item.getTitle().equalsIgnoreCase(title)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }

    public  List<LibraryItem> searchByAuthor(String author) {
        synchronized (libraryData) {
            List<LibraryItem> result = new ArrayList<>();
            for (LibraryItem item : libraryData.getItems()) {
                if (item.getAuthor().equalsIgnoreCase(author)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }

    public void sortByPublicationYear() {
        synchronized (libraryData) {
            libraryData.getItems().sort(Comparator.comparingInt(LibraryItem::getPublicationYear));
        }
    }

    public void printAll() {
        synchronized (libraryData) {
            for (LibraryItem item : libraryData.getItems()) {
                item.display();
            }
        }
    }
}
