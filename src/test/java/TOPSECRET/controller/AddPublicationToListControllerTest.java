package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.IListOfPublicationsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationToListControllerTest {

    private IListOfPublicationsRepo _iListRepoDouble;
    private LibraryRepo _libraryRepoDouble;
    private User _userDouble;
    private Genre _genreDouble;
    private Item _itemDouble;
    private Library _libraryDouble;
    private ListOfPublications _publicationsListDouble;

    @BeforeEach
    void setUp() {
        _iListRepoDouble = mock(IListOfPublicationsRepo.class);
        _libraryRepoDouble = mock(LibraryRepo.class);
        _userDouble = mock(User.class);
        _genreDouble = mock(Genre.class);
        _itemDouble = mock(Item.class);
        _libraryDouble = mock(Library.class);
        _publicationsListDouble = mock(ListOfPublications.class);
    }

    // getMyLists
    @Test
    void getMyListsShouldReturnsListsFromRepo() {
        //arrange
        List<ListOfPublications> expected = List.of(_publicationsListDouble);
        when(_listRepoDouble.findListsByUser(_userDouble)).thenReturn(expected);

        //SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //act
        List<ListOfPublications> result = _controllerSUT.getMyLists(_userDouble);

        //assert
        assertSame(expected, result);
    }

    // getItemsInMyLibrary

    @Test
    void getItemsInMyLibraryShouldReturnItemsList() {
        //arrange
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);

        //SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //act
        List<Item> result = _controllerSUT.getItemsInMyLibrary(_userDouble);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void getItemsInMyLibraryShouldThrowWhenUserLibraryNotFound() {
        //arrange
        when(_libraryRepoDouble.findLibraryByUser(_userDouble))
                .thenThrow(new IllegalStateException("Library not found"));

        //SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //assert
        assertThrows(IllegalStateException.class,
                () -> _controllerSUT.getItemsInMyLibrary(_userDouble));
    }

    @Test
    void addItemToListDoesNotFindItemInLibrary() {
        //arrange
        Item otherItem = mock(Item.class);

        when(_listRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_publicationsListDouble);
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getItemsInLibrary()).thenReturn(List.of(otherItem));

        //SUT
        AddPublicationToListController controller = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //act
        controller.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble);

        // assert
        verify(_publicationsListDouble).addItem(null);
    }


    // addPublicationToList (BOOK + ISBN)

    @Test
    void addItemToListShouldAddItemWhenValid() {
        //arrange
        when(_listRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_publicationsListDouble);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        //SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //assert
        assertDoesNotThrow(() ->
                _controllerSUT.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble)
        );
    }

    // ---------------------
    // Null argument tests
    // ---------------------

    @Test
    void addItemToList_throwsWhenListNameIsBlank() {
        //arrange / SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //assert
        assertThrows(IllegalArgumentException.class,
                () -> _controllerSUT.addItemToList(_userDouble, " ", _genreDouble, _itemDouble));
    }

    // -----------------------
    // Check for duplications
    // -----------------------
    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        //arrange
        when(_listRepoDouble.findByOwnerNameAndGenre(_userDouble, "My List", _genreDouble))
                .thenReturn(_publicationsListDouble);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble))
                .thenReturn(_libraryDouble);

        when(_libraryDouble.getItemsInLibrary())
                .thenReturn(List.of(_itemDouble));

        // Simulate the domain rule: list rejects duplicates
        doThrow(new IllegalStateException("Item already in list"))
                .when(_publicationsListDouble)
                .addItem(_itemDouble);

        //SUT
        AddPublicationToListController _controllerSUT = new AddPublicationToListController(_listRepoDouble, _libraryRepoDouble);

        //assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controllerSUT.addItemToList(_userDouble, "My List", _genreDouble, _itemDouble)
        );

        assertEquals("Item already in list", ex.getMessage());
    }

}