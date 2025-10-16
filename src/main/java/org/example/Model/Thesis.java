package org.example.Model;

public class Thesis extends LibraryItem {
    private String  university;
    private String supervisor;



    public String getUniversity() { return university; }
    public String getSupervisor() { return supervisor; }

    public Thesis(String title, String author, int publicationYear, ItemStatus status, String university, String supervisor) {
        super(title, author, publicationYear, status);
        this.university = university;
        this.supervisor = supervisor;
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId());
        System.out.println("Thesis: " + getTitle() + " by " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("University: " + university + ", Supervisor: " + supervisor);
        System.out.println("Status: " + getStatus() );
        System.out.println("Return Date: " + getReturnTime() );
        System.out.println("------------------------");
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
