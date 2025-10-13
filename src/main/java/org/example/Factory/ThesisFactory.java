package org.example.Factory;


import org.example.Model.LibraryItem;
import org.example.Model.Thesis;

import java.util.Scanner;

public class ThesisFactory implements LibraryItemFactory{

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
        System.out.print("Enter university: ");
        String university = scanner.nextLine();
        System.out.print("Enter number of supervisor: ");
        String supervisor = scanner.nextLine();
        return new Thesis(title, author, year, available, university, supervisor);
    }
}
