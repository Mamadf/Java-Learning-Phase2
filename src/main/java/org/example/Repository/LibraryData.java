package org.example.Repository;

import org.example.Model.LibraryItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryData {
    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;

    public LibraryData() {
        items = new ArrayList<>();
        itemById = new ConcurrentHashMap<>();
    }

    public List<LibraryItem> getItems() {
        return items;
    }

    public Map<Integer, LibraryItem> getItemById() {
        return itemById;
    }

}
