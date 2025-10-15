package org.example.Config;

import org.example.Repository.LibraryData;
import org.example.Service.*;
import org.example.Storage.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final LibraryData libraryData;
    private final LibraryManagerService libraryManagerService;
    private final LibraryLoanService libraryLoanService;
    private final Object activeStorageHandler;
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
            System.out.println("Could not load config.properties: " + e.getMessage());
        }


        this.storageType = props.getProperty("storage.type", "proto");
        this.storagePath = props.getProperty("storage.path", "library_data.bin");
        this.libraryData = new LibraryData();
        this.libraryManagerService = new LibraryManagerService(libraryData);
        this.libraryLoanService = new LibraryLoanService(libraryData);

        switch (storageType) {
            case "json" :
                LibraryJsonHandler jsonHandler = new LibraryJsonHandler(libraryData);
                jsonHandler.loadFromJson(storagePath);
                activeStorageHandler = jsonHandler;
                break;
            case "csv" :
                CsvHandler csvHandler = new CsvHandler();
                csvHandler.loadData(libraryManagerService);
                activeStorageHandler = csvHandler;
                break;
            default :
                ProtobufHandler protoHandler = new ProtobufHandler(libraryData);
                protoHandler.loadFromProto(storagePath);
                activeStorageHandler = protoHandler;
        }
    }
    public void saveData(){
        switch(storageType){
            case "json" :
                LibraryJsonHandler jsonHandler = new LibraryJsonHandler(libraryData);
                jsonHandler.saveToJson(storagePath);
                break;
            case "proto":
                ProtobufHandler protoHandler = new ProtobufHandler(libraryData);
                protoHandler.saveToProto(storagePath);
                break;
            default:
                System.out.println("Unsupported storage type: " + storageType);
        }
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
