package org.example.Config;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Factory.StorageFactory;
import org.example.Repository.LibraryData;
import org.example.Service.*;
import org.example.Storage.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class AppConfig {

    private final LibraryData libraryData;
    private final LibraryManagerService libraryManagerService;
    private final LibraryLoanService libraryLoanService;
    private final StorageHandler activeStorageHandler;
    private final String storageType;
    private final String storagePath;

    private static final Logger logger = Logger.getLogger(AppConfig.class.getName());

    public AppConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.warning("Could not find config.properties in resources folder.");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }


        this.storageType = props.getProperty("storage.type", "proto");
        this.storagePath = props.getProperty("storage.path", "library_data.bin");
        this.libraryData = LibraryData.getInstance();
        this.libraryManagerService = new LibraryManagerService(libraryData);
        this.libraryLoanService = new LibraryLoanService(libraryData);
        this.activeStorageHandler = StorageFactory.createStorageHandler(storageType, storagePath);
    }

    public void saveData(){
        activeStorageHandler.saveData(storagePath);
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
