package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Auction;
import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryPublicationList(), and putPublicationOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private Library _userLibraryDouble;
    private Library _libraryDouble2;
    private AuctionId _auctionId;
    private Item _itemDouble;
    private List<Item> _items;
    private Auction _auctionDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _userIdDouble = mock(UserId.class);
        _userLibraryDouble = mock(Library.class);
        _libraryDouble2 = mock(Library.class);
        _auctionId = mock(AuctionId.class);
        _itemDouble = mock(Item.class);
        _items = new ArrayList<>();
        _items.add(_itemDouble);
        _auctionDouble = mock(Auction.class);

    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble, _userIdDouble));
    }

    @Test
    void testGetLibraryPublicationListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble, _userIdDouble);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryItemsList(_userIdDouble2),
                "Library not found for user");
    }

    @Test
    void testGetLibraryItemsListForUserWithEmptyLibrary() {
        // Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of());

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        // Act
        List<Item> result = controller.getLibraryItemsList(_userIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryPublicationListIsImmutable() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        //Act
        List<Item> result = controller.getLibraryItemsList(_userIdDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void shouldReturnItemsFromUserLibrary() {
        //arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        //act
        List<Item> result = controller.getLibraryItemsList(_userIdDouble);

        //assert
        assertEquals(1, result.size());
        assertEquals(_itemDouble, result.get(0));
    }

    @Test
    void testPutPublicationOnAuctionSuccess() throws InstantiationException {
        //Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_iAuctionRepoDouble.addAuction(_auctionId ,_items, startPrice, reservePrice, outrightPrice, startDate, endDate)).thenReturn(_auctionDouble);
        when(_libraryDouble2.getItem(_itemDouble)).thenReturn(_itemDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        // Act
        Auction result = controller.putItemOnAuction(_auctionId ,_items, startPrice, reservePrice, outrightPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);;
        verify(_itemDouble).setAuction(_auctionDouble);
        verify(_libraryDouble2).getItem(_itemDouble);
        verify(_iAuctionRepoDouble).addAuction(_auctionId ,_items, startPrice, reservePrice, outrightPrice, startDate, endDate);
    }
}