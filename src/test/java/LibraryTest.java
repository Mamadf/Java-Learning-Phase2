import org.example.Repository.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryLoanService;
import org.example.Service.LibraryManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private LibraryData library;
    private LibraryManagerService  libraryManagerService;
    private LibraryLoanService libraryLoanService;
    private LibraryItem item1;
    private LibraryItem item2;
    private LibraryItem item3;
    private LibraryItem item4;
    private LibraryItem item5;
    private LibraryItem item6;
    private String firstItemTitle;
    private String forthItemTitle;
    private String nonExistence;
    private String date;
    @BeforeEach
    void setUp() {
        library = new LibraryData();
        libraryLoanService = new LibraryLoanService(library);
        libraryManagerService = new LibraryManagerService(library);
        item1 = new Book("Java Basics", "John Smith", 2020 , true,  "Programming", 300);
        item2 = new Magazine("Science Today", "Mary Editor", 2023, false,"Nature Pub" , 12);
        item3 = new Thesis("AI in Healthcare", "Ali Reza", 2022, false, "Tehran Uni", "Dr. Karimi");
        item4 = new Book("test", "Ali Reza", 2022, true,"Tehran Uni", 23);
        item5 = new Magazine("test", "Reza", 2018, true, "Tehran Uni", 1);
        item6 = new ReferenceBook("Oxford Dictionary", "Oxford Press", 2015,true , "Language", "13");
        date = "2023-12-03";

        firstItemTitle = "Java Basics";
        forthItemTitle = "test";
        nonExistence = "nonexistence";

    }

    @Test
    void testAddItem() {
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        assertEquals(2, library.getItems().size());
        assertTrue(library.getItems().contains(item1));
        assertTrue(library.getItems().contains(item1));
    }

    @Test
    void testDeleteBooks() {
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        libraryManagerService.deleteItem(item1.getId());
        libraryManagerService.deleteItem(item3.getId());
        assertEquals(1, library.getItems().size());
        assertFalse(library.getItems().contains(item1));
        assertTrue(library.getItems().contains(item2));
    }

//    @Test
//    void testUpdateBooks() {
//        library.addItem(item1);
//        library.updateBook(item1 , item2);
//        assertEquals(1, library.getItems().size());
//        assertEquals(secondBookTitle, item1.getTitle());
//    }
//

    @Test
    void testSearchBTitle() {
        libraryManagerService.addItem(item1);
        List<LibraryItem> items = libraryManagerService.searchByTitle(firstItemTitle);
        assertEquals(1, items.size());
        assertTrue(items.contains(item1));
    }

    @Test
    void testSearchByTitleMultipleBooksSameTitle() {
        libraryManagerService.addItem(item4);
        libraryManagerService.addItem(item5);

        List<LibraryItem> result = libraryManagerService.searchByTitle(forthItemTitle);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchByTitleBookNotExists() {
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);

        List<LibraryItem> result = libraryManagerService.searchByTitle(nonExistence);
        assertTrue(result.isEmpty());
    }

    //    @Test
//    void testSearchByAuthor() {
//        library.addItem(item2);
//        library.addItem(book4);
//
//        MyLinkedList<Book> result = library.searchByAuthor(secondBookAuthor);
//
//        MyLinkedList<Book> result2 = library.searchByAuthor(nonExistence);
//
//        assertNull(result2);
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void testSortedByPublicationYear() {
//        library.addItem(book3); // 1960
//        library.addItem(item1); // 1925
//        library.addItem(item2); // 1949
//
//        library.sortedByPublicationYear();
//        MyLinkedList<Book> sortedBooks = library.getItems();
//        assertEquals(3, sortedBooks.size());
//        assertEquals(year1, sortedBooks.get(0).getPublicationYear());
//        assertEquals(year2, sortedBooks.get(1).getPublicationYear());
//        assertEquals(year3, sortedBooks.get(2).getPublicationYear());
//    }
//
    @Test
    void testgetItems() {
        assertNotNull(library.getItems());
        assertEquals(0, library.getItems().size());

        libraryManagerService.addItem(item1);
        assertEquals(1, library.getItems().size());
    }

    @Test
    void testBorrowItem(){
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        LibraryItem resultItem = libraryLoanService.borrowItem(item1.getId());
        LibraryItem resultItem2 = libraryLoanService.borrowItem(item2.getId()); //since is already borrowed
        assertEquals(false, resultItem.isAvailable());
        assertNull(resultItem2);
    }

    @Test
    void testReturnItem(){
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        LibraryItem resultItem = libraryLoanService.returnItem(item1.getId());
        LibraryItem resultItem2 = libraryLoanService.returnItem(item2.getId());
        assertEquals(true, resultItem2.isAvailable());
        assertNull(resultItem);
    }
    @Test
    void testReturnTime(){
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        LibraryItem resultItem = libraryLoanService.editReturnTime(item1.getId() , date);
        LibraryItem resultItem2 = libraryLoanService.editReturnTime(item2.getId() , date);
        assertEquals(date, resultItem.getReturnTime().toString());
        assertNull(resultItem2);

    }
}