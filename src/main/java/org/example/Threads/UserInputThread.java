package org.example.Threads;

import org.example.View.LibraryView;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class UserInputThread extends Thread {
    private final BlockingQueue<String> requestQueue;
    private final LibraryView controller;

    public UserInputThread(BlockingQueue<String> requestQueue, LibraryView controller) {
        this.requestQueue = requestQueue;
        this.controller = controller;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command: commands, add, remove, borrow, return, update, status, exit");
        boolean running = true;
        while (running) {
            String command = scanner.nextLine();
            switch (command) {
                case "add" :
                    controller.addItem(scanner);
                    break;
                case "remove" :
                    controller.removeItem(scanner);
                    break;
                case "update" :
                    controller.updateItem(scanner);
                    break;
                case "status" :
                    controller.printAll();
                    break;
                case "borrow" :
                    controller.borrowItem(scanner , requestQueue);
                    break;
                case "return" :
                    controller.returnItem(scanner , requestQueue);
                    break;
                case "commands" :
                    System.out.println("Enter command: commands, add, remove, borrow, return, update, status, exit");
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
