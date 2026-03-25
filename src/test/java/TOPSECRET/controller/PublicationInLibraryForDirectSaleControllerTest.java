package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private PublicationInLibraryForDirectSaleController _controller;
    private ILibraryRepo _libraryRepoDouble;
    private IDirectSaleRepo _directSaleRepoDouble;
    private User _userDouble;
    private Library _libraryDouble;
    private Item _itemDouble;
    private Price _priceDouble;
    private Period _timeLimitDouble;
    private User _userID;

    @BeforeEach
    void setUp() {
        _libraryRepoDouble = mock(ILibraryRepo.class);
        _directSaleRepoDouble = mock(IDirectSaleRepo.class);
        _userDouble = mock(User.class);
        _libraryDouble = mock(Library.class);
        _itemDouble = mock(Item.class);
        _priceDouble = mock(Price.class);
        _timeLimitDouble = Period.ofDays(30);
        _userID = mock(User.class);

        _controller = new PublicationInLibraryForDirectSaleController(_libraryRepoDouble, _directSaleRepoDouble, _userID); //SUT
    }

    @Test
    void testConstructorPublicationInLibraryForDirectSaleController() {
        //Act + Assert
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(_libraryRepoDouble, _directSaleRepoDouble, _userID));
    }

    @Test
    void testGetItemsInLibraryForUserWithoutLibraryByUser() {
        //Arrange
        when(_libraryRepoDouble.getItemsInLibraryByUser(_userDouble))
                .thenThrow(new IllegalStateException("Library not found for user"));

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.getItemsInLibraryByUser(_userDouble));
    }

    @Test
    void testGetItemsInLibraryForUserWithEmptyLibraryByUser() {
        //Arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
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
        when(_libraryRepoDouble.getItemsInLibraryByUser(_userDouble)).thenReturn(List.of(_itemDouble));

        //Act
        List<Item> result = _controller.getItemsInLibraryByUser(_userDouble);

        //Assert
        assertEquals(1, result.size());
        assertEquals(_itemDouble, result.get(0));
    }

    @Test
    void testGetItemsInLibraryByUserListIsImmutable() {
        //Arrange
        when(_libraryRepoDouble.getItemsInLibraryByUser(_userDouble)).thenReturn(List.of(_itemDouble));

        //Act
        List<Item> result = _controller.getItemsInLibraryByUser(_userDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Item.class)));
    }

    @Test
    void testAddItemForDirectSaleSuccess() {
        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        when(_directSaleRepoDouble.addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(_directSaleDouble);
        //Act
        DirectSale result = _controller.addItemForDirectSale(_itemDouble, _priceDouble, _timeLimitDouble);

        //Assert
        assertNotNull(result);
        assertEquals(_directSaleDouble, result);
        verify(_directSaleRepoDouble).addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble);
        verify(_itemDouble).setDirectSale(_directSaleDouble);
    }

    @Test
    void testAddItemForDirectSaleWhenItemAlreadyInDirectSale() {
        //Arrange
        when(_directSaleRepoDouble.addDirectSale(_itemDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(mock(DirectSale.class));


        doThrow(new IllegalStateException("Item is already in a direct sale."))
                .when(_itemDouble).setDirectSale(any(DirectSale.class));

        //Act + Assert
        assertThrows(IllegalStateException.class, () ->
                _controller.addItemForDirectSale(_itemDouble, _priceDouble, _timeLimitDouble));
    }
}