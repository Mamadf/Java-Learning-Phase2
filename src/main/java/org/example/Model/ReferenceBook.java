package org.example.Model;

public class ReferenceBook extends LibraryItem {
    private String subject;
    private String edition;

    public ReferenceBook(String title, String author, int publicationYear , boolean available, String subject, String edition) {
        super(title, author, publicationYear , available);
        this.subject = subject;
        this.edition = edition;
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId());
        System.out.println("ReferenceBook: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Subject: " + subject + ", edition: " + edition);
        System.out.println("Availability: " + isAvailable() );
        System.out.println("Return Date: " + getReturnTime() );
        System.out.println("------------------------");
    }

    public String getSubject() {
        return subject;
    }

    public String getEdition() {
        return edition;
    }
}
