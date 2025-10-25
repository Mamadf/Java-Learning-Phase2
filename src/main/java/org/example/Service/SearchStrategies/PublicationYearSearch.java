package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PublicationYearSearch implements SearchStrategy{
    private LibraryData libraryData;

    public PublicationYearSearch() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String year) {
        synchronized(libraryData) {
            Consumer<LibraryItem> displayItem = LibraryItem::display;
            int targetYear = Integer.parseInt(year);

            var result = libraryData.getItems().stream()
                    .filter(item -> item.getPublicationYear() == targetYear)
                    .peek(displayItem)
                    .toList();
            return result;
        }
    }
}
