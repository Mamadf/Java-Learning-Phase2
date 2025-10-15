package org.example.Storage;

import org.example.Repository.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryManagerService;
import org.example.proto.LibraryOuterClass;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtobufHandler {
    private final LibraryData library;
    private LibraryManagerService libraryManagerService;

    public ProtobufHandler(LibraryData library) {
        this.library = library;
        this.libraryManagerService = new LibraryManagerService(library);
    }

    public void saveToProto(String fileName) {
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            LibraryOuterClass.Library.Builder libraryBuilder = LibraryOuterClass.Library.newBuilder();

            for (LibraryItem item : library.getItems()) {
                LibraryOuterClass.LibraryItem.Builder itemBuilder = LibraryOuterClass.LibraryItem.newBuilder();

                if (item instanceof Book) {
                    Book b = (Book) item;
                    itemBuilder.setBook(LibraryOuterClass.Book.newBuilder()
                            .setId(b.getId())
                            .setTitle(b.getTitle())
                            .setAuthor(b.getAuthor())
                            .setPublicationYear(b.getPublicationYear())
                            .setAvailable(b.isAvailable())
                            .setGenre(b.getGenre())
                            .setPages(b.getPages())
                            .setReturnDate(b.getReturnTime() == null ? "" : b.getReturnTime()));
                } else if (item instanceof Magazine) {
                    Magazine m  = (Magazine) item;
                    itemBuilder.setMagazine(LibraryOuterClass.Magazine.newBuilder()
                            .setId(m.getId())
                            .setTitle(m.getTitle())
                            .setAuthor(m.getAuthor())
                            .setPublicationYear(m.getPublicationYear())
                            .setAvailable(m.isAvailable())
                            .setPublisher(m.getPublisher())
                            .setIssueNumber(m.getIssue())
                            .setReturnDate(m.getReturnTime() == null ? "" : m.getReturnTime()));
                } else if (item instanceof ReferenceBook) {
                    ReferenceBook r = (ReferenceBook) item;
                    itemBuilder.setReferenceBook(LibraryOuterClass.ReferenceBook.newBuilder()
                            .setId(r.getId())
                            .setTitle(r.getTitle())
                            .setAuthor(r.getAuthor())
                            .setPublicationYear(r.getPublicationYear())
                            .setAvailable(r.isAvailable())
                            .setSubject(r.getSubject())
                            .setEdition(r.getEdition())
                            .setReturnDate(r.getReturnTime() == null ? "" : r.getReturnTime()));
                } else if (item instanceof Thesis) {
                    Thesis t = (Thesis) item;
                    itemBuilder.setThesis(LibraryOuterClass.Thesis.newBuilder()
                            .setId(t.getId())
                            .setTitle(t.getTitle())
                            .setAuthor(t.getAuthor())
                            .setPublicationYear(t.getPublicationYear())
                            .setAvailable(t.isAvailable())
                            .setUniversity(t.getUniversity())
                            .setSupervisor(t.getSupervisor())
                            .setReturnDate(t.getReturnTime() == null ? "" : t.getReturnTime()));
                }

                libraryBuilder.addItems(itemBuilder);
            }

            libraryBuilder.build().writeTo(output);

        } catch (IOException e) {
            System.err.println("Failed to save library: " + e.getMessage());
        }
    }

    public void loadFromProto(String fileName) {
        try (FileInputStream input = new FileInputStream(fileName)) {
            LibraryOuterClass.Library protoLib = LibraryOuterClass.Library.parseFrom(input);
            for (LibraryOuterClass.LibraryItem item : protoLib.getItemsList()) {
                if (item.hasBook()) {
                    LibraryOuterClass.Book b = item.getBook();
                    libraryManagerService.addItem(new Book(b.getTitle(), b.getAuthor(), b.getPublicationYear(),
                            b.getAvailable(), b.getGenre(), b.getPages()));
                } else if (item.hasMagazine()) {
                    LibraryOuterClass.Magazine m = item.getMagazine();
                    libraryManagerService.addItem(new Magazine(m.getTitle(), m.getAuthor(), m.getPublicationYear(),
                            m.getAvailable(), m.getPublisher(), m.getIssueNumber()));
                } else if (item.hasReferenceBook()) {
                    LibraryOuterClass.ReferenceBook r = item.getReferenceBook();
                    libraryManagerService.addItem(new ReferenceBook(r.getTitle(), r.getAuthor(), r.getPublicationYear(),
                            r.getAvailable(), r.getSubject(), r.getEdition()));
                } else if (item.hasThesis()) {
                    LibraryOuterClass.Thesis t = item.getThesis();
                    libraryManagerService.addItem(new Thesis(t.getTitle(), t.getAuthor(), t.getPublicationYear(),
                            t.getAvailable(), t.getUniversity(), t.getSupervisor()));
                }
            }

        } catch (IOException e) {
            System.err.println("Could not load library: " + e.getMessage());
        }
    }
}
