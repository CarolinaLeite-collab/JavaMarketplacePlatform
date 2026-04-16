package TOPSECRET.controller;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.SaleStatus;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h3>Unit tests for PublicationSaleAuctionController (US016)</h3>
 * Covers constructor, getLibraryItemsIdList(), and putItemIdOnAuction()
 */

class PublicationSaleAuctionControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private Library _userLibraryDouble;
    private Library _libraryDouble2;
    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Auction _auctionDouble;
    private UserId _userIdDouble;
    private Item _itemDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _userIdDouble = mock(UserId.class);
        _userLibraryDouble = mock(Library.class);
        _libraryDouble2 = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);
        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);
        _auctionDouble = mock(Auction.class);
        _itemDouble = mock(Item.class);
    }

    @Test
    void testUsingConstructorPublicationSaleAuctionController() {

        // Arrange / Act / Assert
        assertDoesNotThrow(() -> new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble));
    }

    @Test
    void getLibraryItemsIdListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

        //act
        List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

        //assert
        assertEquals(1, result.size());
        assertEquals(_itemIdDouble, result.get(0));
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        // Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_userLibraryDouble);
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_libraryDouble2.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));
        when(_iAuctionRepoDouble.addAuction(_itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate))
                .thenReturn(_auctionDouble);

        // SUT
        PublicationSaleAuctionController controller =
                new PublicationSaleAuctionController(
                        _iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

        // Act
        Auction result = controller.putItemOnAuction(
                _iItemRepoDouble, _itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
        verify(_iAuctionRepoDouble).addAuction(_itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate);
        verify(_itemDouble).markAsAuction();
    }

    @Test
    void testPutItemOnAuctionWhenItemDoesNotExist() {
        // Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // SUT
        PublicationSaleAuctionController controller =
                new PublicationSaleAuctionController(
                        _iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.putItemOnAuction(
                        _iItemRepoDouble, _itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate)
        );

        assertTrue(exception.getMessage().contains("Item not found"));
        verify(_iAuctionRepoDouble, never()).addAuction(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testPutItemOnAuctionWhenItemIsAlreadyOnSale() {
        // Arrange
        Price startPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(8);

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);

        PublicationSaleAuctionController controller =
                new PublicationSaleAuctionController(
                        _iLibraryRepoDouble, _iAuctionRepoDouble, _iItemRepoDouble, _userIdDouble
                );

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.putItemOnAuction(
                        _iItemRepoDouble, _itemsId, startPrice, reservePrice, outrightPrice, startDate, endDate)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
        verify(_iAuctionRepoDouble, never()).addAuction(any(), any(), any(), any(), any(), any());
    }
}
