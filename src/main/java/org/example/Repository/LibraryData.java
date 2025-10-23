package org.example.Repository;

import org.example.Model.LibraryItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LibraryData {
    private static LibraryData libraryData;
    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;

    public LibraryData() {
        items = new ArrayList<>();
        itemById = new ConcurrentHashMap<>();
    }

    public static LibraryData getInstance() {
        if (libraryData == null) {
            synchronized (LibraryData.class) {
                if (libraryData == null) {
                    libraryData = new LibraryData();
                }
            }
        }
        return libraryData;
    }
    public List<LibraryItem> getItems() {
        return items;
    }

    public Map<Integer, LibraryItem> getItemById() {
        return itemById;
    }

}
