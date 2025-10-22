package org.example.Factory;


import org.example.Storage.*;

public class StorageFactory {

    public static StorageHandler createStorageHandler(String storageType, String storagePath) {
        if (storageType == null) {
            System.out.println("⚠️ storageType is null, defaulting to proto");
            storageType = "proto";
        }

        StorageHandler handler;
        switch (storageType) {
            case "json":
                handler = new LibraryJsonHandler();
                break;
            case "proto":
                handler = new ProtobufHandler();
                break;
            case "csv":
                handler = new CsvHandler();
                break;
            default:
                throw new IllegalArgumentException("Unsupported storage type: " + storageType);
        }
        handler.loadData(storagePath);
        return handler;
    }
}
