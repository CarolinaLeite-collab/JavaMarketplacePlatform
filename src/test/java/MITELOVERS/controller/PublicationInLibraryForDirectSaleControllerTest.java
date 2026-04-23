package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private UserId _userIdDouble;
    private Library _libraryDouble;
    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Period _timeLimitDouble;
    private Item _itemDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _userIdDouble = mock(UserId.class);
        _libraryDouble = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);

        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);

        _priceDouble = mock(Price.class);
        _timeLimitDouble = Period.ofDays(30);

        _itemDouble = mock(Item.class);
    }

    @Test
    void testPublicationInLibraryForDirectSaleControllerWhenLibraryExists() {

        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble));    // SUT

    }

    @Test
    void testGetItemsIdInLibraryForUserWithEmptyLibraryByUser() {
        //Arrange
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

            //SUT
            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            //Act
            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

            //Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

        }
    }

    @Test
    void testGetItemsInLibraryForUserWithItemsIdInLibraryByUser() {
        //Arrange
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

            //SUT
            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            //Act
            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

            //Assert
            assertEquals(1, result.size());
            assertEquals(_itemIdDouble, result.get(0));

        }
    }

    @Test
    void testGetItemsIdInLibraryByUserListIsImmutable() {
        //Arrange
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

            //SUT
            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            //Act
            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

            //Assert
            assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
        }
    }

    @Test
    void testPutItemIdOnDirectSaleSuccess() {
        // Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
            when(_iDirectSaleRepoDouble.addDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                    .thenReturn(directSaleDouble);

            // SUT
            PublicationInLibraryForDirectSaleController controller =
                    new PublicationInLibraryForDirectSaleController(
                            _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            // Act
            DirectSale result = controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);

            // Assert
            assertNotNull(result);
            assertSame(directSaleDouble, result);
            verify(_iDirectSaleRepoDouble).addDirectSale(_itemsId, _priceDouble, _timeLimitDouble);
            verify(_itemDouble).markAsDirectSale();
        }
    }

    @Test
    void testPutItemIdOnDirectSaleMarksItemAsDirectSale() {
        // Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
            when(_iDirectSaleRepoDouble.addDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                    .thenReturn(directSaleDouble);

            PublicationInLibraryForDirectSaleController controller =
                    new PublicationInLibraryForDirectSaleController(
                            _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            // Act
            controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);

            // Assert
            verify(_itemDouble).markAsDirectSale();
        }
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        List<ItemId> items = List.of(itemId);
        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryId);

            when(_iLibraryRepoDouble.ofIdentity(libraryId))
                    .thenReturn(Optional.of(mock(Library.class)));

            when(_iItemRepoDouble.ofIdentity(itemId))
                    .thenReturn(Optional.empty());

            // SUT
            PublicationInLibraryForDirectSaleController controller =
                    new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            // Act + Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> controller.putItemIdOnDirectSale(items, _priceDouble, _timeLimitDouble)
            );

            assertTrue(exception.getMessage().contains("Item not found"));
        }
    }

    @Test
    void shouldThrowExceptionWhenItemAlreadyOnSale() {

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.of(_libraryDouble));

            when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                    .thenReturn(Optional.of(_itemDouble));

            when(_itemDouble.getSaleStatus())
                    .thenReturn(SaleStatus.OnAuction);

            // SUT
            PublicationInLibraryForDirectSaleController controller =
                    new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

            // Act + Assert
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> controller.putItemIdOnDirectSale(
                            _itemsId, _priceDouble, _timeLimitDouble)
            );

            // Assert
            assertTrue(exception.getMessage().contains("already on sale"));

            verify(_iDirectSaleRepoDouble, never())
                    .addDirectSale(any(), any(), any());
        }
    }
}
