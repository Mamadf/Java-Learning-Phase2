package org.example.Repository;

import org.example.Config.DatabaseConfig;
import org.example.Exception.GlobalExceptionHandler;
import org.example.Model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OperationRepository {

    public void borrow(LibraryItem item) {
        String sql = "INSERT INTO operations (item_id, user_id, borrow_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getId());
            ps.setInt(2, 1); // default user id
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setDate(4, Date.valueOf(LocalDate.now().plusDays(14)));
            ps.setDate(5, null);

            ps.executeUpdate();
            item.setReturnTime(String.valueOf(LocalDate.now().plusDays(14)));
        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    public void returnItem(LibraryItem item) {
        String sql = "UPDATE operations SET return_date = ? WHERE item_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, item.getId());

            ps.executeUpdate();
            item.setReturnTime(null);
        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    public void add(LibraryItem item) {
        String sql = "INSERT INTO LibraryItem (title, author, publication_year, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getTitle());
            ps.setString(2, item.getAuthor());
            ps.setInt(3, item.getPublicationYear());
            ps.setString(4,ItemStatus.EXIST.toString());
            ps.executeUpdate();

            int itemId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    itemId = rs.getInt(1);
                    item.setId(itemId);
                } else {
                    throw new SQLException("Failed to retrieve generated item_id.");
                }
            }

            if (item instanceof Book b) insertBook(conn, itemId, b);
            else if (item instanceof Magazine m) insertMagazine(conn, itemId, m);
            else if (item instanceof ReferenceBook r) insertReferenceBook(conn, itemId, r);
            else if (item instanceof Thesis t) insertThesis(conn, itemId, t);

        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    private void insertBook(Connection conn, int itemId, Book b) throws SQLException {
        String sql = "INSERT INTO Book (item_id, genre, page) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setString(2, b.getGenre());
            ps.setInt(3, b.getPages());
            ps.executeUpdate();
        }
    }

    private void insertMagazine(Connection conn, int itemId, Magazine m) throws SQLException {
        String sql = "INSERT INTO Magazine (item_id, publisher, issue) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setString(2, m.getPublisher());
            ps.setInt(3, m.getIssue());
            ps.executeUpdate();
        }
    }

    private void insertReferenceBook(Connection conn, int itemId, ReferenceBook r) throws SQLException {
        String sql = "INSERT INTO ReferenceBook (item_id, subject, edition) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setString(2, r.getSubject());
            ps.setString(3, r.getEdition());
            ps.executeUpdate();
        }
    }

    private void insertThesis(Connection conn, int itemId, Thesis t) throws SQLException {
        String sql = "INSERT INTO Thesis (item_id, university, supervisor) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setString(2, t.getUniversity());
            ps.setString(3, t.getSupervisor());
            ps.executeUpdate();
        }
    }

    public void delete(LibraryItem item) {
        if (item == null) return;

        try (Connection conn = DatabaseConfig.getConnection()) {

// Map item types to their corresponding tables
            Map<Class<? extends LibraryItem>, String> tableMap = Map.of(
                    Book.class, "Book",
                    Magazine.class, "Magazine",
                    ReferenceBook.class, "ReferenceBook",
                    Thesis.class, "Thesis"
            );

// Delete from specific item table if applicable
            String tableName = tableMap.get(item.getClass());
            if (tableName != null) {
                deleteByItemId(conn, tableName, item.getId());
            }

// Delete related records
            deleteByItemId(conn, "Operations", item.getId());
            deleteByItemId(conn, "LibraryItem", item.getId());

        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }





    private void deleteByItemId(Connection conn, String tableName, int itemId) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public void update(LibraryItem item) {
        if (item == null) return;

        try (Connection conn = DatabaseConfig.getConnection()) {

            String baseSql = "UPDATE LibraryItem SET title = ?, author = ?, status = ?, publication_year = ? WHERE item_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(baseSql)) {
                ps.setString(1, item.getTitle());
                ps.setString(2, item.getAuthor());
                ps.setString(3, item.getStatus().name());
                ps.setInt(4, item.getPublicationYear());
                ps.setInt(5, item.getId());
                ps.executeUpdate();
            }

            Map<Class<? extends LibraryItem>, String> updateMap = Map.of(
                    Book.class, "UPDATE Book SET genre = ?, page = ? WHERE item_id = ?",
                    Magazine.class, "UPDATE Magazine SET publisher = ?, issue = ? WHERE item_id = ?",
                    ReferenceBook.class, "UPDATE ReferenceBook SET subject = ?, edition = ? WHERE item_id = ?",
                    Thesis.class, "UPDATE Thesis SET university = ?, supervisor = ? WHERE item_id = ?"
            );

            String sql = updateMap.get(item.getClass());
            if (sql != null) {
                updateSpecific(conn, sql, item);
            }

        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    private void updateSpecific(Connection conn, String sql, LibraryItem item) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (item instanceof Book b) {
                ps.setString(1, b.getGenre());
                ps.setInt(2, b.getPages());
            } else if (item instanceof Magazine m) {
                ps.setString(1, m.getPublisher());
                ps.setInt(2, m.getIssue());
            } else if (item instanceof ReferenceBook r) {
                ps.setString(1, r.getSubject());
                ps.setString(2, r.getEdition());
            } else if (item instanceof Thesis t) {
                ps.setString(1, t.getUniversity());
                ps.setString(2, t.getSupervisor());
            }

            ps.setInt(3, item.getId());
            ps.executeUpdate();
        }
    }


    public void updateDueDate(int itemId, String newDueDate) {
        String sql = "UPDATE operations SET due_date = ? WHERE item_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(newDueDate));
            ps.setInt(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

}
