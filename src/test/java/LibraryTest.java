import org.example.Factory.BookFactory;
import org.example.Repository.LibraryData;
import org.example.Model.*;
import org.example.Service.LibraryLoanService;
import org.example.Service.LibraryManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Scanner;

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
        item1 = new Book("Java Basics", "John Smith", 2020 , ItemStatus.EXIST,  "Programming", 300);
        item2 = new Magazine("Science Today", "Mary Editor", 2023, ItemStatus.BORROWED,"Nature Pub" , 12);
        item3 = new Thesis("AI in Healthcare", "Ali Reza", 2022, ItemStatus.BORROWED, "Tehran Uni", "Dr. Karimi");
        item4 = new Book("test", "Ali Reza", 2022, ItemStatus.EXIST,"Tehran Uni", 23);
        item5 = new Magazine("test", "Reza", 2018, ItemStatus.EXIST, "Tehran Uni", 1);
        item6 = new ReferenceBook("Oxford Dictionary", "Oxford Press", 2015,ItemStatus.BANNED , "Language", "13");
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
//        libraryManagerService.addItem(item1);
//        libraryManagerService.update();
//        assertEquals(1, library.getItems().size());
//        assertEquals(secondBookTitle, item1.getTitle());
//    }

    @Test
    void testBookFactoryCreatesBook() {
        String input = "Java Basics\nJohn Smith\n2020\nEXIST\nProgramming\n300\n";
        Scanner scanner = new Scanner(input);

        BookFactory factory = new BookFactory();
        LibraryItem item = factory.createItem(scanner);

        assertEquals("Java Basics", item.getTitle());
        assertTrue(item instanceof Book);
    }

//    @Test
//    void testSearchBTitle() {
//        libraryManagerService.addItem(item1);
//        List<LibraryItem> items = libraryManagerService.searchByTitle(firstItemTitle);
//        assertEquals(1, items.size());
//        assertTrue(items.contains(item1));
//    }
//
//    @Test
//    void testSearchByTitleMultipleBooksSameTitle() {
//        libraryManagerService.addItem(item4);
//        libraryManagerService.addItem(item5);
//
//        List<LibraryItem> result = libraryManagerService.searchByTitle(forthItemTitle);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void testSearchByTitleBookNotExists() {
//        libraryManagerService.addItem(item1);
//        libraryManagerService.addItem(item2);
//
//        List<LibraryItem> result = libraryManagerService.searchByTitle(nonExistence);
//        assertTrue(result.isEmpty());
//    }


//    @Test
//    void testSearchByAuthor() {
//        libraryManagerService.addItem(item1);
//        libraryManagerService.addItem(item2);
//        libraryManagerService.addItem(item3);
//        libraryManagerService.addItem(item4);
//        libraryManagerService.addItem(item5);
//        libraryManagerService.addItem(item6);
//
//        List<LibraryItem> result = libraryManagerService.searchByAuthor("Ali Reza");
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(item3));
//        assertTrue(result.contains(item4));
//        result = libraryManagerService.searchByAuthor(nonExistence);
//        assertTrue(result.isEmpty());
//    }


    @Test
    void testSortByPublicationYear() {
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        libraryManagerService.addItem(item3);
        libraryManagerService.addItem(item4);
        libraryManagerService.addItem(item5);
        libraryManagerService.addItem(item6);

        libraryManagerService.sortByPublicationYear();
        List<LibraryItem> sorted = library.getItems();

        for (int i = 0; i < sorted.size() - 1; i++) {
            int currentYear = sorted.get(i).getPublicationYear();
            int nextYear = sorted.get(i + 1).getPublicationYear();
            assertTrue(currentYear <= nextYear);
        }

        assertEquals(2015, sorted.get(0).getPublicationYear());
        assertEquals(2023, sorted.get(sorted.size() - 1).getPublicationYear());
    }


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
        LibraryItem resultItem3 = libraryLoanService.borrowItem(1000);
        assertEquals(ItemStatus.BORROWED, resultItem.getStatus());
        assertNull(resultItem2);
        assertNull(resultItem3);
    }

    @Test
    void testReturnItem(){
        libraryManagerService.addItem(item1);
        libraryManagerService.addItem(item2);
        LibraryItem resultItem = libraryLoanService.returnItem(item1.getId());
        LibraryItem resultItem2 = libraryLoanService.returnItem(item2.getId());
        LibraryItem resultItem3= libraryLoanService.returnItem(1000);
        assertEquals(ItemStatus.EXIST, resultItem2.getStatus());
        assertNull(resultItem);
        assertNull(resultItem3);
    }
//    @Test
//    void borrowed
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