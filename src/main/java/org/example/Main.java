package org.example;

import org.example.View.LibraryView;
import org.example.Repository.LibraryData;
import org.example.Service.*;
import org.example.Storage.ProtobufHandler;
import org.example.Threads.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        LibraryData library = new LibraryData();
        LibraryManagerService managerService = new LibraryManagerService(library);
        LibraryLoanService loanService = new LibraryLoanService(library);

        ProtobufHandler protobufHandler = new ProtobufHandler(library);
        protobufHandler.loadFromProto("library_data.bin");

//        LibraryJsonHandler libraryJsonHandler = new LibraryJsonHandler(library);
//        libraryJsonHandler.loadFromJson("test.json"); //Load from Json file

//        CsvHandler csvHandler = new CsvHandler();
//        csvHandler.loadData(libraryManagerService); //Load from CSV file


        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        LibraryView view = new LibraryView(managerService, loanService);

        Thread userThread = new UserInputThread(queue, view);
        Thread managerThread = new ManagerThread(queue, loanService, protobufHandler);

        userThread.start();
        managerThread.start();

    }
}
