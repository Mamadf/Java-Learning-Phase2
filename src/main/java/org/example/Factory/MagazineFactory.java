package org.example.Factory;

import org.example.Model.LibraryItem;
import org.example.Model.Magazine;

import java.util.Scanner;

public class MagazineFactory implements LibraryItemFactory{

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
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter number of issue: ");
        int issue = Integer.parseInt(scanner.nextLine());
        return new Magazine(title, author, year, available, publisher, issue);
    }
}
