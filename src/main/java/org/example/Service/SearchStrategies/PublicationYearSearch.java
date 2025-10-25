package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;

public class PublicationYearSearch implements SearchStrategy{
    private LibraryData libraryData;

    public PublicationYearSearch() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String year) {
        synchronized(libraryData) {
            int targetYear = Integer.parseInt(year);

            var result = libraryData.getItems().stream()
                    .filter(item -> item.getPublicationYear() == targetYear)
                    .peek(LibraryItem::display)
                    .toList();
            return result;
        }
    }
}
