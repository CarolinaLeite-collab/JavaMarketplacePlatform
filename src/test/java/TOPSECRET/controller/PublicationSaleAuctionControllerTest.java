package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryPublicationList(), and putPublicationOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private PublicationSaleAuctionController controller;
    private LibraryRepo libraryRepo;
    private ItemRepo itemRepo;
    private AuctionRepo auctionRepo;
    private User testUser;
    private Library testLibrary;
    private CountryFactory _countryFactory;
    private LibraryFactory _libraryFactory = new LibraryFactory();
    private Country _country;

    @BeforeEach
    void setUp() {
        libraryRepo = new LibraryRepo(_libraryFactory);
        itemRepo = new ItemRepo();
        auctionRepo = new AuctionRepo();
        _countryFactory = new CountryFactory();
        _country = _countryFactory.createFactory("Portugal");
        controller = new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo);
        testUser = mock(User.class);
//        testUser = new User(
//                new Name("John Test"),
//                new Address("Test Road", "123", Address.BuildingType.HOUSE, "Porto", "Porto", _country, "4000-123", null),
//                new Email("test@isep.ipp.pt"),
//                new Phone(new PhonePrefix("+351"), "999999999"));
        testLibrary = libraryRepo.addLibrary(testUser);

    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        //validating constructor works when given all parameters
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo));
    }


    @Test
    void testConstructorWithNullRepos() {

        //validates constructor doesn't work when repo parameter is missing

        // Null libraryRepo → fails on getLibraryPublicationList
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(null, itemRepo, auctionRepo));

        // Null itemRepo → fails putPublicationOnAuction() at createItem()
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, null, auctionRepo));

        // Test null auctionRepo → fails putPublicationOnAuction() at createAuction()
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, itemRepo, null));
    }

    @Test
    void testGetLibraryPublicationListNullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.getLibraryPublicationList(null), "User required");
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary(){

        //instantiate new user that doesn't have a library
        User userWithoutLibrary = mock(User.class);
//        User userWithoutLibrary = new User(
//                new Name("NoLibrary"),
//                new Address("NoLib Road", "456", Address.BuildingType.HOUSE,
//                        "Porto", "Porto", _country, "4000-456", null),
//                new Email("nolib@isep.ipp.pt"),
//                new Phone(new PhonePrefix("+351"), "888888888"));

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
                .publisher(new PublishingCompany("Penguin"))
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
                .publisher(new PublishingCompany("Penguin"))
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
                .publisher(new PublishingCompany("Penguin"))
                .build();

        PublicationDetails newPubDetails = new PublicationDetails(newTestPub);
        assertThrows(UnsupportedOperationException.class, () -> result.add(newPubDetails));
    }

    @Test
    void testPutPublicationOnAuctionWithNullArguments(){
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertNull(controller.putPublicationOnAuction(null, testPub, Condition.POOR,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));

        assertNull(controller.putPublicationOnAuction(testUser, null, Condition.POOR,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));

        assertNull(controller.putPublicationOnAuction(testUser, testPub, null,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));

        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.POOR,
                null, ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));

        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.POOR,
                new Price(10, Currency.EUR), null, ZonedDateTime.now().plusDays(1)));

        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.POOR,
                new Price(10, Currency.EUR), ZonedDateTime.now(), null));
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
                .publisher(new PublishingCompany("Penguin"))
                .build();
        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.POOR, new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().minusDays(1)));
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
                .publisher(new PublishingCompany("Penguin"))
                .build();

        // Publication added to user's library
        testLibrary.addPublicationToLibrary(testPub);

        // Mark testPub as already an item
        itemRepo.createItem(testPub, Condition.GOOD);

        //returns null because testPub was already made into an item (above);
        //putPublicationOnAuction tries doing so again here
        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
    }

    @Test
    void testPutPublicationOnAuctionPublicationNotInUserLibrary() {
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        // not adding testPub to testLibrary

        assertThrows(IllegalArgumentException.class,
                () -> controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD,
                        new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Publication not found in user's library");
    }

    @Test
    void testPutPublicationOnAuctionSuccess(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        // add publication to user's library
        testLibrary.addPublicationToLibrary(testPub);

        Auction result = controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD, new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(8));

        assertNotNull(result); // non-null auction means entire operation succeeded
        assertEquals(1, itemRepo.getAll().size()); //one item in itemRepo now
        Item createdItem = itemRepo.getAll().get(0);
        assertNotNull(createdItem.getAuction()); //proves item.setAuction(auction) carried out successfully
        assertEquals(createdItem, createdItem.getAuction().getItem()); //verified consistency in all directions
        assertEquals(testPub, createdItem.getPublication()); //correct publication was turned into item
        assertEquals(Condition.GOOD, createdItem.getCondition()); //proves condition parameter was preserved

        //Using existing AuctionRepo method to verify auction was created, persists, and linked to its item
        assertEquals(1, auctionRepo.getAuctionItemsByAuthor(new Author("George Orwell")).size());

    }


}