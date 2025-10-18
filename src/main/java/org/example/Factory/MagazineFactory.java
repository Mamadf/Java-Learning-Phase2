package org.example.Factory;

import org.example.Model.Book;
import org.example.Model.ItemStatus;
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
        ItemStatus status = ItemStatus.EXIST;
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter number of issue: ");
        int issue = Integer.parseInt(scanner.nextLine());
        return new Magazine(title, author, year, status, publisher, issue);
    }

    @Override
    public void updateItem(LibraryItem libraryItem, Scanner scanner) {
        Magazine magazine = (Magazine) libraryItem;

        System.out.print("Enter new title (" + magazine.getTitle() + "): ");
        String title = scanner.nextLine();
        magazine.setTitle(title);

        System.out.print("Enter new author (" + magazine.getAuthor() + "): ");
        String author = scanner.nextLine();
        magazine.setAuthor(author);


        System.out.print("Enter new publication year (" + magazine.getPublicationYear() + "): ");
        int year = Integer.parseInt(scanner.nextLine());
        magazine.setPublicationYear(year);

        System.out.print("Enter new publisher (" + magazine.getPublisher() + "): ");
        String publisher = scanner.nextLine();
        magazine.setPublisher(publisher);

        System.out.print("Enter new issue (" + magazine.getIssue() + "): ");
        String issue = scanner.nextLine();
        magazine.setIssue(Integer.parseInt(issue));

        System.out.println("✅ Magazine updated successfully!");
    }
}
