package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private ILibraryRepo _iLibraryRepoDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private UserId _userIdDouble;
    private Library _libraryDouble;
    private ItemId _itemIdDouble;
    private Price _priceDouble;
    private Period _timeLimitDouble;

    @BeforeEach
    void setUp() {
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _userIdDouble = mock(UserId.class);
        _libraryDouble = mock(Library.class);
        _itemIdDouble = mock(ItemId.class);
        _priceDouble = mock(Price.class);
        _timeLimitDouble = Period.ofDays(30);
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
        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of());

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

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
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

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
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getItemsInLibraryByUser(_userIdDouble);

        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
    }

    @Test
    void testAddItemIdForDirectSaleSuccess() {
        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemId(_itemIdDouble)).thenReturn(_itemIdDouble);
        when(_iDirectSaleRepoDouble.addDirectSale(_itemIdDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(_directSaleDouble);

        //SUT
        PublicationInLibraryForDirectSaleController controller = new PublicationInLibraryForDirectSaleController(_iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        DirectSale result = controller.addItemIdForDirectSale(_itemIdDouble, _priceDouble, _timeLimitDouble);

        //Assert
        assertNotNull(result);
        assertSame(_directSaleDouble, result);
        verify(_iDirectSaleRepoDouble).addDirectSale(_itemIdDouble, _priceDouble, _timeLimitDouble);
    }

    @Test
    void testAddItemIdForDirectSaleInvokesSetDirectSaleOnItemId() {
        //Arrange
        DirectSale directSaleDouble = mock(DirectSale.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemId(_itemIdDouble)).thenReturn(_itemIdDouble);
        when(_iDirectSaleRepoDouble.addDirectSale(_itemIdDouble, _priceDouble, _timeLimitDouble))
                .thenReturn(directSaleDouble);

        PublicationInLibraryForDirectSaleController controller =
                new PublicationInLibraryForDirectSaleController(
                        _iLibraryRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        controller.addItemIdForDirectSale(_itemIdDouble, _priceDouble, _timeLimitDouble);

        //Assert
        verify(_itemIdDouble).setDirectSale(directSaleDouble);
    }
}
