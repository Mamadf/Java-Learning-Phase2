package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.ArrayList;
import java.util.List;

public class AuthorSearchStrategy implements SearchStrategy {
    LibraryData libraryData;

    public AuthorSearchStrategy() {
        this.libraryData = LibraryData.getInstance();
    }

    @Override
    public List<LibraryItem> search(String author) {
        synchronized (libraryData) {
            List<LibraryItem> result = new ArrayList<>();
            for (LibraryItem item : libraryData.getItems()) {
                if (item.getAuthor().equalsIgnoreCase(author)) {
                    item.display();
                    result.add(item);
                }
            }
            return result;
        }
    }
}
