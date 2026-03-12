package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private LibraryRepo _libraryRepoDouble;
    private AuctionRepo _auctionRepoDouble;
    private ItemFactory _itemFactoryDouble;
    private Library _libraryDouble;
    private Item _itemDouble;
    private Auction _auctionDouble;
    private User _userDouble;
    private Publication _publicationDouble;

    @BeforeEach
    void setUp() {
        _libraryRepoDouble = mock(LibraryRepo.class);
        _auctionRepoDouble = mock(AuctionRepo.class);
        _itemFactoryDouble = mock(ItemFactory.class);
        _userDouble = mock(User.class);
        _publicationDouble = mock(Publication.class);
        _libraryDouble = mock(Library.class);
        _itemDouble = mock(Item.class);
        _auctionDouble = mock(Auction.class);

        // SUT
        controller = new PublicationSaleAuctionController(_libraryRepoDouble, _auctionRepoDouble, _libraryDouble);

    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {
        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(_libraryRepoDouble, _auctionRepoDouble, _libraryDouble));
    }


    @Test
    void testConstructorWithNullRepos() {
        // Arrange / Act / Assert
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(null, _auctionRepoDouble, _libraryDouble));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(_libraryRepoDouble, null, _libraryDouble));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(_libraryRepoDouble, _auctionRepoDouble, null));
    }

    @Test
    void testGetLibraryPublicationListNullUser() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getLibraryItemsList(null), "User required");
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary() {
        // Arrange
        User _userDouble2 = mock(User.class);
        when(_libraryRepoDouble.findLibraryByUser(_userDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryItemsList(_userDouble2),
                "Library not found for user");
    }

    @Test
    void testGetLibraryItemsListForUserWithEmptyLibrary() {
        // Arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of());

        // Act
        List<Item> result = controller.getLibraryItemsList(_userDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryPublicationListIsImmutable() {
        //Arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        //Act
        List<Item> result = controller.getLibraryItemsList(_userDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void testPutItemOnAuctionWithNullArguments() throws InstantiationException{
        // Act / Assert
        assertNull(controller.putItemOnAuction(null, new Price(2, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
        assertNull(controller.putItemOnAuction(_itemDouble,null, ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(2, Currency.EUR), null, ZonedDateTime.now().plusDays(1)));
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(2, Currency.EUR), ZonedDateTime.now(), null));
    }


    @Test
    void testPutPublicationOnAuctionWithInvalidDates() throws InstantiationException{
        // Act / Assert
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().minusDays(1)));
    }



    @Test
    void testPutPublicationOnAuctionSuccess() throws InstantiationException {
        //Arrange
        Price startPrice = mock(Price.class);
        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_auctionRepoDouble.createAuction(_itemDouble, startPrice, startDate, endDate)).thenReturn(_auctionDouble);
        when(_itemDouble.getAuction()).thenReturn(_auctionDouble);
        when(_libraryDouble.getItem(_itemDouble)).thenReturn(_itemDouble);

        // Act
        Auction result = controller.putItemOnAuction(_itemDouble, startPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
        assertSame(_auctionDouble, _itemDouble.getAuction());
    }
}