package org.example.Service;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Model.ItemStatus;
import org.example.Observer.Notifier;
import org.example.Observer.User;
import org.example.Repository.LibraryData;
import org.example.Model.LibraryItem;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                    SQLBorrow(item);
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
    public static void SQLBorrow(LibraryItem item ) {
        String sql = "INSERT INTO operations (item_id, user_id, borrow_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getId());
            ps.setInt(2, 1);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setDate(4, Date.valueOf(LocalDate.now().plusDays(14)));
            ps.setDate(5, null);

            ps.executeUpdate();
            System.out.println("✅ Borrow operation saved successfully!");
        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }
    public static void SQLReturn(LibraryItem item ) {
        String sql = "UPDATE operations SET return_date = ? WHERE item_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, item.getId());

            ps.executeUpdate();
            System.out.println("✅ Return operation saved successfully!");
        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
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
                    SQLReturn(item);
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
