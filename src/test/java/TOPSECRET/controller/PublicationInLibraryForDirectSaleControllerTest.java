package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private PublicationInLibraryForDirectSaleController _controller;
    private ILibraryRepo _iLibraryRepoDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private User _userDouble;
    private Library _libraryDouble;
    private Item _itemDouble;
    private Price _priceDouble;
    private Period _timeLimitDouble;
    private User _userIDDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _userDouble = mock(User.class);
        _libraryDouble = mock(Library.class);
        _itemDouble = mock(Item.class);
        _priceDouble = mock(Price.class);
        _timeLimitDouble = Period.ofDays(30);
        _userIDDouble = mock(User.class);

        _controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIDDouble); //SUT
    }

    @Test
    void testConstructorPublicationInLibraryForDirectSaleController() {
        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIDDouble));
    }

    @Test
    void testGetItemsInLibraryForUserWithoutLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.getItemsInLibraryByUser(_userDouble))
                .thenThrow(new IllegalStateException("Library not found for user"));

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.getItemsInLibraryByUser(_userDouble));
    }

    @Test
    void testGetItemsInLibraryForUserWithEmptyLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of());

        //Act
        List<Item> result = _controller.getItemsInLibraryByUser(_userDouble);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetItemsInLibraryForUserWithItemsInLibraryByUser() {
        //Arrange
        when(_iLibraryRepoDouble.getItemsInLibraryByUser(_userDouble)).thenReturn(List.of(_itemDouble));

        //Act
        List<Item> result = _controller.getItemsInLibraryByUser(_userDouble);

        //Assert
        assertEquals(1, result.size());
        assertEquals(_itemDouble, result.get(0));
    }

    @Test
    void testGetItemsInLibraryByUserListIsImmutable() {
        //Arrange
        when(_iLibraryRepoDouble.getItemsInLibraryByUser(_userDouble)).thenReturn(List.of(_itemDouble));

        //Act
        List<Item> result = _controller.getItemsInLibraryByUser(_userDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Item.class)));
    }

    @Test
    void testAddItemForDirectSaleSuccess() {
        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        when(_iDirectSaleRepoDouble.addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(_directSaleDouble);
        //Act
        DirectSale result = _controller.addItemForDirectSale(_itemDouble, _priceDouble, _timeLimitDouble);

        //Assert
        assertNotNull(result);
        assertEquals(_directSaleDouble, result);
        verify(_iDirectSaleRepoDouble).addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble);
        verify(_itemDouble).setDirectSale(_directSaleDouble);
    }

    @Test
    void testAddItemForDirectSaleWhenItemAlreadyInDirectSale() {
        //Arrange
        when(_iDirectSaleRepoDouble.addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(mock(DirectSale.class));


        doThrow(new IllegalStateException("Item is already in a direct sale."))
                .when(_itemDouble).setDirectSale(any(DirectSale.class));

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.addItemForDirectSale(_itemDouble, _priceDouble, _timeLimitDouble));
    }
}