package org.example;

public class Magazine extends LibraryItem{
    private String publisher;
    private int issue;

    public Magazine(String title, String author, int publicationYear, String publisher, int issue) {
        super(title, author, publicationYear);
        this.publisher = publisher;
        this.issue = issue;
    }



    @Override
    public void display() {
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("issue number: " + issue + ", publisher: " + publisher);
    }
}
