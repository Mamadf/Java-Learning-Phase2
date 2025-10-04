package org.example;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;

public abstract class LibraryItem {
    private static int counter = 1;
    private int id;
    private LocalDate returnTime;
    private String title;
    private String author;
    private int publicationYear;
    private boolean available;


    public LibraryItem(String title, String author, int publicationYear , boolean available) {
        this.id = counter++;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.available = available;
    }


    public int getId() { return id; }
    public boolean isAvailable() { return available; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
    public abstract void display();

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDate getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDate returnTime) {
        this.returnTime = returnTime;
    }
}
