//package MITELOVERS.controller;
//
//import MITELOVERS.domain.directsale.DirectSale;
//import MITELOVERS.domain.directsale.DirectSaleFactory;
//import MITELOVERS.domain.item.Item;
//import MITELOVERS.domain.library.Library;
//import MITELOVERS.domain.repository.IDirectSaleRepo;
//import MITELOVERS.domain.repository.IItemRepo;
//import MITELOVERS.domain.repository.ILibraryRepo;
//import MITELOVERS.domain.valueobject.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.time.Period;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PublicationInLibraryForDirectSaleControllerTest {
//
//    private ILibraryRepo _iLibraryRepoDouble;
//    private IDirectSaleRepo _iDirectSaleRepoDouble;
//    private IItemRepo _iItemRepoDouble;
//    private UserId _userIdDouble;
//    private Library _libraryDouble;
//    private List<ItemId> _itemsId;
//    private ItemId _itemIdDouble;
//    private Price _priceDouble;
//    private Duration _timeLimitDouble;
//    private Instant _creationDate;
//    private Item _itemDouble;
//    private DirectSaleFactory _directSaleFactory;
//
//    @BeforeEach
//    void setUp() {
//        _directSaleFactory = mock(DirectSaleFactory.class);
//        _iLibraryRepoDouble = mock(ILibraryRepo.class);
//        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
//        _iItemRepoDouble = mock(IItemRepo.class);
//        _userIdDouble = mock(UserId.class);
//        _libraryDouble = mock(Library.class);
//        _itemIdDouble = mock(ItemId.class);
//
//        _itemsId = new ArrayList<>();
//        _itemsId.add(_itemIdDouble);
//
//        _priceDouble = mock(Price.class);
//        _timeLimitDouble = Duration.ofDays(30);
//
//        _itemDouble = mock(Item.class);
//    }
//
//    @Test
//    void testPublicationInLibraryForDirectSaleControllerWhenLibraryExists() {
//
//        //Act + Assert
//        assertDoesNotThrow(() ->
//                new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble));    // SUT
//
//    }
//
//    @Test
//    void testGetItemsIdInLibraryForUserWithEmptyLibraryByUser() {
//        //Arrange
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());
//
//            //SUT
//            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);
//
//            //Act
//            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);
//
//            //Assert
//            assertNotNull(result);
//            assertTrue(result.isEmpty());
//
//        }
//    }
//
//    @Test
//    void testGetItemsInLibraryForUserWithItemsIdInLibraryByUser() {
//        //Arrange
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));
//
//            //SUT
//            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);
//
//            //Act
//            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);
//
//            //Assert
//            assertEquals(1, result.size());
//            assertEquals(_itemIdDouble, result.get(0));
//
//        }
//    }
//
//    @Test
//    void testGetItemsIdInLibraryByUserListIsImmutable() {
//        //Arrange
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));
//
//            //SUT
//            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);
//
//            //Act
//            List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);
//
//            //Assert
//            assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
//        }
//    }
//
//    @Test
//    void testPutItemIdOnDirectSaleSuccess() {
//        // Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
//            when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
//                    .thenReturn(directSaleDouble);
//
//            // SUT
//            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);
//
//            // Act
//            DirectSale result = controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);
//
//            // Assert
//            assertNotNull(result);
//            assertSame(directSaleDouble, result);
//        }
//    }
//
//    @Test
//    void testPutItemIdOnDirectSaleMarksItemAsDirectSale() {
//        // Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//            when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//            when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//            when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
//            when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
//                    .thenReturn(directSaleDouble);
//
//            //SUT
//            PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);
//
//            // Act
//            controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble);
//
//            // Assert
//            verify(_itemDouble).markAsDirectSale();
//        }
//    }
//
//    @Test
//    void shouldThrowExceptionWhenItemNotFound() {
//
//        // Arrange
//        LibraryId _libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(_libraryIdDouble);
//
//        DirectSale directSaleDouble = mock(DirectSale.class);
//
//        when(_iLibraryRepoDouble.ofIdentity(_libraryIdDouble)).thenReturn(Optional.ofNullable(_libraryDouble));
//        LibraryId libraryId = mock(LibraryId.class);
//
//        when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble)).thenReturn(directSaleDouble);
//        when(directSaleDouble.identity()).thenReturn(mock(MITELOVERS.domain.valueobject.DirectSaleId.class));
//        when(_iDirectSaleRepoDouble.containsOfIdentity(any())).thenReturn(false);
//
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryId);
//
//        // SUT
//        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);
//
//        // Act
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble));
//
//        // Assert
//        assertTrue(exception.getMessage().contains("Item not found"));
//        }
//    }
//    @Test
//    void shouldThrowExceptionWhenItemAlreadyOnAuction() {
//        // Arrange
//        LibraryId libraryIdDouble = mock(LibraryId.class);
//
//        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
//            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
//                    .thenReturn(libraryIdDouble);
//
//        DirectSale directSaleMock = mock(DirectSale.class);
//        when(_directSaleFactory.createDirectSale(any(), any(), any()))
//                .thenReturn(directSaleMock);
//        when(_iDirectSaleRepoDouble.containsOfIdentity(any())).thenReturn(false);
//
//            when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
//                    .thenReturn(Optional.of(_itemDouble));
//
//            when(_itemDouble.getSaleStatus())
//                    .thenReturn(SaleStatus.OnAuction);
//
//        //SUT
//        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);
//
//        // Act + Assert
//        IllegalStateException exception = assertThrows(
//                IllegalStateException.class,
//                () -> controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble)
//        );
//
//        assertTrue(exception.getMessage().contains("already on sale"));
//        }
//    }
//
//    @Test
//    void putItemIdOnDirectSaleShouldThrowIfDirectSaleAlreadyExists() {
//        // Arrange
//        DirectSale directSaleDouble = mock(DirectSale.class);
//        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
//        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
//        when(_directSaleFactory.createDirectSale(_itemsId, _priceDouble, _timeLimitDouble)).thenReturn(directSaleDouble);
//        when(_iDirectSaleRepoDouble.containsOfIdentity(directSaleDouble.identity())).thenReturn(true);
//
//        //SUT
//        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_directSaleFactory, _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);
//
//        // Act + Assert
//        IllegalStateException ex = assertThrows(
//                IllegalStateException.class,
//                () -> controller.putItemIdOnDirectSale(_itemsId, _priceDouble, _timeLimitDouble)
//        );
//        assertTrue(ex.getMessage().contains("Direct sale already exists"));
//
//    }
//}
