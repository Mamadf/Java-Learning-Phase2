package org.example.Model;

public sealed abstract class LibraryItem permits Book, Thesis, ReferenceBook, Magazine {
    private static int counter = 1;
    private int id;
    private String returnTime;
    private String title;
    private String author;
    private int publicationYear;
    private ItemStatus status;


    public LibraryItem(String title, String author, int publicationYear , ItemStatus status) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = status;
    }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    public ItemStatus getStatus() {
        return status;
    }

    public int getPublicationYear() { return publicationYear; }
    public abstract void display();



    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public static void setCounter(int counter) {
        LibraryItem.counter = counter;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
    public void setId(int id) {
        this.id = id;
    }
}
