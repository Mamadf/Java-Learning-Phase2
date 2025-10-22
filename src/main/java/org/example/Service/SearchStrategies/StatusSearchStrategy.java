package org.example.Service.SearchStrategies;

import org.example.Model.ItemStatus;
import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;

public class StatusSearchStrategy implements SearchStrategy {
    LibraryData libraryData;
    public StatusSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }


    @Override
    public List<LibraryItem> search(String query) {
        synchronized(libraryData) {
            List<LibraryItem> result = new ArrayList<>();
            for (LibraryItem item : libraryData.getItems()) {
                if (item.getStatus() == ItemStatus.valueOf(query)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }
}
