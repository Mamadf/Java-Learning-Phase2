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
            List<LibraryItem> result = new ArrayList<>();
            for (var item : libraryData.getItems()) {
                if (item.getTitle().equalsIgnoreCase(title)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }
}
