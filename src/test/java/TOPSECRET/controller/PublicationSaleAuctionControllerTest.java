package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.PortUnreachableException;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryPublicationList(), and putPublicationOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private PublicationSaleAuctionController controller;
    private LibraryRepo libraryRepo;
    private PublicationRepo publicationRepo;
    private ItemRepo itemRepo;
    private AuctionRepo auctionRepo;
    private User testUser;
    private Library testLibrary;

    @BeforeEach
    void setUp() {
        libraryRepo = new LibraryRepo();
        publicationRepo = new PublicationRepo();
        itemRepo = new ItemRepo();
        auctionRepo = new AuctionRepo();

        controller = new PublicationSaleAuctionController(libraryRepo, publicationRepo, itemRepo, auctionRepo);

        testUser = new User(
                new Name("John Test"),
                new Address("Test Road", "123", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "4000-123", null),
                new Email("test@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "999999999"));
        testLibrary = libraryRepo.create(testUser);

    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        //validating constructor works when given all parameters
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(libraryRepo, publicationRepo, itemRepo, auctionRepo));
    }


    @Test
    void testConstructorWithNullRepos() {

        //validates constructor doesn't work when repo parameter is missing

        // Null libraryRepo → fails on getLibraryPublicationList
        PublicationSaleAuctionController nullLibraryController =
                new PublicationSaleAuctionController(null, publicationRepo, itemRepo, auctionRepo);
        assertThrows(NullPointerException.class, () ->
                nullLibraryController.getLibraryPublicationList(testUser));

        // Null publicationRepo → fails on putPublicationOnAuction
        PublicationSaleAuctionController nullPubController =
                new PublicationSaleAuctionController(libraryRepo, null, itemRepo, auctionRepo);

                Publication testPub = Publication.builder().type(new PublicationType("BOOK"))
                                .identifier(new ISBN("9781800816862")).year(Year.of(2020))
                                .title(new Title("Test")).author(new Author("Testy McTest"))
                                .publisher(new Publisher("Testing Co.")).build();

                        assertThrows(NullPointerException.class,
                                () -> nullPubController.putPublicationOnAuction(
                                        testPub,
                                        Condition.LIKE_NEW,
                                        new Price(10, Currency.EUR),
                                        ZonedDateTime.now(),
                                        ZonedDateTime.now().plusDays(7))
                        );

        // Null itemRepo → fails putPublicationOnAuction() at createItem()
        PublicationSaleAuctionController nullItemController =
                new PublicationSaleAuctionController(libraryRepo, publicationRepo, null, auctionRepo);
        publicationRepo.add(testPub);
        assertThrows(NullPointerException.class,
                () -> nullItemController.putPublicationOnAuction(testPub, Condition.LIKE_NEW,
                        new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)));

        // Test null auctionRepo → fails putPublicationOnAuction() at createAuction()
        PublicationSaleAuctionController nullAuctionController =
                new PublicationSaleAuctionController(libraryRepo, publicationRepo, itemRepo, null);
        assertThrows(NullPointerException.class,
                () -> nullAuctionController.putPublicationOnAuction(testPub, Condition.LIKE_NEW,
                        new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)));
    }

    @Test
    void testGetLibraryPublicationListNullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.getLibraryPublicationList(null), "User required");
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary(){

        //instantiate new user that doesn't have a library
        User userWithoutLibrary = new User(
                new Name("NoLibrary"),
                new Address("NoLib Road", "456", Address.BuildingType.HOUSE,
                        "Porto", "Porto", Address.Country.PORTUGAL, "4000-456", null),
                new Email("nolib@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "888888888"));

        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryPublicationList(userWithoutLibrary),
                "Library not found for user: " + userWithoutLibrary.toString());
    }

    @Test
    void testGetLibraryPublicationListForUserWithEmptyLibrary(){
        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser); //testUser doesn't have any pubs in library yet
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryPublicationListForUserWithPublicationInLibrary(){

        //Adding pub to library using Library's method addPublicationToLibrary()
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        testLibrary.addPublicationToLibrary(testPub);

        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser);

        assertEquals(1, result.size());
    }

    @Test
    void testGetLibraryPublicationListIsImmutable(){

        //Adding pub to library using Library's method addPublicationToLibrary()
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        testLibrary.addPublicationToLibrary(testPub);

        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser);

        // Ensuring above result from getLibraryPublicationList is immutable

        Publication newTestPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141023571"))
                .year(Year.of(2006))
                .title(new Title("Of Mice and Men"))
                .author(new Author("John Steinbeck"))
                .publisher(new Publisher("Penguin"))
                .build();

        PublicationDetails newPubDetails = new PublicationDetails(newTestPub);
        assertThrows(UnsupportedOperationException.class, () -> result.add(newPubDetails));
    }

    @Test
    void testPutNullPublicationOnAuction(){
        assertFalse(controller.putPublicationOnAuction(null, Condition.LIKE_NEW,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)));
    }

    @Test
    void testPutPublicationOnAuctionWithNullCondition(){
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        assertFalse(controller.putPublicationOnAuction(testPub, null,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));

    }

    @Test
    void testPutPublicationOnAuctionWithInvalidDates(){

        // End date comes before start date

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();
        ZonedDateTime now = ZonedDateTime.now();
        assertFalse(controller.putPublicationOnAuction(testPub, Condition.POOR, new Price(10, Currency.EUR), now, now.minusDays(1)));
    }

    @Test
    void testPutPublicationOnAuctionPublicationAlreadyItem(){

        // make publication an item:
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();
        publicationRepo.add(testPub);
        itemRepo.createItem(testPub, Condition.GOOD);

        //returns false because pub was already made into an item;
        //putPublicationOnAuction tries doing so again

        assertFalse(controller.putPublicationOnAuction(testPub, Condition.GOOD,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
    }

    @Test
    void testPutPublicationOnAuctionSuccess(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();
        publicationRepo.add(testPub);

        ZonedDateTime now = ZonedDateTime.now();

        boolean result = controller.putPublicationOnAuction(testPub, Condition.GOOD, new Price(10, Currency.EUR), now.plusDays(1), now.plusDays(8));

        assertTrue(result);
        assertEquals(1, itemRepo.getAll().size());
        Item createdItem = itemRepo.getAll().get(0);
        assertEquals(testPub, createdItem.getPublication());
        assertEquals(Condition.GOOD, createdItem.getCondition());

        //Using existing AuctionRepo method to verify item was successfully put on auction
        assertEquals(1, auctionRepo.getAuctionItemsByAuthor(new Author("George Orwell")).size());

    }

    @Test
    void testPutPublicationNotInRepoOrLibraryOnAuction() {

        Publication unknownPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141439556"))  // Invalid ISBN
                .year(Year.of(2002))
                .title(new Title("Wuthering Heights"))
                .author(new Author("Emily Brönte"))
                .publisher(new Publisher("Penguin"))
                .build();

        //  Publication never added to PublicationRepo, and therefore never to library

        assertThrows(IllegalArgumentException.class, () ->
                        controller.putPublicationOnAuction(unknownPub, Condition.GOOD,
                                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Publication not found");
        }

}