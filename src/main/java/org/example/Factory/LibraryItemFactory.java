package org.example.Factory;

import org.example.Model.LibraryItem;
import java.util.Scanner;

public interface LibraryItemFactory {
    LibraryItem createItem(Scanner scanner);
}
