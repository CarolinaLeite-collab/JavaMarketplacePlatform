package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddItemToListControllerTest {

    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private ILibraryRepo _iLibraryRepoDouble;
    private User _userDouble;
    private Genre _genreDouble;
    private Item _itemDouble;
    private Library _libraryDouble;
    private ListOfItems _itemsListDouble;

    @BeforeEach
    void setUp() {
        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _userDouble = mock(User.class);
        _genreDouble = mock(Genre.class);
        _itemDouble = mock(Item.class);
        _libraryDouble = mock(Library.class);
        _itemsListDouble = mock(ListOfItems.class);
    }

    @Test
    void getMyListsShouldReturnsListsFromRepo() {
        //arrange
        List<ListOfItems> expected = List.of(_itemsListDouble);
        when(_iListOfItemsRepoDouble.findListsByUser(_userDouble)).thenReturn(expected);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //act
        List<ListOfItems> result = _controllerSUT.getMyLists(_userDouble);

        //assert
        assertSame(expected, result);
    }

    @Test
    void getItemsInMyLibraryShouldReturnItemsList() {
        //arrange
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));
        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //act
        List<Item> result = _controllerSUT.getItemsInMyLibrary(_userDouble);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void getItemsInMyLibraryShouldThrowWhenUserLibraryNotFound() {
        //arrange
        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble))
                .thenThrow(new IllegalStateException("Library not found"));

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //assert
        assertThrows(IllegalStateException.class,
                () -> _controllerSUT.getItemsInMyLibrary(_userDouble));
    }

    @Test
    void addItemToListDoesNotFindItemInLibrary() {
        //arrange
        Item otherItem = mock(Item.class);

        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);
        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(otherItem));

        //SUT
        AddItemToListController controller = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //act
        controller.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble);

        // assert
        verify(_itemsListDouble).addItem(null);
    }

    @Test
    void addItemToListShouldAddItemWhenValid() {
        //arrange
        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);

        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //assert
        assertDoesNotThrow(() ->
                _controllerSUT.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble)
        );
    }

    @Test
    void addItemToList_throwsWhenListNameIsBlank() {
        //arrange / SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //assert
        assertThrows(IllegalArgumentException.class,
                () -> _controllerSUT.addItemToList(_userDouble, " ", _genreDouble, _itemDouble));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        //arrange
        when(_iListOfItemsRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_itemsListDouble);

        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        doThrow(new IllegalStateException("Item already in list"))
                .when(_itemsListDouble)
                .addItem(_itemDouble);

        //SUT
        AddItemToListController _controllerSUT = new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userDouble);

        //assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controllerSUT.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble)
        );

        assertEquals("Item already in list", ex.getMessage());
    }

}