package org.example.Exception;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GlobalExceptionHandler {

    public static void handle(Exception e) {
        if (e instanceof NumberFormatException) {
            System.out.println("❌ Invalid number! Please enter an integer.");
        } else if (e instanceof IllegalArgumentException) {
            System.out.println(e.getMessage());
        } else if (e instanceof NullPointerException) {
            System.out.println("❌ Something went wrong (missing value).");
        }else if(e instanceof FileNotFoundException){
            System.out.println("❌ No existing library file found — starting new library.");
        }else if (e instanceof IOException) {
            System.out.println("❌ File error: " + e.getMessage());
        } else if (e instanceof InterruptedException) {
            System.out.println("❌ Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } else {
            System.out.println("❌ Unexpected error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }


}
