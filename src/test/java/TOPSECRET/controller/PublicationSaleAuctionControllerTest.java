package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Currency;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryPublicationList(), and putPublicationOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private ILibraryRepo _libraryRepoDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private Library _userLibraryDouble;
    private Library _libraryDouble2;
    private Item _itemDouble;
    private Auction _auctionDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _libraryRepoDouble = mock(ILibraryRepo.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _userDouble = mock(User.class);
        _userLibraryDouble = mock(Library.class);
        _libraryDouble2 = mock(Library.class);
        _itemDouble = mock(Item.class);
        _auctionDouble = mock(Auction.class);

    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble));
    }

    @Test
    void testConstructorWithNullRepos() {
        // Arrange / Act / Assert
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(null, _iAuctionRepoDouble, _userLibraryDouble));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(_libraryRepoDouble, null, _userLibraryDouble));
        assertThrows(NullPointerException.class, () -> new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, null));
    }

    @Test
    void testGetLibraryPublicationListNullUser() {

        // Arrange & SUT

        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getLibraryItemsList(null), "User required");
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary() {
        // Arrange
        User _userDouble2 = mock(User.class);
        when(_libraryRepoDouble.findLibraryByUser(_userDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryItemsList(_userDouble2),
                "Library not found for user");
    }

    @Test
    void testGetLibraryItemsListForUserWithEmptyLibrary() {
        // Arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of());

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2);

        // Act
        List<Item> result = controller.getLibraryItemsList(_userDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryPublicationListIsImmutable() {
        //Arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2);

        //Act
        List<Item> result = controller.getLibraryItemsList(_userDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void testPutItemOnAuctionWithNullArguments() throws InstantiationException{
        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble);

        // Act / Assert
        assertNull(controller.putItemOnAuction(null, new Price(2, Currency.EUR), new Price(10, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
        assertNull(controller.putItemOnAuction(_itemDouble,null, new Price(2, Currency.EUR), ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)));
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(2, Currency.EUR), new Price(10, Currency.EUR), null, ZonedDateTime.now()));
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(2, Currency.EUR), new Price(10, Currency.EUR), ZonedDateTime.now(), null));
    }


    @Test
    void testPutPublicationOnAuctionWithInvalidDates() throws InstantiationException{
        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble);

        // Act & Assert
        assertNull(controller.putItemOnAuction(_itemDouble, new Price(10, Currency.EUR), new Price(15, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().minusDays(1)));
    }
    @Test
    void shouldReturnNullWhenAuctionRepoThrowsIllegalArgumentException() throws InstantiationException {
        //arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_libraryDouble2.getItem(_itemDouble)).thenReturn(_itemDouble);
        when(_iAuctionRepoDouble.createAuction(_itemDouble, startPrice, outrightPrice, startDate, endDate))
                .thenThrow(new IllegalArgumentException("Invalid auction parameters"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2);

        //act
        Auction result = controller.putItemOnAuction(_itemDouble, startPrice, outrightPrice, startDate, endDate);

        //assert
        assertNull(result);
    }

    @Test
    void shouldReturnItemsFromUserLibrary() {
        //arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2);

        //act
        List<Item> result = controller.getLibraryItemsList(_userDouble);

        //assert
        assertEquals(1, result.size());
        assertEquals(_itemDouble, result.get(0));
    }

    @Test
    void testPutPublicationOnAuctionSuccess() throws InstantiationException {
        //Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_userLibraryDouble);
        when(_iAuctionRepoDouble.createAuction(_itemDouble, startPrice, outrightPrice, startDate, endDate)).thenReturn(_auctionDouble);
        when(_libraryDouble2.getItem(_itemDouble)).thenReturn(_itemDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_libraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2);

        // Act
        Auction result = controller.putItemOnAuction(_itemDouble, startPrice, outrightPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);;
        verify(_itemDouble).setAuction(_auctionDouble);
        verify(_libraryDouble2).getItem(_itemDouble);
        verify(_iAuctionRepoDouble).createAuction(_itemDouble, startPrice, outrightPrice, startDate, endDate);
    }
}