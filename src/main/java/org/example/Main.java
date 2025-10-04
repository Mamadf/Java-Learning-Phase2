package org.example;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        int id;
        String title , author;

        library.addItem(new Book("Java Basics", "John Smith", 2020 , true,  "Programming", 300));
        library.addItem(new Magazine("Science Today", "Mary Editor", 2023, false,"Nature Pub" , 12));
        library.addItem(new Thesis("AI in Healthcare", "Ali Reza", 2022, false, "Tehran Uni", "Dr. Karimi"));
        library.addItem(new Book("test", "Ali Reza", 2022, true,"Tehran Uni", 23));
        library.addItem(new Magazine("test", "Reza", 2018, true, "Tehran Uni", 1));
        library.addItem(new ReferenceBook("Oxford Dictionary", "Oxford Press", 2015,true , "Language", "13"));

//        library.printAll();

//        System.out.println("Search by author 'Ali Reza':");
//        library.searchByAuthor("Ali Reza");
//        library.searchByTitle("test");
//        library.printBorrowedItems();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "borrow":
                    System.out.println("Enter Book ID: ");
                    id = scanner.nextInt();
                    library.borrowItem(id);
                    scanner.nextLine();
                    break;
                case "return":
                    System.out.println("Enter Book ID: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    library.returnItem(id);
                    break;
                case "status":
                    library.printAll();
                    break;
                case "search by author":
                    System.out.println("Enter Author: ");
                    author = scanner.nextLine();
                    library.searchByAuthor(author);
                    break;
                case "search by title":
                    System.out.println("Enter title: ");
                    title = scanner.nextLine();
                    library.searchByTitle(title);
                    break;
                case "sort":
                    library.sortByPublicationYear();
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
