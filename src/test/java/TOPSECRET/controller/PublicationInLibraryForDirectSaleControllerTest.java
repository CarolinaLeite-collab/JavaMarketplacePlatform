package TOPSECRET.controller;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.SaleStatus;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testConstructorPublicationInLibraryForDirectSaleController() {
        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble));
    }

    @Test
    void testGetItemsInLibraryForUserWithoutLibraryByUser() {
        //Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2))
                .thenThrow(new IllegalStateException("Library not found for user"));

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                controller.getItemsInLibraryByUser(_userIdDouble2));
    }

    @Test
    void testGetItemsInLibraryForUserWithEmptyLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetItemsInLibraryForUserWithItemsInLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertEquals(_itemIdDouble, result.get(0));
    }

    @Test
    void testGetItemsInLibraryByUserListIsImmutable() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble,_iItemRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
    }

    @Test
    void testPutItemIdOnDirectSaleSuccess() {
        // Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.get_saleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_iDirectSaleRepoDouble.addDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                .thenReturn(directSaleDouble);

        // SUT
        PublicationInLibraryForDirectSaleController controller =
                new PublicationInLibraryForDirectSaleController(
                        _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

        // Act
        DirectSale result = controller.putItemIdOnDirectSale(_iItemRepoDouble, _itemsId, _priceDouble, _timeLimitDouble);

        // Assert
        assertNotNull(result);
        assertSame(directSaleDouble, result);
        verify(_iDirectSaleRepoDouble).addDirectSale(_itemsId, _priceDouble, _timeLimitDouble);
        verify(_itemDouble).markAsDirectSale();
    }

    @Test
    void testPutItemIdOnDirectSaleMarksItemAsDirectSale() {
        // Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.get_saleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_iDirectSaleRepoDouble.addDirectSale(_itemsId, _priceDouble, _timeLimitDouble))
                .thenReturn(directSaleDouble);

        PublicationInLibraryForDirectSaleController controller =
                new PublicationInLibraryForDirectSaleController(
                        _iLibraryRepoDouble, _iDirectSaleRepoDouble, _iItemRepoDouble, _userIdDouble);

        // Act
        controller.putItemIdOnDirectSale(_iItemRepoDouble, _itemsId, _priceDouble, _timeLimitDouble);

        // Assert
        verify(_itemDouble).markAsDirectSale();
    }
}
