package org.example;

import org.example.Exception.GlobalExceptionHandler;
import org.example.View.LibraryView;
import org.example.Config.AppConfig;
import org.example.Threads.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Unhandled exception in thread " + t.getName());
            GlobalExceptionHandler.handle(e);
        });
        AppConfig config = new AppConfig();
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        LibraryView view = new LibraryView(
                config.getLibraryManagerService(),
                config.getLibraryLoanService()
        );


        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(new UserInputThread(queue, view));
        executor.execute(new ManagerThread(queue, config.getLibraryLoanService(), config));

        executor.shutdown();
    }
}
