package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.List;

public interface SearchStrategy {
    List<LibraryItem> search(String query);
}
