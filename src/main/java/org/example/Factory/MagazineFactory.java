package org.example.Factory;

import org.example.Model.Book;
import org.example.Model.ItemStatus;
import org.example.Model.LibraryItem;
import org.example.Model.Magazine;
import org.example.utils.CheckValidation;

import java.util.Scanner;

public class MagazineFactory implements LibraryItemFactory{

    @Override
    public LibraryItem createItem(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = CheckValidation.getNonEmptyString(scanner);
        System.out.print("Enter author: ");
        String author = CheckValidation.getNonEmptyString(scanner);
        System.out.print("Enter publication year: ");
        int year = CheckValidation.getValidInt(scanner);
        ItemStatus status = ItemStatus.EXIST;
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter number of issue: ");
        int issue = CheckValidation.getValidInt(scanner);
        return new Magazine(title, author, year, status, publisher, issue);
    }

    @Override
    public void updateItem(LibraryItem libraryItem, Scanner scanner) {
        Magazine magazine = (Magazine) libraryItem;

        System.out.print("Enter new title (" + magazine.getTitle() + "): ");
        String title = CheckValidation.getNonEmptyString(scanner);
        magazine.setTitle(title);

        System.out.print("Enter new author (" + magazine.getAuthor() + "): ");
        String author = CheckValidation.getNonEmptyString(scanner);
        magazine.setAuthor(author);


        System.out.print("Enter new publication year (" + magazine.getPublicationYear() + "): ");
        int year = CheckValidation.getValidInt(scanner);
        magazine.setPublicationYear(year);

        System.out.print("Enter new publisher (" + magazine.getPublisher() + "): ");
        String publisher = scanner.nextLine();
        magazine.setPublisher(publisher);

        System.out.print("Enter new issue (" + magazine.getIssue() + "): ");
        int issue = CheckValidation.getValidInt(scanner);
        magazine.setIssue(issue);

        System.out.println("✅ Magazine updated successfully!");
    }
}
