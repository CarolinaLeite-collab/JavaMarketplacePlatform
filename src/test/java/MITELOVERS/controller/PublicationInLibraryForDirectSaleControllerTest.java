package MITELOVERS.controller;

import MITELOVERS.controllers.cli.PublicationInLibraryForDirectSaleController;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IDirectSaleRepo;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class PublicationInLibraryForDirectSaleControllerTest {

    @Mock
    ILibraryRepo _iLibraryRepoDouble;

    @Mock
    IDirectSaleRepo _iDirectSaleRepoDouble;

    @Mock
    IItemRepo _iItemRepoDouble;

    @Mock
    DirectSaleFactory _directSaleFactory;

    @InjectMocks
    PublicationInLibraryForDirectSaleController _publicationInLibraryForDirectSaleController;

    private UserId _userIdDouble;
    private Library _libraryDouble;
    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Duration _timeLimitDouble;
    private Item _itemDouble;


    @BeforeEach
    void setUp() {
        _userIdDouble = mock(UserId.class);
        _libraryDouble = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);
        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);
        _priceDouble = mock(Price.class);
        _timeLimitDouble = Duration.ofDays(30);
        _itemDouble = mock(Item.class);
    }

    @Test
    void testPublicationInLibraryForDirectSaleControllerWhenLibraryExists() {

        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble));    // SUT

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

            //Act
            List<ItemId> result = _publicationInLibraryForDirectSaleController.getItemsIdInLibraryByUserId(_userIdDouble);

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

            //Act
            List<ItemId> result = _publicationInLibraryForDirectSaleController.getItemsIdInLibraryByUserId(_userIdDouble);

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

            //Act
            List<ItemId> result = _publicationInLibraryForDirectSaleController.getItemsIdInLibraryByUserId(_userIdDouble);

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

            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
            when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                    .thenReturn(directSaleDouble);

            // Act
            DirectSale result = _publicationInLibraryForDirectSaleController.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);

            // Assert
            assertNotNull(result);
            assertSame(directSaleDouble, result);
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

            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
            when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                    .thenReturn(directSaleDouble);

            // Act
            _publicationInLibraryForDirectSaleController.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);

            // Assert
            verify(_itemDouble).markAsDirectSale();
        }
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {

        // Arrange
        LibraryId _libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

        DirectSale directSaleDouble = mock(DirectSale.class);

        LibraryId libraryId = mock(LibraryId.class);

        when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble)).thenReturn(directSaleDouble);
        when(directSaleDouble.identity()).thenReturn(mock(MITELOVERS.domain.valueobject.DirectSaleId.class));
        when(_iDirectSaleRepoDouble.containsOfIdentity(any())).thenReturn(false);

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryId);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> _publicationInLibraryForDirectSaleController.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble));

        // Assert
        assertTrue(exception.getMessage().contains("Item not found"));
        }
    }
    @Test
    void shouldThrowExceptionWhenItemAlreadyOnAuction() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

        DirectSale directSaleMock = mock(DirectSale.class);
        when(_directSaleFactory.createDirectSale(any(), any(), any()))
                .thenReturn(directSaleMock);
        when(_iDirectSaleRepoDouble.containsOfIdentity(any())).thenReturn(false);

            when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                    .thenReturn(Optional.of(_itemDouble));

            when(_itemDouble.getSaleStatus())
                    .thenReturn(SaleStatus.OnAuction);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _publicationInLibraryForDirectSaleController.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
        }
    }

    @Test
    void putItemIdOnDirectSaleShouldThrowIfDirectSaleAlreadyExists() {
        // Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);
        when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble)).thenReturn(directSaleDouble);
        when(_iDirectSaleRepoDouble.containsOfIdentity(directSaleDouble.identity())).thenReturn(true);

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _publicationInLibraryForDirectSaleController.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble)
        );
        assertTrue(ex.getMessage().contains("Direct sale already exists"));

    }
}
