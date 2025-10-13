package org.example.Factory;

import org.example.Model.Book;
import org.example.Model.LibraryItem;

import java.util.Scanner;

public class BookFactory implements LibraryItemFactory{

    @Override
    public LibraryItem createItem(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Is it available? (true/false): ");
        boolean available = Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter number of pages: ");
        int pages = Integer.parseInt(scanner.nextLine());
        return new Book(title, author, year, available, genre, pages);
    }
}
