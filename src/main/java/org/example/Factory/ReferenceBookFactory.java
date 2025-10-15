package org.example.Factory;


import org.example.Model.LibraryItem;
import org.example.Model.ReferenceBook;
import org.example.Model.Thesis;

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

    @Override
    public void updateItem(LibraryItem libraryItem, Scanner scanner) {
        ReferenceBook referenceBook = (ReferenceBook) libraryItem;

        System.out.print("Enter new title (" + referenceBook.getTitle() + "): ");
        String title = scanner.nextLine();
        referenceBook.setTitle(title);

        System.out.print("Enter new author (" + referenceBook.getAuthor() + "): ");
        String author = scanner.nextLine();
        referenceBook.setAuthor(author);


        System.out.print("Enter new publication year (" + referenceBook.getPublicationYear() + "): ");
        int year = Integer.parseInt(scanner.nextLine());
        referenceBook.setPublicationYear(year);

        System.out.print("Enter new subject (" + referenceBook.getSubject() + "): ");
        String subject = scanner.nextLine();
        referenceBook.setSubject(subject);

        System.out.print("Enter new edition (" + referenceBook.getEdition() + "): ");
        String edition = scanner.nextLine();
        referenceBook.setEdition(edition);

        System.out.println("✅ ReferenceBook updated successfully!");
    }
}
