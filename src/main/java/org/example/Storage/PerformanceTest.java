package org.example.Storage;

import org.example.Repository.LibraryData;
import org.example.Model.Book;
import org.example.Service.LibraryManagerService;

import java.io.File;

public class PerformanceTest {

    public static void main(String[] args) {

        LibraryData library = new LibraryData();
        LibraryManagerService libraryManager = new LibraryManagerService(library);
        for (int i = 0; i < 1000; i++) {
            libraryManager.addItem(new Book(
                    "Book " + i,
                    "Author " + (i % 100),
                    2000 + (i % 20),
                    true,
                    "Genre " + (i % 5),
                    200 + i
            ));
        }

        LibraryJsonHandler jsonHandler = new LibraryJsonHandler(library);
        ProtobufHandler protoHandler = new ProtobufHandler(library);

        long startJson = System.nanoTime();
        jsonHandler.saveToJson("benchmark.json");
        long endJson = System.nanoTime();
        long jsonTime = endJson - startJson;
        long jsonSize = new File("benchmark.json").length();

        long startProto = System.nanoTime();
        protoHandler.saveToProto("benchmark.bin");
        long endProto = System.nanoTime();
        long protoTime = endProto - startProto;
        long protoSize = new File("benchmark.bin").length();

        System.out.println("---- Performance Comparison ----");
        System.out.printf("JSON Save Time: %.3f ms%n", jsonTime / 1_000_000.0);
        System.out.printf("PROTO Save Time: %.3f ms%n", protoTime / 1_000_000.0);
        System.out.println("--------------------------------");
        System.out.println("JSON File Size: " + jsonSize + " bytes");
        System.out.println("PROTO File Size: " + protoSize + " bytes");

        long startJsonLoad = System.nanoTime();
        jsonHandler.loadFromJson("benchmark.json");
        long endJsonLoad = System.nanoTime();

        long startProtoLoad = System.nanoTime();
        protoHandler.loadFromProto("benchmark.bin");
        long endProtoLoad = System.nanoTime();

        System.out.printf("JSON Load Time: %.3f ms%n", (endJsonLoad - startJsonLoad) / 1_000_000.0);
        System.out.printf("PROTO Load Time: %.3f ms%n", (endProtoLoad - startProtoLoad) / 1_000_000.0);
    }
}
