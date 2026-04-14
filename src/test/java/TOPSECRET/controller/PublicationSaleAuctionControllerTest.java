package TOPSECRET.controller;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryItemsIdList(), and putItemIdOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private Library _userLibraryDouble;
    private Library _libraryDouble2;
    private ItemId _itemIdDouble;
    private Auction _auctionDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _userIdDouble = mock(UserId.class);
        _userLibraryDouble = mock(Library.class);
        _libraryDouble2 = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);
        _auctionDouble = mock(Auction.class);
    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble, _userIdDouble));
    }

    @Test
    void testGetLibraryItemsIdListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _userLibraryDouble, _userIdDouble);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getLibraryItemsIdList(_userIdDouble2),
                "Library not found for user");
    }

    @Test
    void testGetLibraryItemsIdListForUserWithEmptyLibrary() {
        // Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        // Act
        List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLibraryItemsIdListIsImmutable() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        //Act
        List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
    }

    @Test
    void shouldReturnItemIdsFromUserLibrary() {
        //arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_userLibraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        //act
        List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

        //assert
        assertEquals(1, result.size());
        assertEquals(_itemIdDouble, result.get(0));
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        //Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_libraryDouble2.getItemId(_itemIdDouble)).thenReturn(_itemIdDouble);
        when(_iAuctionRepoDouble.createAuction(_itemIdDouble, startPrice, reservePrice, outrightPrice, startDate, endDate)).thenReturn(_auctionDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _libraryDouble2, _userIdDouble);

        // Act
        Auction result = controller.putItemIdOnAuction(_itemIdDouble, startPrice, reservePrice, outrightPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
        verify(_itemIdDouble).setAuction(_auctionDouble);
        verify(_libraryDouble2).getItemId(_itemIdDouble);
        verify(_iAuctionRepoDouble).createAuction(_itemIdDouble, startPrice, reservePrice, outrightPrice, startDate, endDate);
    }
}
