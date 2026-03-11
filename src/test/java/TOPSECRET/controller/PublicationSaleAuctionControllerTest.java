package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryPublicationList(), and putPublicationOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private PublicationSaleAuctionController controller;
    private LibraryRepo libraryRepo;
    private ItemRepo itemRepo;
    private AuctionRepo auctionRepo;
    private ItemFactory itemFactory;
    private AuctionFactory auctionFactory;
    private User testUser;
    private Library libraryDouble;
    private Item itemDouble;
    private Auction auctionDouble;

    @BeforeEach
    void setUp() {
        libraryRepo = mock(LibraryRepo.class);
        itemRepo = mock(ItemRepo.class);
        auctionRepo = mock(AuctionRepo.class);
        itemFactory = mock(ItemFactory.class);
        auctionFactory = mock(AuctionFactory.class);

        // SUT
        controller = new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo, itemFactory, auctionFactory);

        testUser = mock(User.class);
        libraryDouble = mock(Library.class);
        itemDouble = mock(Item.class);
        auctionDouble = mock(Auction.class);
    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {
        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo, itemFactory, auctionFactory));
    }


    @Test
    void testConstructorWithNullRepos() {
        // Arrange / Act / Assert
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(null, itemRepo, auctionRepo, itemFactory, auctionFactory));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, null, auctionRepo, itemFactory, auctionFactory));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, itemRepo, null, itemFactory, auctionFactory));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo, null, auctionFactory));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(libraryRepo, itemRepo, auctionRepo, itemFactory, null));
    }

    @Test
    void testGetLibraryPublicationListNullUser() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getLibraryPublicationList(null), "User required");
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary() {
        // Arrange
        User userWithoutLibrary = mock(User.class);
        when(libraryRepo.findLibraryByUser(userWithoutLibrary))
                .thenThrow(new IllegalStateException("Library not found for user: " + userWithoutLibrary));

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryPublicationList(userWithoutLibrary),
                "Library not found for user: " + userWithoutLibrary.toString());
    }

    @Test
    void testGetLibraryPublicationListForUserWithEmptyLibrary() {
        // Arrange
        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(libraryDouble);
        when(libraryDouble.getPublicationsInLibrary()).thenReturn(List.of());

        // Act
        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryPublicationListForUserWithPublicationInLibrary() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(libraryDouble);
        when(libraryDouble.getPublicationsInLibrary()).thenReturn(List.of(new PublicationDetails(testPub)));

        // Act
        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testGetLibraryPublicationListIsImmutable() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(libraryDouble);
        when(libraryDouble.getPublicationsInLibrary()).thenReturn(List.of(new PublicationDetails(testPub)));

        List<PublicationDetails> result = controller.getLibraryPublicationList(testUser);

        // Act / Assert
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
    void testPutPublicationOnAuctionWithNullArguments() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        // Act / Assert
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
    void testPutPublicationOnAuctionWithInvalidDates() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        // Act / Assert
        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.POOR,
                new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().minusDays(1)));
    }

    @Test
    void testPutPublicationOnAuctionPublicationAlreadyItem() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        when(itemRepo.exists(testPub)).thenReturn(true);

        // Act / Assert
        assertNull(controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD,
                new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
    }

    @Test
    void testPutPublicationOnAuctionPublicationNotInUserLibrary() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(libraryDouble);
        when(libraryDouble.getPublicationFromLibrary(testPub))
                .thenThrow(new IllegalArgumentException("Publication not found in user's library"));

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD,
                        new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Publication not found in user's library");
    }

    @Test
    void testPutPublicationOnAuctionSuccess() {
        // Arrange
        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        Price startPrice = new Price(10, Currency.EUR);
        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(libraryDouble);
        when(libraryDouble.getPublicationFromLibrary(testPub)).thenReturn(testPub);
        when(itemFactory.createItem(testPub, Condition.GOOD)).thenReturn(itemDouble);
        when(auctionFactory.createAuction(itemDouble, startPrice, startDate, endDate)).thenReturn(auctionDouble);
        when(itemDouble.getAuction()).thenReturn(auctionDouble);

        // Act
        Auction result = controller.putPublicationOnAuction(testUser, testPub, Condition.GOOD, startPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(auctionDouble, result);
        assertSame(auctionDouble, itemDouble.getAuction());
    }


}