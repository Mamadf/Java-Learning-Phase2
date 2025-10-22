package org.example.Service;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;
import org.example.Service.SearchStrategies.SearchStrategy;

import java.util.*;

public class LibraryManagerService {
    private final LibraryData libraryData;
    private SearchStrategy searchStrategy;


    public LibraryManagerService(LibraryData libraryData) {
        this.libraryData = libraryData;
    }


    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public List<LibraryItem> search(String query) {
        try{
            return searchStrategy.search(query);
        }catch (NullPointerException e){
            GlobalExceptionHandler.handle(e);
            return null;
        }
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
