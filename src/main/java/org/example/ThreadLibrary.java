package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ThreadLibrary {
    public static void main(String[] args) {
        Library library = new Library();
        LibraryLoanService loanService = new LibraryLoanService(library);
        ProtobufHandler protobufHandler = new ProtobufHandler(library);
        protobufHandler.loadFromProto("library_data.bin");
        List<String> requestQueue = Collections.synchronizedList(new ArrayList<>());

        Thread userThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            boolean flag = true;
            while (flag) {
                System.out.println("Enter request (format: bookId:lend, bookId:return) or 'exit':");
                String request = scanner.nextLine();
                synchronized (requestQueue) {
                    requestQueue.add(request);
                    requestQueue.notify();
                }
                if (request.equalsIgnoreCase("exit")) {
                    flag = false;
                    protobufHandler.saveToProto("library_data.bin");
                }
            }
        });
        Thread managerThread = new Thread(() -> {
            while (true) {
                String request = null;
                synchronized (requestQueue) {
                    while (requestQueue.isEmpty()) {
                        try {
                            requestQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    request = requestQueue.remove(0);
                }

                if (request.equalsIgnoreCase("exit")) break;

                try {
                    String[] parts = request.split(":");
                    int bookId = Integer.parseInt(parts[0]);
                    String action = parts[1];

                    switch (action) {
                        case "lend" :
                            loanService.borrowItem(bookId);
                            break;
                        case "return" :
                            loanService.returnItem(bookId);
                            break;
                        default :
                            System.out.println("Unknown action: " + action);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid request format.");
                }
            }
        });

        userThread.start();
        managerThread.start();

        try {
            userThread.join();
            managerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
