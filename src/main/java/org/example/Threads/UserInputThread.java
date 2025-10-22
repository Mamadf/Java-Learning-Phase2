package org.example.Threads;

import org.example.Exception.GlobalExceptionHandler;
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
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter command: add, remove, borrow, return, update, get all assets , borrowed item, search by title, search by author, sort , help, exit");
            boolean running = true;
            while (running) {
                try {
                    String command = scanner.nextLine();
                    switch (command) {
                        case "add":
                            view.addItem(scanner);
                            break;
                        case "remove":
                            view.removeItem(scanner);
                            break;
                        case "update":
                            view.updateItem(scanner);
                            break;
                        case "get all assets":
                            view.printAll();
                            break;
                        case "borrow":
                            view.borrowItem(scanner, requestQueue);
                            break;
                        case "return":
                            view.returnItem(scanner, requestQueue);
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
                        case "search by status":
                            view.statusSearch(scanner);
                            break;
                        case "search by year":
                            view.yearSearch(scanner);
                            break;
                        case "help":
                            view.help();
                            break;
                        case "exit":
                            requestQueue.offer("exit");
                            running = false;
                            break;
                        default:
                            System.out.println("❌ Unknown command");
                    }
                }catch (Exception e){
                    GlobalExceptionHandler.handle(e);
                }
            }
        }catch (Exception e){
            GlobalExceptionHandler.handle(e);
        }
    }
}
