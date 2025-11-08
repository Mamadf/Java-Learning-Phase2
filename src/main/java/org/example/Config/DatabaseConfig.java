package org.example.Config;

import org.example.Exception.GlobalExceptionHandler;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseConfig {

    private DatabaseConfig() {}
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    public static Connection getConnection() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                logger.warning("Could not find config.properties in resources folder.");
            } else {
                props.load(input);
            }
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            GlobalExceptionHandler.handle(e);
        } catch (Exception e) {
            GlobalExceptionHandler.handle(e);
        }

        return null;
    }
}
