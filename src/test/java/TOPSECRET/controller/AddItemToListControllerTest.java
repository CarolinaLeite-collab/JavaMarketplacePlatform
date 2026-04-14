
package TOPSECRET.controller;

import TOPSECRET.domain.repository.IListOfItemsRepo;
import TOPSECRET.domain.listofitems.ListOfItems;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddItemToListControllerTest {

    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private ILibraryRepo _iLibraryRepoDouble;
    private UserId _userIdDouble;
    private GenreId _genreIdDouble;
    private ItemId _itemIdDouble;
    private Library _libraryDouble;
    private ListOfItems _listDouble;

    @BeforeEach
    void setUp() {
        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _userIdDouble = mock(UserId.class);
        _genreIdDouble = mock(GenreId.class);
        _itemIdDouble = mock(ItemId.class);
        _libraryDouble = mock(Library.class);
        _listDouble = mock(ListOfItems.class);
    }

    @Test
    void getMyListsShouldReturnsListsFromRepo() {
        //arrange
        List<ListOfItems> expected = List.of(_listDouble);
        when(_iListOfItemsRepoDouble.findListsByUserId(_userIdDouble)).thenReturn(expected);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //act
        List<ListOfItems> result = _controllerSUT.getMyLists(_userIdDouble);

        //assert
        assertSame(expected, result);
    }

    @Test
    void getItemsInMyLibraryShouldReturnItemsList() {
        //arrange
        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //act
        List<ItemId> result = _controllerSUT.getItemsInMyLibrary(_userIdDouble);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void getItemsInMyLibraryShouldThrowWhenUserLibraryNotFound() {
        //arrange
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble))
                .thenThrow(new IllegalStateException("Library not found"));

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        assertThrows(IllegalStateException.class,
                () -> _controllerSUT.getItemsInMyLibrary(_userIdDouble));
    }

    @Test
    void addItemToList_throwsWhenListNameIsBlank() {
        //arrange / SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        assertThrows(IllegalArgumentException.class,
                () -> _controllerSUT.addItemToList(_userIdDouble, " ", _genreIdDouble, _itemIdDouble));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        //arrange
        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userIdDouble, "My List", _genreIdDouble))
                .thenReturn(_listDouble);

        doThrow(new IllegalStateException("Item already in list"))
                .when(_listDouble)
                .addItem(_itemIdDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controllerSUT.addItemToList(_userIdDouble, "My List", _genreIdDouble, _itemIdDouble)
        );

        assertEquals("Item already in list", ex.getMessage());
    }

}