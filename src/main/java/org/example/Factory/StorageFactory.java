package org.example.Factory;

import org.example.Repository.LibraryData;
import org.example.Service.LibraryManagerService;
import org.example.Storage.*;

public class StorageFactory {

    public static StorageHandler createStorageHandler(String storageType, String storagePath, LibraryData libraryData) {
        if (storageType == null) {
            System.out.println("⚠️ storageType is null, defaulting to proto");
            storageType = "proto";
        }

        StorageHandler handler;
        switch (storageType) {
            case "json":
                handler = new LibraryJsonHandler(libraryData);
                break;
            case "proto":
                handler = new ProtobufHandler(libraryData);
                break;
            case "csv":
                handler = new CsvHandler(libraryData);
                break;
            default:
                throw new IllegalArgumentException("Unsupported storage type: " + storageType);
        }
        handler.loadData(storagePath);
        return handler;
    }
}
