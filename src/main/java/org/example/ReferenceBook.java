package org.example;

public class ReferenceBook extends LibraryItem{
    private String subject;
    private String edition;

    public ReferenceBook(String title, String author, int publicationYear, String subject, String edition) {
        super(title, author, publicationYear);
        this.subject = subject;
        this.edition = edition;
    }

    @Override
    public void display() {
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Subject: " + subject + ", edition: " + edition);
    }
}
