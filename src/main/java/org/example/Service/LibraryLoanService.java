package org.example.Service;

import org.example.Model.ItemStatus;
import org.example.Observer.Notifier;
import org.example.Observer.User;
import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class LibraryLoanService {
    private static final Logger logger = Logger.getLogger(LibraryLoanService.class.getName());

    private static final Predicate<LibraryItem> isBorrowable =
            item -> item != null && item.getStatus() == ItemStatus.EXIST;
    private static final Predicate<LibraryItem> isBorrowed =
            item -> item != null && item.getStatus() == ItemStatus.BORROWED;
    private LibraryData library;

    private List<LibraryItem> items;
    private Map<Integer, LibraryItem> itemById;
    public LibraryLoanService(LibraryData library) {
        this.library = library;
        items = library.getItems();
        itemById = library.getItemById();
    }

    public LibraryItem borrowItem(int id) {
        synchronized (library) {
            var item = itemById.get(id);
            if (item != null) {
                if (isBorrowable.test(item)) {
                    item.setStatus(ItemStatus.BORROWED);
                    logger.info("Item '" + item.getTitle() + "' borrowed successfully.");
                } else if (isBorrowed.test(item)) {
                    logger.warning("Item is already borrowed.");
                    return null;
                } else {
                    logger.warning("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }

    public LibraryItem returnItem(int id) {
        synchronized (library) {
            var item = itemById.get(id);
            if (item != null) {
                if (isBorrowed.test(item)) {
                    item.setStatus(ItemStatus.EXIST);
                    item.setReturnTime(LocalDate.now().toString());
                    Notifier notifier = new Notifier();
                    notifier.notifyObservers("Item '" + item.getTitle() + "' returned successfully.");
                    logger.info("Item '" + item.getTitle() + "' returned successfully.");

                } else if (isBorrowable.test(item)) {
                    logger.warning("Item was not borrowed.");
                    return null;
                } else {
                    logger.warning("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }

    public LibraryItem editReturnTime(int id , String date) {
        synchronized (library) {
            var item = itemById.get(id);
            if (item != null) {
                if (isBorrowable.test(item)) {
                    item.setReturnTime(date);
                    logger.info("Item '" + item.getTitle() + "' return time has been set successfully.");
                } else if (isBorrowed.test(item)) {
                    logger.warning("Item is borrowed, you can't change return time");
                    return null;
                } else {
                    logger.warning("Item '" + item.getTitle() + "' is banned.");
                }
            } else {
                System.out.println("Item not found.");
                return null;
            }
            return item;
        }
    }
}
