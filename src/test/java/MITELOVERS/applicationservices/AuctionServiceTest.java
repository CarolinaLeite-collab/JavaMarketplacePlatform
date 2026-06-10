package MITELOVERS.applicationservices;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    //SUT
    @InjectMocks
    AuctionService _auctionService;

    @Mock
    ILibraryRepo _iLibraryRepoDouble;

    @Mock
    IAuctionRepo _iAuctionRepoDouble;

    @Mock
    AuctionFactory _auctionFactoryDouble;

    @Mock
    IItemRepo _iItemRepoDouble;



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
        _userIdDouble = mock(UserId.class);
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
        AuctionService service = new AuctionService(_iLibraryRepoDouble, _iAuctionRepoDouble,
                _auctionFactoryDouble, _iItemRepoDouble);
    }

    @Test
    void getLibraryItemsIdListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        LibraryId libraryIdDouble = mock(LibraryId.class);
        LibraryId _libraryIdDouble2 = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble2))
                    .thenReturn(_libraryIdDouble2);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble2)).thenReturn(Optional.empty());
            // Act / Assert
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> _auctionService.getLibraryItemsIdList(_userIdDouble2));

            assertEquals("Library not found for user!", ex.getMessage());
        }
    }

    @Test
    void testGetLibraryItemsIdListForUserWithEmptyLibrary() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.of(libraryDouble));

            when(libraryDouble.getItemsIdInLibrary())
                    .thenReturn(List.of());

            // Act
            List<ItemId> result = _auctionService.getLibraryItemsIdList(_userIdDouble);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetLibraryItemsIdListIsImmutable() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.of(libraryDouble));

            when(libraryDouble.getItemsIdInLibrary())
                    .thenReturn(List.of(_itemIdDouble));

            // Act
            List<ItemId> result = _auctionService.getLibraryItemsIdList(_userIdDouble);

            // Assert
            assertThrows(UnsupportedOperationException.class,
                    () -> result.add(_itemIdDouble));
        }
    }

    @Test
    void shouldReturnItemIdsFromUserLibrary() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.of(libraryDouble));

            when(libraryDouble.getItemsIdInLibrary())
                    .thenReturn(List.of(_itemIdDouble));

            // Act
            List<ItemId> result = _auctionService.getLibraryItemsIdList(_userIdDouble);

            // Assert
            assertEquals(1, result.size());
            assertEquals(_itemIdDouble, result.get(0));
        }
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                .thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getSaleStatus())
                .thenReturn(SaleStatus.NotOnSale);

        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,
                _startDate, _endDate)).thenReturn(_auctionDouble);

        when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

        // Act
        Auction result = _auctionService.putItemOnAuction(
                _itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
    }

    @Test
    void testPutItemIdOnAuctionWithoutOutrightPriceSuccess() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                .thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getSaleStatus())
                .thenReturn(SaleStatus.NotOnSale);

        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, null,
                _startDate, _endDate)).thenReturn(_auctionDouble);

        when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

        // Act
        Auction result = _auctionService.putItemOnAuction(
                _itemsId, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
    }

    @Test
    void testPutItemOnAuctionWhenItemDoesNotExist() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _auctionService.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("Item not found"));
    }

    @Test
    void testPutItemOnAuctionWhenItemIsAlreadyOnSale() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _auctionService.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
    }

    @Test
    void shouldThrowWhenAuctionAlreadyExists() {

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                .thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getSaleStatus())
                .thenReturn(SaleStatus.NotOnSale);

        AuctionId auctionId = mock(AuctionId.class);

        when(_auctionDouble.identity())
                .thenReturn(auctionId);

        when(_auctionFactoryDouble.createAuction(
                _itemsId,
                _startingPriceDouble,
                _reservePriceDouble,
                _outrightPriceDouble,
                _startDate,
                _endDate))
                .thenReturn(_auctionDouble);

        when(_iAuctionRepoDouble.containsOfIdentity(auctionId))
                .thenReturn(true);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _auctionService.putItemOnAuction(
                        _itemsId,
                        _startingPriceDouble,
                        _reservePriceDouble,
                        _outrightPriceDouble,
                        _startDate,
                        _endDate)
        );

        assertEquals("Auction already exists!", ex.getMessage());
    }
}