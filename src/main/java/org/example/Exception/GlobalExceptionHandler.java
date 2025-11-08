package org.example.Exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    public static void handle(Throwable e) {
        if (e instanceof NumberFormatException) {
            logger.warning("❌ Invalid number! Please enter an integer.");
        } else if (e instanceof IllegalArgumentException) {
            logger.warning(e.getMessage());
        } else if (e instanceof NullPointerException) {
            logger.severe("❌ Something went wrong (missing value).");
        }else if(e instanceof FileNotFoundException){
            logger.info("No existing library file found — starting new library.");
        }else if (e instanceof IOException) {
            logger.severe("❌ File error: " + e.getMessage());
        } else if (e instanceof InterruptedException) {
            logger.warning("❌ Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } else if (e instanceof SQLException) {
            logger.severe("❌ Database error: " + e.getMessage());
        } else if (e instanceof RuntimeException) {
            logger.severe("❌ Runtime error: " + e.getMessage());
        } else {
            logger.severe("❌ Unexpected error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }


}
