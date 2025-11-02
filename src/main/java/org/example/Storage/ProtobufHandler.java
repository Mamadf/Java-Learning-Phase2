package org.example.Storage;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Repository.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryManagerService;
import org.example.proto.LibraryOuterClass;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtobufHandler implements StorageHandler {
    private final LibraryData library;
    private LibraryManagerService libraryManagerService;

    public ProtobufHandler() {
        this.library = LibraryData.getInstance();
        this.libraryManagerService = new LibraryManagerService(library);
    }

    @Override
    public void saveData(String fileName) {
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            LibraryOuterClass.Library.Builder libraryBuilder = LibraryOuterClass.Library.newBuilder();

            for (LibraryItem item : library.getItems()) {
                LibraryOuterClass.LibraryItem.Builder itemBuilder = LibraryOuterClass.LibraryItem.newBuilder();

                if (item instanceof Book b) {
                    itemBuilder.setBook(LibraryOuterClass.Book.newBuilder()
                            .setId(b.getId())
                            .setTitle(b.getTitle())
                            .setAuthor(b.getAuthor())
                            .setPublicationYear(b.getPublicationYear())
                            .setStatus(LibraryOuterClass.ItemStatus.valueOf(b.getStatus().name()))
                            .setGenre(b.getGenre())
                            .setPages(b.getPages())
                            .setReturnDate(b.getReturnTime() == null ? "" : b.getReturnTime()));
                } else if (item instanceof Magazine m) {
                    itemBuilder.setMagazine(LibraryOuterClass.Magazine.newBuilder()
                            .setId(m.getId())
                            .setTitle(m.getTitle())
                            .setAuthor(m.getAuthor())
                            .setPublicationYear(m.getPublicationYear())
                            .setStatus(LibraryOuterClass.ItemStatus.valueOf(m.getStatus().name()))
                            .setPublisher(m.getPublisher())
                            .setIssueNumber(m.getIssue())
                            .setReturnDate(m.getReturnTime() == null ? "" : m.getReturnTime()));
                } else if (item instanceof ReferenceBook r) {
                    itemBuilder.setReferenceBook(LibraryOuterClass.ReferenceBook.newBuilder()
                            .setId(r.getId())
                            .setTitle(r.getTitle())
                            .setAuthor(r.getAuthor())
                            .setPublicationYear(r.getPublicationYear())
                            .setStatus(LibraryOuterClass.ItemStatus.valueOf(r.getStatus().name()))
                            .setSubject(r.getSubject())
                            .setEdition(r.getEdition())
                            .setReturnDate(r.getReturnTime() == null ? "" : r.getReturnTime()));
                } else if (item instanceof Thesis t) {
                    itemBuilder.setThesis(LibraryOuterClass.Thesis.newBuilder()
                            .setId(t.getId())
                            .setTitle(t.getTitle())
                            .setAuthor(t.getAuthor())
                            .setPublicationYear(t.getPublicationYear())
                            .setStatus(LibraryOuterClass.ItemStatus.valueOf(t.getStatus().name()))
                            .setUniversity(t.getUniversity())
                            .setSupervisor(t.getSupervisor())
                            .setReturnDate(t.getReturnTime() == null ? "" : t.getReturnTime()));
                }

                libraryBuilder.addItems(itemBuilder);
            }

            libraryBuilder.build().writeTo(output);

        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    @Override
    public void loadData(String fileName) {
        try (FileInputStream input = new FileInputStream(fileName)) {
            LibraryOuterClass.Library protoLib = LibraryOuterClass.Library.parseFrom(input);
            for (LibraryOuterClass.LibraryItem item : protoLib.getItemsList()) {
                if (item.hasBook()) {
                    LibraryOuterClass.Book b = item.getBook();
                    Book book = new Book(b.getTitle(), b.getAuthor(), b.getPublicationYear(),
                            ItemStatus.valueOf(b.getStatus().name()), b.getGenre(), b.getPages());
                    libraryManagerService.addItem(book);
                    if (!b.getReturnDate().isEmpty()) book.setReturnTime(b.getReturnDate());

                } else if (item.hasMagazine()) {
                    LibraryOuterClass.Magazine m = item.getMagazine();
                    Magazine magazine = new Magazine(m.getTitle(), m.getAuthor(), m.getPublicationYear(),
                            ItemStatus.valueOf(m.getStatus().name()), m.getPublisher(), m.getIssueNumber());
                    libraryManagerService.addItem(magazine);
                    if (!m.getReturnDate().isEmpty()) magazine.setReturnTime(m.getReturnDate());
                } else if (item.hasReferenceBook()) {
                    LibraryOuterClass.ReferenceBook r = item.getReferenceBook();
                    ReferenceBook referenceBook = new ReferenceBook(r.getTitle(), r.getAuthor(), r.getPublicationYear(),
                            ItemStatus.valueOf(r.getStatus().name()), r.getSubject(), r.getEdition());
                    libraryManagerService.addItem(referenceBook);
                    if (!r.getReturnDate().isEmpty()) referenceBook.setReturnTime(r.getReturnDate());
                } else if (item.hasThesis()) {
                    LibraryOuterClass.Thesis t = item.getThesis();
                    Thesis thesis = new Thesis(t.getTitle(), t.getAuthor(), t.getPublicationYear(),
                            ItemStatus.valueOf(t.getStatus().name()), t.getUniversity(), t.getSupervisor());
                    libraryManagerService.addItem(thesis);
                    if (!t.getReturnDate().isEmpty()) thesis.setReturnTime(t.getReturnDate());
                }
            }

        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }
}
