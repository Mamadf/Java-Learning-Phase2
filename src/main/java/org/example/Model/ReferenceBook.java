package org.example.Model;

public final class ReferenceBook extends LibraryItem {
    private String subject;
    private String edition;

    public ReferenceBook(String title, String author, int publicationYear , ItemStatus status, String subject, String edition) {
        super(title, author, publicationYear , status);
        this.subject = subject;
        this.edition = edition;
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId());
        System.out.println("ReferenceBook: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Subject: " + subject + ", edition: " + edition);
        System.out.println("Status: " + getStatus() );
        System.out.println("Return Date: " + getReturnTime() );
        System.out.println("------------------------");
    }

    public String getSubject() {
        return subject;
    }

    public String getEdition() {
        return edition;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}
