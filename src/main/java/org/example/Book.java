package org.example;

public class Book extends LibraryItem {
    private String genre;
    private int pages;

    public Book(String title, String author, int publicationYear, String genre, int pages) {
        super(title, author, publicationYear);
        this.genre = genre;
        this.pages = pages;
    }
    public String getGenre() { return genre; }
    public int getPages() { return pages; }

    @Override
    public void display() {
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Genre: " + genre + ", Pages: " + pages);
    }
}

