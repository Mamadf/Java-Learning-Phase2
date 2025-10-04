package org.example;

public class Magazine extends LibraryItem{
    private String publisher;
    private int issue;

    public Magazine(String title, String author, int publicationYear, boolean available, String publisher, int issue) {
        super(title, author, publicationYear , available);
        this.publisher = publisher;
        this.issue = issue;
    }



    @Override
    public void display() {
        System.out.println("ID: " + getId());
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("issue number: " + issue + ", publisher: " + publisher);
        System.out.println("Availability: " + isAvailable() );
        System.out.println("Return Date: " + getReturnTime() );
        System.out.println("------------------------");
    }
}
