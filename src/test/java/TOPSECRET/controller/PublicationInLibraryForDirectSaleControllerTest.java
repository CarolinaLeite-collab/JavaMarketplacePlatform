package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicationInLibraryForDirectSaleControllerTest {

    private PublicationInLibraryForDirectSaleController controller;
    private LibraryRepo libraryRepo;
    private PublicationRepo publicationRepo;
    private ItemRepo itemRepo;
    private DirectSaleRepo directSaleRepo;
    private User testUser;
    private Library testLibrary;
    private Period timeLimit;
    private CountryFactory _countryFactory;
    private Country _country;

    @BeforeEach
    void setUp() {
        _countryFactory = new CountryFactory();
        _country = _countryFactory.createClass("Portugal");           libraryRepo = new LibraryRepo();
        publicationRepo = new PublicationRepo();
        itemRepo = new ItemRepo();
        directSaleRepo = new DirectSaleRepo();
        timeLimit = Period.ofDays(100);

        controller = new PublicationInLibraryForDirectSaleController(libraryRepo, publicationRepo, itemRepo, directSaleRepo);

        testUser = new User(
                new Name("John Test"),
                new Address("Test Road", "123", Address.BuildingType.HOUSE, "Porto", "Porto", _country, "4000-123", null),
                new Email("test@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "999999999"));
        testLibrary = libraryRepo.createMyLibrary(testUser);

    }

    @Test
    void testConstructorPublicationInLibraryForDirectSaleController() {

        assertDoesNotThrow(() -> new PublicationInLibraryForDirectSaleController(libraryRepo, publicationRepo, itemRepo, directSaleRepo));
    }


    @Test
    void testConstructorWithNullRepos() {

        //validates constructor doesn't work when a repo parameter is missing

        // Null libraryRepo → fails on getPublicationsInLibraryList
        PublicationInLibraryForDirectSaleController nullLibraryController =
                new PublicationInLibraryForDirectSaleController(null, publicationRepo, itemRepo, directSaleRepo);
        assertThrows(NullPointerException.class, () ->
                nullLibraryController.getPublicationsInLibraryList(testUser));

        // Null publicationRepo → fails on addPublicationForDirectSale
        PublicationInLibraryForDirectSaleController nullPubController =
                new PublicationInLibraryForDirectSaleController(libraryRepo, null, itemRepo, directSaleRepo);

        Publication testPub = Publication.builder().type(new PublicationType("BOOK"))
                .identifier(new ISBN("9781800816862")).year(Year.of(2020))
                .title(new Title("Test")).author(new Author("Testy McTest"))
                .publisher(new PublishingCompany("Testing Co.")).build();

        assertThrows(NullPointerException.class,
                () -> nullPubController.addPublicationForDirectSale(
                        testPub,
                        Condition.LIKE_NEW,
                        new Price(10, Currency.EUR),
                        timeLimit)
        );

        // Null itemRepo → fails addPublicationForDirectSale() at createItem()
        PublicationInLibraryForDirectSaleController nullItemController =
                new PublicationInLibraryForDirectSaleController(libraryRepo, publicationRepo, null, directSaleRepo);
        publicationRepo.add(testPub);
        assertThrows(NullPointerException.class,
                () -> nullItemController.addPublicationForDirectSale(testPub, Condition.LIKE_NEW,
                        new Price(10, Currency.EUR), timeLimit));

        // Test null directSaleRepo → fails addPublicationForDirectSale() at createDirectSale()
        PublicationInLibraryForDirectSaleController nullDirectSaleController =
                new PublicationInLibraryForDirectSaleController(libraryRepo, publicationRepo, itemRepo, null);
        assertThrows(NullPointerException.class,
                () -> nullDirectSaleController.addPublicationForDirectSale(testPub, Condition.LIKE_NEW,
                        new Price(10, Currency.EUR), timeLimit));
    }

    @Test
    void testGetPublicationsInLibraryListNullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.getPublicationsInLibraryList(null), "User required");
    }

    @Test
    void testGetPublicationsInLibraryList_ForUserWithoutLibrary() {

        //instantiate new user that doesn't have a library
        User userWithoutLibrary = new User(
                new Name("NoLibrary"),
                new Address("NoLib Road", "456", Address.BuildingType.HOUSE,
                        "Porto", "Porto", _country, "4000-456", null),
                new Email("nolib@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "888888888"));

        assertThrows(IllegalStateException.class,
                () -> controller.getPublicationsInLibraryList(userWithoutLibrary),
                "Library not found for user: " + userWithoutLibrary.toString());
    }

    @Test
    void testGetPublicationsInLibraryList_ForUserWithEmptyLibrary() {
        List<Publication> result = controller.getPublicationsInLibraryList(testUser); //testUser doesn't have any pubs in library yet
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPublicationsInLibraryList_ForUserWithPublicationInLibrary() {

        //Adding pub to library using Library's method addPublicationToLibrary()
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        testLibrary.addPublicationToLibrary(testPub);

        List<Publication> result = controller.getPublicationsInLibraryList(testUser);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPublicationsInLibraryListIsImmutable() {

        //Adding pub to library using Library's method addPublicationToLibrary()
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        testLibrary.addPublicationToLibrary(testPub);

        List<Publication> result = controller.getPublicationsInLibraryList(testUser);

        // Ensuring above result from getLibraryPublicationList is immutable

        Publication newTestPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141023571"))
                .year(Year.of(2006))
                .title(new Title("Of Mice and Men"))
                .author(new Author("John Steinbeck"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertThrows(UnsupportedOperationException.class, () -> result.add(newTestPub));
    }

    @Test
    void testAddNullPublicationForDirectSale() {
        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationForDirectSale(null, Condition.LIKE_NEW,
                new Price(10, Currency.EUR), timeLimit));
    }

    @Test
    void testAddPublicationForDirectSaleWithNullCondition() throws InstantiationException {
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        publicationRepo.add(testPub);
        assertFalse(controller.addPublicationForDirectSale(testPub, null,
                new Price(10, Currency.EUR), timeLimit));

    }

//    @Test
//    void testAddPublicationForDirectSaleWithInvalidTimeLimit() {
//
//        // time limit cannot be negative
//
//        Publication testPub = Publication.builder()
//                .type(new PublicationType("BOOK"))
//                .identifier(new ISBN("9780141036144"))
//                .year(Year.of(2012))
//                .title(new Title("1984"))
//                .author(new Author("George Orwell"))
//                .publisher(new PublishingCompany("Penguin"))
//                .build();
//
//        publicationRepo.add(testPub);
//        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationForDirectSale(testPub, Condition.POOR, new Price(10, Currency.EUR), Period.ofDays(-5)));
//    }

    @Test
    void testAddPublicationForDirectSale_ItemAlreadyExists() {

        // make publication an item:
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        publicationRepo.add(testPub);
        itemRepo.createItem(testPub, Condition.GOOD);

        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationForDirectSale(testPub, Condition.GOOD,
                new Price(10, Currency.EUR), timeLimit));
    }

    @Test
    void testAddPublicationForDirectSaleSuccess() throws InstantiationException {

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        publicationRepo.add(testPub);

        boolean result = controller.addPublicationForDirectSale(testPub, Condition.GOOD, new Price(10, Currency.EUR), timeLimit);

        assertTrue(result);

        // The item must have been created
        assertEquals(1, itemRepo.getAll().size());
        Item createdItem = itemRepo.getAll().get(0);

        assertEquals(testPub, createdItem.getPublication());
        assertEquals(Condition.GOOD, createdItem.getCondition());

        // DirectSale must exist because it was created by the controller
        List<Item> itemsByAuthor = directSaleRepo.getDirectSaleItemsByAuthor(new Author("George Orwell"));
        assertEquals(1, itemsByAuthor.size());

        // Assert that the item retuned contains the directSale reference
        DirectSale createdSale = createdItem.getDirectSale();
        assertNotNull(createdSale, "Item must reference its DirectSale");

    }

    @Test
    void testAddPublicationNotInRepoOrLibraryForDirectSale() {

        Publication unknownPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141439556"))  // Invalid ISBN
                .year(Year.of(2002))
                .title(new Title("Wuthering Heights"))
                .author(new Author("Emily Brönte"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        //  Publication never added to PublicationRepo, and therefore it was never added to library

        assertThrows(IllegalArgumentException.class, () ->
                        controller.addPublicationForDirectSale(unknownPub, Condition.GOOD,
                                new Price(10, Currency.EUR), timeLimit),
                "Publication not found");
    }

}