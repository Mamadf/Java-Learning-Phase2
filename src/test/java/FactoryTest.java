import org.example.Factory.*;
import org.example.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {

    private LibraryItemFactory bookFactory;
    private LibraryItemFactory magazineFactory;
    private LibraryItemFactory referenceBookFactory;
    private LibraryItemFactory thesisFactory;

    @BeforeEach
    void setUp() {
        bookFactory = LibraryItemFactoryProducer.getFactory("Book");
        magazineFactory = LibraryItemFactoryProducer.getFactory("Magazine");
        referenceBookFactory = LibraryItemFactoryProducer.getFactory("ReferenceBook");
        thesisFactory = LibraryItemFactoryProducer.getFactory("Thesis");
    }

    @Test
    void testBookFactoryCreateAndUpdate() {
        // Create
        String createInput = "Java Basics\nJohn Smith\n2020\ntrue\nProgramming\n300\n";
        Scanner createScanner = new Scanner(new ByteArrayInputStream(createInput.getBytes()));
        LibraryItem book = bookFactory.createItem(createScanner);

        assertTrue(book instanceof Book);
        assertEquals("Java Basics", book.getTitle());
        assertEquals("John Smith", book.getAuthor());
        assertEquals(2020, book.getPublicationYear());
        assertTrue(book.isAvailable());
        assertEquals("Programming", ((Book) book).getGenre());
        assertEquals(300, ((Book) book).getPages());

        // Update
        String updateInput = "Java Advanced\nJohn Doe\n2021\nProgramming Advanced\n350\n";
        Scanner updateScanner = new Scanner(new ByteArrayInputStream(updateInput.getBytes()));
        bookFactory.updateItem(book, updateScanner);

        assertEquals("Java Advanced", book.getTitle());
        assertEquals("John Doe", book.getAuthor());
        assertEquals(2021, book.getPublicationYear());
        assertEquals("Programming Advanced", ((Book) book).getGenre());
        assertEquals(350, ((Book) book).getPages());
    }

    @Test
    void testMagazineFactoryCreateAndUpdate() {
        String createInput = "Science Today\nMary Editor\n2023\nfalse\nNature Pub\n12\n";
        Scanner createScanner = new Scanner(new ByteArrayInputStream(createInput.getBytes()));
        LibraryItem magazine = magazineFactory.createItem(createScanner);

        assertTrue(magazine instanceof Magazine);
        assertEquals("Science Today", magazine.getTitle());
        assertEquals("Mary Editor", magazine.getAuthor());
        assertEquals(2023, magazine.getPublicationYear());
        assertFalse(magazine.isAvailable());
        assertEquals("Nature Pub", ((Magazine) magazine).getPublisher());
        assertEquals(12, ((Magazine) magazine).getIssue());

        String updateInput = "Science Updated\nMary Editor 2\n2024\nNature Pub 2\n15\n";
        Scanner updateScanner = new Scanner(new ByteArrayInputStream(updateInput.getBytes()));
        magazineFactory.updateItem(magazine, updateScanner);

        assertEquals("Science Updated", magazine.getTitle());
        assertEquals("Mary Editor 2", magazine.getAuthor());
        assertEquals(2024, magazine.getPublicationYear());
        assertEquals("Nature Pub 2", ((Magazine) magazine).getPublisher());
        assertEquals(15, ((Magazine) magazine).getIssue());
    }

    @Test
    void testReferenceBookFactoryCreateAndUpdate() {
        String createInput = "Oxford Dictionary\nOxford Press\n2015\ntrue\nLanguage\n13\n";
        Scanner createScanner = new Scanner(new ByteArrayInputStream(createInput.getBytes()));
        LibraryItem referenceBook = referenceBookFactory.createItem(createScanner);

        assertTrue(referenceBook instanceof ReferenceBook);
        assertEquals("Oxford Dictionary", referenceBook.getTitle());
        assertEquals("Oxford Press", referenceBook.getAuthor());
        assertEquals(2015, referenceBook.getPublicationYear());
        assertTrue(referenceBook.isAvailable());
        assertEquals("Language", ((ReferenceBook) referenceBook).getSubject());
        assertEquals("13", ((ReferenceBook) referenceBook).getEdition());

        String updateInput = "Oxford Updated\nOxford Press 2\n2016\nGrammar\n14\n";
        Scanner updateScanner = new Scanner(new ByteArrayInputStream(updateInput.getBytes()));
        referenceBookFactory.updateItem(referenceBook, updateScanner);

        assertEquals("Oxford Updated", referenceBook.getTitle());
        assertEquals("Oxford Press 2", referenceBook.getAuthor());
        assertEquals(2016, referenceBook.getPublicationYear());
        assertEquals("Grammar", ((ReferenceBook) referenceBook).getSubject());
        assertEquals("14", ((ReferenceBook) referenceBook).getEdition());
    }

    @Test
    void testThesisFactoryCreateAndUpdate() {
        String createInput = "AI in Healthcare\nAli Reza\n2022\nfalse\nTehran Uni\nDr. Karimi\n";
        Scanner createScanner = new Scanner(new ByteArrayInputStream(createInput.getBytes()));
        LibraryItem thesis = thesisFactory.createItem(createScanner);

        assertTrue(thesis instanceof Thesis);
        assertEquals("AI in Healthcare", thesis.getTitle());
        assertEquals("Ali Reza", thesis.getAuthor());
        assertEquals(2022, thesis.getPublicationYear());
        assertFalse(thesis.isAvailable());
        assertEquals("Tehran Uni", ((Thesis) thesis).getUniversity());
        assertEquals("Dr. Karimi", ((Thesis) thesis).getSupervisor());

        String updateInput = "AI Updated\nAli Reza 2\n2023\nTehran Uni 2\nDr. Karimi 2\n";
        Scanner updateScanner = new Scanner(new ByteArrayInputStream(updateInput.getBytes()));
        thesisFactory.updateItem(thesis, updateScanner);

        assertEquals("AI Updated", thesis.getTitle());
        assertEquals("Ali Reza 2", thesis.getAuthor());
        assertEquals(2023, thesis.getPublicationYear());
        assertEquals("Tehran Uni 2", ((Thesis) thesis).getUniversity());
        assertEquals("Dr. Karimi 2", ((Thesis) thesis).getSupervisor());
    }
}
