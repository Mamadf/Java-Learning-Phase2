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
            List<LibraryItem> result = new ArrayList<>();
            for (var item : libraryData.getItems()) {
                if (item.getPublicationYear() == Integer.parseInt(year)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }
}
