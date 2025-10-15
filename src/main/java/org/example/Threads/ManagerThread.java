package org.example.Threads;

import org.example.Config.AppConfig;
import org.example.Service.LibraryLoanService;
import org.example.Storage.ProtobufHandler;

import java.util.concurrent.BlockingQueue;

public class ManagerThread extends Thread {
    private final BlockingQueue<String> requestQueue;
    private final LibraryLoanService loanService;
    private final AppConfig appConfig;

    public ManagerThread(BlockingQueue<String> requestQueue, LibraryLoanService loanService , AppConfig appConfig) {
        this.requestQueue = requestQueue;
        this.loanService = loanService;
        this.appConfig = appConfig;
    }

    @Override
    public void run() {
        try {
            boolean running = true;
            while (running) {
                String request = requestQueue.take();
                if (request.equals("exit")) {
//                    protobufHandler.saveToProto("library_data.bin");
                    appConfig.saveData();
                    running = false;
                    continue;
                }
                String[] parts = request.split(":");
                int id = Integer.parseInt(parts[0]);
                String action = parts[1];

                switch (action) {
                    case "borrow" :
                        loanService.borrowItem(id);
                        break;
                    case "return" :
                        loanService.returnItem(id);
                        break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
