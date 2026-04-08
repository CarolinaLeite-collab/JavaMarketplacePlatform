package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private UserId _userIdDouble;
    private Library _libraryDouble;
    private List<Item> _items;
    private Item _itemDouble;
    private Price _priceDouble;
    private Period _timeLimitDouble;
//    private User _userIdDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _userIdDouble = mock(UserId.class);
        _libraryDouble = mock(Library.class);
        _items = new ArrayList<>();
        _itemDouble = mock(Item.class);
        _priceDouble = mock(Price.class);
        _timeLimitDouble = Period.ofDays(30);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void testConstructorPublicationInLibraryForDirectSaleController() {
        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble));
    }

    @Test
    void testGetItemsInLibraryForUserWithoutLibraryByUser() {
        //Arrange
        UserId _userIdDouble2 = mock(UserId.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble2))
                .thenThrow(new IllegalStateException("Library not found for user"));

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                controller.getItemsInLibraryByUser(_userIdDouble2));
    }

    @Test
    void testGetItemsInLibraryForUserWithEmptyLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of());

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<Item> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetItemsInLibraryForUserWithItemsInLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        _items.add(_itemDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(_items);
        when(_iLibraryRepoDouble.getItemsInLibraryByUserId(_userIdDouble)).thenReturn(_items);

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<Item> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertEquals(_itemDouble, result.get(0));
    }

    @Test
    void testGetItemsInLibraryByUserListIsImmutable() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<Item> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void testAddItemForDirectSaleSuccess() {
        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        when(_iDirectSaleRepoDouble.addDirectSale(_items, _priceDouble, _timeLimitDouble))
                .thenReturn(_directSaleDouble);

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        DirectSale result = controller.addItemForDirectSale(_items, _priceDouble, _timeLimitDouble);

        //Assert
        assertNotNull(result);
        assertEquals(_directSaleDouble, result);
        verify(_iDirectSaleRepoDouble).addDirectSale(_items, _priceDouble, _timeLimitDouble);
    }

    @Test
    void testAddItemForDirectSaleWhenItemAlreadyInDirectSale() {
        _items.add(_itemDouble);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItem(_itemDouble)).thenReturn(_itemDouble);

        when(_iDirectSaleRepoDouble.addDirectSale(_items, _priceDouble, _timeLimitDouble))
                .thenThrow(new IllegalStateException("Item is already in a direct sale."));

        PublicationInLibraryForDirectSaleController controller =
                new PublicationInLibraryForDirectSaleController(
                        _iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        assertThrows(IllegalStateException.class, () ->
                controller.addItemForDirectSale(_items, _priceDouble, _timeLimitDouble));
    }
}