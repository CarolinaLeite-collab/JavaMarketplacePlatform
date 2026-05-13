package MITELOVERS.controller;

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
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class PublicationSaleAuctionControllerTest {

    @Mock
    ILibraryRepo _iLibraryRepoDouble;

    @Mock
    IAuctionRepo _iAuctionRepoDouble;

    @Mock
    AuctionFactory _auctionFactoryDouble;

    @Mock
    IItemRepo _iItemRepoDouble;

    @InjectMocks
    PublicationSaleAuctionController _publicationSaleAuctionController;

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
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);
    }

    @Test
    void getLibraryItemsIdListForUserWithoutLibrary() {
        // Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        LibraryId _libraryIdDouble2 = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble2))
                    .thenReturn(_libraryIdDouble2);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble2)).thenReturn(Optional.empty());

            // SUT
            PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                    _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

            // Act / Assert
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> controller.getLibraryItemsIdList(_userIdDouble2));

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

            // SUT
            PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                    _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

            // Act
            List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

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

            // SUT
            PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                    _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

            // Act
            List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

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

            // SUT
            PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                    _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

            // Act
            List<ItemId> result = controller.getLibraryItemsIdList(_userIdDouble);

            // Assert
            assertEquals(1, result.size());
            assertEquals(_itemIdDouble, result.get(0));
        }
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                    .thenReturn(Optional.of(_itemDouble));

            when(_itemDouble.getSaleStatus())
                    .thenReturn(SaleStatus.NotOnSale);

            when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,
                    _startDate, _endDate)).thenReturn(_auctionDouble);

            when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

            // SUT
            PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                    _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

            // Act
            Auction result = controller.putItemOnAuction(
                    _itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

            // Assert
            assertNotNull(result);
            assertSame(_auctionDouble, result);
            verify(_auctionFactoryDouble).createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble,
                    _outrightPriceDouble, _startDate, _endDate);
            verify(_iAuctionRepoDouble).save(_auctionDouble);
            verify(_itemDouble).markAsAuction();
        }
    }

        @Test
        void testPutItemIdOnAuctionWithoutOutrightPriceSuccess() {
            // Arrange
            LibraryId libraryIdDouble = mock(LibraryId.class);
            Library libraryDouble = mock(Library.class);

            try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

                mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                        .thenReturn(libraryIdDouble);

                when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                        .thenReturn(Optional.of(_itemDouble));

                when(_itemDouble.getSaleStatus())
                        .thenReturn(SaleStatus.NotOnSale);

                when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, null,
                        _startDate, _endDate)).thenReturn(_auctionDouble);

                when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

                // SUT
                PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                        _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

                // Act
                Auction result = controller.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

                // Assert
                assertNotNull(result);
                assertSame(_auctionDouble, result);
                verify(_auctionFactoryDouble).createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble,
                        null, _startDate, _endDate);
                verify(_iAuctionRepoDouble).save(_auctionDouble);
                verify(_itemDouble).markAsAuction();
            }
        }

    @Test
    void testPutItemOnAuctionWhenItemDoesNotExist() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // SUT
        PublicationSaleAuctionController controller = new PublicationSaleAuctionController(_iLibraryRepoDouble,
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
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
                _iAuctionRepoDouble, _auctionFactoryDouble, _iItemRepoDouble);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
        verify(_auctionFactoryDouble, never()).createAuction(any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldThrowWhenAuctionAlreadyExists() {

        // Assert + Act
        assertThrows(IllegalStateException.class, () ->
                _publicationSaleAuctionController.putItemOnAuction(
                        _itemsId,
                        _startingPriceDouble,
                        _reservePriceDouble,
                        _outrightPriceDouble,
                        _startDate,
                        _endDate));

        verify(_iAuctionRepoDouble, never()).save(any());
    }
}
