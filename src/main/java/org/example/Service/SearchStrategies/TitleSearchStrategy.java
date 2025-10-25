package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TitleSearchStrategy implements SearchStrategy{
    private LibraryData libraryData;

    public TitleSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String title) {
        synchronized(libraryData) {
            Consumer<LibraryItem> displayItem = LibraryItem::display;
            var results = libraryData.getItems().stream()
                    .filter(item -> item.getTitle().equalsIgnoreCase(title))
                    .peek(displayItem)
                    .toList();
            return results;
        }
    }
}
