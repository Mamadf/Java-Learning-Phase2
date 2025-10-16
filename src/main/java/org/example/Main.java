package org.example;

import org.example.View.LibraryView;
import org.example.Config.AppConfig;
import org.example.Threads.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args) {

        AppConfig config = new AppConfig();
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        LibraryView view = new LibraryView(
                config.getLibraryManagerService(),
                config.getLibraryLoanService()
        );

        Thread userThread = new UserInputThread(queue, view);
        Thread managerThread = new ManagerThread(queue, config.getLibraryLoanService(), config);

        userThread.start();
        managerThread.start();

    }
}
