package org.example.Factory;

import org.example.Model.Book;
import org.example.Model.ItemStatus;
import org.example.Model.LibraryItem;
import org.example.utils.CheckValidation;

import java.util.Scanner;

public class BookFactory implements LibraryItemFactory{

    @Override
    public LibraryItem createItem(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = CheckValidation.getNonEmptyString(scanner);
        System.out.print("Enter author: ");
        String author = CheckValidation.getNonEmptyString(scanner);
        System.out.print("Enter publication year: ");
        int year = CheckValidation.getValidInt(scanner);
        ItemStatus status = ItemStatus.EXIST;
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter number of pages: ");
        int pages = CheckValidation.getValidInt(scanner);
        return new Book(title, author, year, status, genre, pages);
    }

    @Override
    public void updateItem(LibraryItem libraryItem, Scanner scanner) {
        Book book = (Book) libraryItem;

        System.out.print("Enter new title (" + book.getTitle() + "): ");
        String title = CheckValidation.getNonEmptyString(scanner);
        book.setTitle(title);

        System.out.print("Enter new author (" + book.getAuthor() + "): ");
        String author = CheckValidation.getNonEmptyString(scanner);
        book.setAuthor(author);


        System.out.print("Enter new publication year (" + book.getPublicationYear() + "): ");
        int year = CheckValidation.getValidInt(scanner);
        book.setPublicationYear(year);

        System.out.print("Enter new genre (" + book.getGenre() + "): ");
        String genre = scanner.nextLine();
        book.setGenre(genre);

        System.out.print("Enter new page count (" + book.getPages() + "): ");
        int page =  CheckValidation.getValidInt(scanner);
        book.setPages(page);

        System.out.println("✅ Book updated successfully!");
    }
}
