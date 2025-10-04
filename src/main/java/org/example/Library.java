package org.example;

import org.example.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<LibraryItem> items;

    public Library() {
        items = new ArrayList<>();
    }

    public void addItem(LibraryItem item) {
        items.add(item);
    }

    public void printAll() {
        for (LibraryItem item : items) {
            item.display();
            System.out.println("--------------------");
        }
    }

    public void searchByTitle(String title) {
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.display();
            }
        }
    }

    public void searchByAuthor(String author) {
        for (LibraryItem item : items) {
            if (item.getAuthor().equalsIgnoreCase(author)) {
                item.display();
            }
        }
    }

    public void searchByYear(int year) {
        for (LibraryItem item : items) {
            if (item.getPublicationYear() == year) {
                item.display();
            }
        }
    }
}
