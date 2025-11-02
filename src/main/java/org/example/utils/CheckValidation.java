package org.example.utils;

import org.example.Exception.GlobalExceptionHandler;
import org.example.Model.ItemStatus;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CheckValidation {

    private static final Pattern DATE_PATTERN =
            Pattern.compile("^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");

    public static int getValidInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                GlobalExceptionHandler.handle(e);
            }
        }
    }


    public static String getValidDate(Scanner scanner) {
        while (true) {
            String date = scanner.nextLine().trim();

            if (DATE_PATTERN.matcher(date).matches()) {
                return date;
            } else {
                System.out.println("❌ Invalid date format! Example: 2024-03-15");
            }
        }
    }


    public static String getNonEmptyString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("❌ Input cannot be empty. Please enter a value.");
            }
        }
    }


    public static String getValidQuery(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            String[] key = input.split(",");

            if (key.length != 3) {
                System.out.println("❌ Wrong input format! Your input should be: <title>,<author>,<publicationYear>");
            } else if (key[0].matches("\\d+") || key[1].matches("\\d+")) {
                System.out.println("❌ Title and author cannot be numbers!");
            } else {
                try {
                    Integer.parseInt(key[2]);
                    return input;
                } catch (NumberFormatException e) {
                    System.out.println("❌ Publication year must be a number!");
                }
            }
        }
    }

    public static boolean isValidItemStatus(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String normalized = input.trim().toUpperCase();
        for (ItemStatus status : ItemStatus.values()) {
            if (status.name().equals(normalized)) {
                return true;
            }
        }
        return false;
    }


}
