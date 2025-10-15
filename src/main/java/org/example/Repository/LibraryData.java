package org.example.Repository;

import org.example.Model.LibraryItem;

import java.util.*;

public class LibraryData {
    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;

    public LibraryData() {
        items = new ArrayList<>();
        itemById = new HashMap<>();
    }

    public List<LibraryItem> getItems() {
        return items;
    }

    public Map<Integer, LibraryItem> getItemById() {
        return itemById;
    }

}
