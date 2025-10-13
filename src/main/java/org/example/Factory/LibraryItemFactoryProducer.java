package org.example.Factory;

import java.util.HashMap;
import java.util.Map;

public class LibraryItemFactoryProducer {
    private static final Map<String, LibraryItemFactory> factories = new HashMap<>();

    static {
        factories.put("Book", new BookFactory());
        factories.put("Magazine", new MagazineFactory());
        factories.put("ReferenceBook", new ReferenceBookFactory());
        factories.put("Thesis", new ThesisFactory());
    }

    public static LibraryItemFactory getFactory(String type) {
        return factories.get(type);
    }
}
