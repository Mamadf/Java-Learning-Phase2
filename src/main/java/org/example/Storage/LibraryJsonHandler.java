package org.example.Storage;

import com.google.gson.*;
import org.example.Repository.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryManagerService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LibraryJsonHandler implements StorageHandler {
    private LibraryData library;
    private LibraryManagerService libraryManagerService;

    public LibraryJsonHandler(LibraryData library) {
        this.library = library;
        this.libraryManagerService = new LibraryManagerService(library);
    }

    @Override
    public void saveData(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            JsonArray jsonArray = new JsonArray();
            for (LibraryItem item : library.getItems()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", item.getClass().getSimpleName());
                obj.add("data", new Gson().toJsonTree(item));
                jsonArray.add(obj);
            }
            new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            library.getItems().clear();
            library.getItemById().clear();

            Gson gson = new Gson();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String type = obj.get("type").getAsString();
                JsonObject data = obj.getAsJsonObject("data");

                LibraryItem item;
                switch (type) {
                    case "Book":
                        item = gson.fromJson(data, Book.class);
                        break;
                    case "Magazine":
                        item = gson.fromJson(data, Magazine.class);
                        break;
                    case "ReferenceBook":
                        item = gson.fromJson(data, ReferenceBook.class);
                        break;
                    case "Thesis":
                        item = gson.fromJson(data, Thesis.class);
                        break;
                    default:
                        System.err.println("Unknown type: " + type);
                        continue;
                }

                libraryManagerService.addItem(item);
            }
            LibraryItem.setCounter(library.getItems().size()+1);
        } catch (FileNotFoundException e) {
            System.out.println("No existing library file found — starting new library.");
        } catch (IOException e) {
            System.err.println("Error reading library: " + e.getMessage());
        }
    }
}
