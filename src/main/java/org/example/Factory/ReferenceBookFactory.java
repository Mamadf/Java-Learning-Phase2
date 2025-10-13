package org.example.Factory;


import org.example.Model.LibraryItem;
import org.example.Model.ReferenceBook;

import java.util.Scanner;

public class ReferenceBookFactory implements LibraryItemFactory{

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
        System.out.print("Enter subject: ");
        String subject = scanner.nextLine();
        System.out.print("Enter number of edition: ");
        String edition = scanner.nextLine();
        return new ReferenceBook(title, author, year, available, subject, edition);
    }
}
