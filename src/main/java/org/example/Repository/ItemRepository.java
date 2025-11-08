package org.example.Repository;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Service.LibraryManagerService;
import org.example.Config.DatabaseConfig;
import org.example.Model.*;
import java.sql.*;

public class ItemRepository {

    public void loadAllItems() {
        LibraryData libraryData = LibraryData.getInstance();
        LibraryManagerService libraryManagerService = new LibraryManagerService(libraryData);

        String baseQuery = "SELECT * FROM LibraryItem";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(baseQuery);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("item_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String status = rs.getString("status");
                int year = rs.getInt("publication_year");

                LibraryItem item = loadSpecificItem(conn, id, title, author, status, year);
                PreparedStatement ps = conn.prepareStatement("SELECT due_date FROM operations WHERE item_id = ? AND return_date IS NULL");
                ps.setInt(1, id);
                ResultSet r = ps.executeQuery();
                if (r.next()) {
                    Date sqlDate = r.getDate("due_date");

                    if (sqlDate != null) {
                        item.setReturnTime(sqlDate.toLocalDate().toString());
                    } else {
                        item.setReturnTime(null);
                    }
                }
                if (item != null) libraryManagerService.addItem(item);
            }

        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        }

    }

    private LibraryItem loadSpecificItem(Connection conn, int id, String title, String author,
                                         String status, int year) throws SQLException {


        if (existsInTable(conn, "Book", id)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Book WHERE item_id = ?");
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                Book b = new Book(title, author, year, ItemStatus.valueOf(status),
                        r.getString("genre"), r.getInt("page"));
                b.setId(id);
                return b;
            }
        } else if (existsInTable(conn, "Magazine", id)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Magazine WHERE item_id = ?");
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                Magazine m = new Magazine(title, author, year, ItemStatus.valueOf(status),
                        r.getString("publisher"), r.getInt("issue"));
                m.setId(id);
                return m;
            }
        } else if (existsInTable(conn, "ReferenceBook", id)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ReferenceBook WHERE item_id = ?");
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                ReferenceBook ref = new ReferenceBook(title, author, year, ItemStatus.valueOf(status),
                        r.getString("subject"), r.getString("edition"));
                ref.setId(id);
                return ref;
            }
        } else if (existsInTable(conn, "Thesis", id)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Thesis WHERE item_id = ?");
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                Thesis t = new Thesis(title, author, year, ItemStatus.valueOf(status),
                        r.getString("university"), r.getString("supervisor"));
                t.setId(id);
                return t;
            }
        }

        return null;
    }

    private boolean existsInTable(Connection conn, String tableName, int itemId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE item_id = ?");
        ps.setInt(1, itemId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }
}
