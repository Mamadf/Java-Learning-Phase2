package org.example;

public class Book extends LibraryItem {
    private String genre;
    private int pages;

    public Book(String title, String author, int publicationYear, boolean available , String genre, int pages) {
        super(title, author, publicationYear, available);
        this.genre = genre;
        this.pages = pages;
    }
    public String getGenre() { return genre; }
    public int getPages() { return pages; }

    @Override
    public void display() {
        System.out.println("ID: " + this.getId());
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Genre: " + genre + ", Pages: " + pages);
        System.out.println("------------------------");
    }
}

