package org.example.Service.SearchStrategies;

import org.example.Model.ItemStatus;
import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StatusSearchStrategy implements SearchStrategy {
    LibraryData libraryData;
    public StatusSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }


    @Override
    public List<LibraryItem> search(String query) {
        synchronized(libraryData) {
            Consumer<LibraryItem> displayItem = LibraryItem::display;
            var results = libraryData.getItems().stream()
                    .filter(item -> item.getStatus().equals(ItemStatus.valueOf(query)))
                    .peek(displayItem)
                    .toList();
            return results;
        }
    }
}
