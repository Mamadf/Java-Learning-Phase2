package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AuthorSearchStrategy implements SearchStrategy {
    LibraryData libraryData;

    public AuthorSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String author) {
        synchronized (libraryData) {
            Consumer<LibraryItem> displayItem = LibraryItem::display;
            var results = libraryData.getItems().stream()
                    .filter(item -> item.getAuthor().equalsIgnoreCase(author))
                    .peek(displayItem)
                    .toList();
            return results;
        }
    }
}
