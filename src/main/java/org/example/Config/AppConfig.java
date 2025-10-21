package org.example.Config;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Factory.StorageFactory;
import org.example.Repository.LibraryData;
import org.example.Service.*;
import org.example.Storage.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final LibraryData libraryData;
    private final LibraryManagerService libraryManagerService;
    private final LibraryLoanService libraryLoanService;
    private final StorageHandler activeStorageHandler;
    private final String storageType;
    private final String storagePath;

    public AppConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Could not find config.properties in resources folder.");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }


        this.storageType = props.getProperty("storage.type", "proto");
        this.storagePath = props.getProperty("storage.path", "library_data.bin");
        this.libraryData = new LibraryData();
        this.libraryManagerService = new LibraryManagerService(libraryData);
        this.libraryLoanService = new LibraryLoanService(libraryData);
        this.activeStorageHandler = StorageFactory.createStorageHandler(storageType, storagePath, libraryData);
    }

    public void saveData(){
        activeStorageHandler.saveData(storagePath);
    }

    public LibraryData getLibraryData() {
        return libraryData;
    }

    public LibraryManagerService getLibraryManagerService() {
        return libraryManagerService;
    }

    public LibraryLoanService getLibraryLoanService() {
        return libraryLoanService;
    }

    public Object getActiveStorageHandler() {
        return activeStorageHandler;
    }

    public String getStorageType() {
        return storageType;
    }

    public String getStoragePath() {
        return storagePath;
    }
}
