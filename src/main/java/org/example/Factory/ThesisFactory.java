package org.example.Factory;


import org.example.Model.Book;
import org.example.Model.ItemStatus;
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
        System.out.print("Enter the Status (EXIST/BORROWED/BANNED): ");
        ItemStatus status = ItemStatus.valueOf(scanner.nextLine());
        System.out.print("Enter university: ");
        String university = scanner.nextLine();
        System.out.print("Enter number of supervisor: ");
        String supervisor = scanner.nextLine();
        return new Thesis(title, author, year, status, university, supervisor);
    }

    @Override
    public void updateItem(LibraryItem libraryItem, Scanner scanner) {
        Thesis thesis = (Thesis) libraryItem;

        System.out.print("Enter new title (" + thesis.getTitle() + "): ");
        String title = scanner.nextLine();
        thesis.setTitle(title);

        System.out.print("Enter new author (" + thesis.getAuthor() + "): ");
        String author = scanner.nextLine();
        thesis.setAuthor(author);


        System.out.print("Enter new publication year (" + thesis.getPublicationYear() + "): ");
        int year = Integer.parseInt(scanner.nextLine());
        thesis.setPublicationYear(year);

        System.out.print("Enter new university (" + thesis.getUniversity() + "): ");
        String university = scanner.nextLine();
        thesis.setUniversity(university);

        System.out.print("Enter new supervisor (" + thesis.getSupervisor() + "): ");
        String supervisor = scanner.nextLine();
        thesis.setSupervisor(supervisor);

        System.out.println("✅ Thesis updated successfully!");
    }
}
