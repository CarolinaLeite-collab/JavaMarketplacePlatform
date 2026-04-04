package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.ListOfItems;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
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
    private Genre _genreDouble;
    private Item _itemDouble;
    private Library _libraryDouble;
    private ListOfItems _itemsListDouble;

    @BeforeEach
    void setUp() {
        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _userIdDouble = mock(UserId.class);
        _genreDouble = mock(Genre.class);
        _itemDouble = mock(Item.class);
        _libraryDouble = mock(Library.class);
        _itemsListDouble = mock(ListOfItems.class);
    }

    @Test
    void getMyListsShouldReturnsListsFromRepo() {
        //arrange
        List<ListOfItems> expected = List.of(_itemsListDouble);
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
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //act
        List<Item> result = _controllerSUT.getItemsInMyLibrary(_userIdDouble);

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
    void addItemToListDoesNotFindItemInLibrary() {
        //arrange
        Item otherItem = mock(Item.class);

        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userIdDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(otherItem));

        //SUT
        AddItemToListController controller = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //act
        controller.addItemToList(_userIdDouble, "My List", _genreDouble, _itemDouble);

        // assert
        verify(_itemsListDouble).addItem(null);
    }

    @Test
    void addItemToListShouldAddItemWhenValid() {
        //arrange
        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userIdDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        assertDoesNotThrow(() ->
                _controllerSUT.addItemToList(_userIdDouble, "My List", _genreDouble, _itemDouble)
        );
    }

    @Test
    void addItemToList_throwsWhenListNameIsBlank() {
        //arrange / SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        assertThrows(IllegalArgumentException.class,
                () -> _controllerSUT.addItemToList(_userIdDouble, " ", _genreDouble, _itemDouble));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        //arrange
        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userIdDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        doThrow(new IllegalStateException("Item already in list"))
                .when(_itemsListDouble)
                .addItem(_itemDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        //assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controllerSUT.addItemToList(_userIdDouble, "My List", _genreDouble, _itemDouble)
        );

        assertEquals("Item already in list", ex.getMessage());
    }

}