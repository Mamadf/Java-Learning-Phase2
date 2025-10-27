package org.example.Service.SearchStrategies;

import org.example.Model.LibraryItem;
import org.example.Repository.LibraryData;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ContainsAtLeastOneKeyStrategy implements SearchStrategy {

    @Override
    public List<LibraryItem> search(String query) {
        Consumer<LibraryItem> displayItem = LibraryItem::display;
        LibraryData libraryData = LibraryData.getInstance();
        String[] fields = query.split(",");
        String titleQuery = fields[0].toLowerCase();
        String authorQuery = fields[1].toLowerCase();
        int yearQuery = Integer.parseInt(fields[2]);

        return libraryData.getItems().stream()
                .filter(item ->
                        item.getTitle().toLowerCase().contains(titleQuery) ||
                                item.getAuthor().toLowerCase().contains(authorQuery) ||
                                item.getPublicationYear() == yearQuery
                )
                .peek(displayItem)
                .collect(Collectors.toList());
    }
}
