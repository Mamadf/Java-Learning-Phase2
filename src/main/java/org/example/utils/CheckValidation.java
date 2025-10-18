package org.example.utils;

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
                System.out.println("❌ Invalid number! Please enter an integer.");
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
}
