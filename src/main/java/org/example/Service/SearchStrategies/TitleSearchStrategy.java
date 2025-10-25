package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;

public class TitleSearchStrategy implements SearchStrategy{
    private LibraryData libraryData;

    public TitleSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String title) {
        synchronized(libraryData) {
            var results = libraryData.getItems().stream()
                    .filter(item -> item.getTitle().equalsIgnoreCase(title))
                    .peek(LibraryItem::display)
                    .toList();
            return results;
        }
    }
}
