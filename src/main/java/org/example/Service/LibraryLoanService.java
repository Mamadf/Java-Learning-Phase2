package org.example.Service;

import org.example.Model.ItemStatus;
import org.example.Observer.Notifier;
import org.example.Observer.User;
import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LibraryLoanService {
    private static final Logger logger = Logger.getLogger(LibraryLoanService.class.getName());

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
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.EXIST) {
                    item.setStatus(ItemStatus.BORROWED);
                    logger.info("Item '" + item.getTitle() + "' borrowed successfully.");
                } else if (item.getStatus() == ItemStatus.BORROWED) {
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
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.BORROWED) {
                    item.setStatus(ItemStatus.EXIST);
                    item.setReturnTime(LocalDate.now().toString());
                    Notifier notifier = new Notifier();
                    notifier.notifyObservers("Item '" + item.getTitle() + "' returned successfully.");
                    logger.info("Item '" + item.getTitle() + "' returned successfully.");

                } else if (item.getStatus() == ItemStatus.EXIST) {
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
            LibraryItem item = itemById.get(id);
            if (item != null) {
                if (item.getStatus() == ItemStatus.EXIST) {
                    item.setReturnTime(date);
                    logger.info("Item '" + item.getTitle() + "' return time has been set successfully.");
                } else if (item.getStatus() == ItemStatus.BORROWED) {
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
