package org.example;

public class Thesis extends LibraryItem{
    private String  university;
    private String supervisor;



    public String getUniversity() { return university; }
    public String getSupervisor() { return supervisor; }

    public Thesis(String title, String author, int publicationYear, boolean available, String university, String supervisor) {
        super(title, author, publicationYear, available);
        this.university = university;
        this.supervisor = supervisor;
    }

    @Override
    public void display() {
        System.out.println("ID: " + this.getId());
        System.out.println("Book: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("University: " + university + ", Supervisor: " + supervisor);
        System.out.println("------------------------");
    }
}
