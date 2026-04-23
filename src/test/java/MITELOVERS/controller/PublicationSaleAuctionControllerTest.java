package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PublicationSaleAuctionControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private AuctionFactory _auctionFactoryDouble;
    private IItemRepo _iItemRepoDouble;
    private Library _userLibraryDouble;
    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Price _startingPriceDouble;
    private Price _reservePriceDouble;
    private Price _outrightPriceDouble;
    private Auction _auctionDouble;
    private UserId _userIdDouble;
    private Item _itemDouble;
    private ZonedDateTime _startDate;
    private ZonedDateTime _endDate;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _auctionFactoryDouble = mock(AuctionFactory.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _userIdDouble = mock(UserId.class);
        _userLibraryDouble = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);
        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);
        _startingPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _auctionDouble = mock(Auction.class);
        _itemDouble = mock(Item.class);

        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);
    }

    @Test
    void testConstructor() {
        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);
    }

    @Test
    void getLibraryItemsIdListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2)).thenThrow(new IllegalStateException("Library not found for user"));

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        //act
        List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

        //assert
        assertEquals(1, result.size());
        assertEquals(_itemIdDouble, result.get(0));
    }

    @Test
    void addAuctionWithoutOutrightStoresAuction() {
        //Arrange
        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble,
                null, _startDate, _endDate)).thenReturn(_auctionDouble);
        when(_iAuctionRepoDouble.save(_auctionDouble)).thenReturn(_auctionDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        //Act
        Auction created = controller.addAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        //Assert
        assertSame(_auctionDouble, created);
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble,
                _outrightPriceDouble, _startDate, _endDate)).thenReturn(_auctionDouble);
        when(_iAuctionRepoDouble.save(_auctionDouble)).thenReturn(_auctionDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        // Act
        Auction result = controller.putItemOnAuction(
                _iItemRepoDouble, _itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        // Assert
        assertSame(_auctionDouble, result);
        verify(_auctionFactoryDouble).createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        verify(_iAuctionRepoDouble).save(_auctionDouble);
        verify(_itemDouble).markAsAuction();
    }

    @Test
    void testPutItemIdOnAuctionWithoutOutrightPriceSuccess() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, null,
                _startDate, _endDate)).thenReturn(_auctionDouble);
        when(_iAuctionRepoDouble.save(_auctionDouble)).thenReturn(_auctionDouble);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        // Act
        Auction result = controller.putItemOnAuction(
                _iItemRepoDouble, _itemsId, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
        verify(_auctionFactoryDouble).createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, null, _startDate, _endDate);
        verify(_iAuctionRepoDouble).save(_auctionDouble);
        verify(_itemDouble).markAsAuction();
    }

    @Test
    void testPutItemOnAuctionWhenItemDoesNotExist() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.putItemOnAuction(
                        _iItemRepoDouble, _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("Item not found"));
        verify(_auctionFactoryDouble, never()).createAuction(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testPutItemOnAuctionWhenItemIsAlreadyOnSale() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble, _userIdDouble);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.putItemOnAuction(
                        _iItemRepoDouble, _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
        verify(_auctionFactoryDouble, never()).createAuction(any(), any(), any(), any(), any(), any());
    }
}
