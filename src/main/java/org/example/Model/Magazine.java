package org.example.Model;

public final class Magazine extends LibraryItem {
    private String publisher;
    private int issue;

    public Magazine(String title, String author, int publicationYear, ItemStatus status, String publisher, int issue) {
        super(title, author, publicationYear , status);
        this.publisher = publisher;
        this.issue = issue;
    }



    @Override
    public void display() {
        System.out.println("ID: " + getId());
        System.out.println("Magazine: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("issue number: " + issue + ", publisher: " + publisher);
        System.out.println("Status: " + getStatus() );
        System.out.println("Return Date: " + getReturnTime() );
        System.out.println("------------------------");
    }

    public String getPublisher() {
        return publisher;
    }

    public int getIssue() {
        return issue;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }
}
