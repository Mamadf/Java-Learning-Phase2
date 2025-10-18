package org.example.Threads;

import org.example.View.LibraryView;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class UserInputThread extends Thread {
    private final BlockingQueue<String> requestQueue;
    private final LibraryView view;

    public UserInputThread(BlockingQueue<String> requestQueue, LibraryView view) {
        this.requestQueue = requestQueue;
        this.view = view;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command: add, remove, borrow, return, update, get all assets , borrowed item, search by title, search by author, sort , help, exit");
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "add" :
                    view.addItem(scanner);
                    break;
                case "remove" :
                    view.removeItem(scanner);
                    break;
                case "update" :
                    view.updateItem(scanner);
                    break;
                case "get all assets" :
                    view.printAll();
                    break;
                case "borrow" :
                    view.borrowItem(scanner , requestQueue);
                    break;
                case "return" :
                    view.returnItem(scanner , requestQueue);
                    break;
                case "return time":
                    view.updateReturnTime(scanner);
                    break;
                case "search by author":
                    view.authorSearch(scanner);
                    break;
                case "search by title":
                    view.titleSearch(scanner);
                    break;
                case "sort":
                    view.sort();
                    break;
                case "borrowed item":
                    view.showBorrowedItems();
                    break;
                case "help" :
                    view.help();
                    break;
                case "exit" :
                    requestQueue.offer("exit");
                    running = false;
                    break;
                default :
                    System.out.println("❌ Unknown command");
            }
        }
    }
}
