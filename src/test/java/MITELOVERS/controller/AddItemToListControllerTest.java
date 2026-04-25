
package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

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


    // -------------------------------
    // getMyLists / findListsByUserId
    // -------------------------------

    @Test
    void getMyListsShouldReturnOnlyListsBelongingToUser() {
        // Arrange
        UserId otherUser = mock(UserId.class);

        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.getUserId()).thenReturn(_userIdDouble);
        when(list2.getUserId()).thenReturn(otherUser);

        when(_iListOfItemsRepoDouble.findAll())
                .thenReturn(List.of(list1, list2));

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getMyLists(_userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        assertThrows(IllegalArgumentException.class,
                () -> controller.findListsByUserId(null));
    }

    @Test
    void getMyListsShouldReturnEmptyListWhenUserHasNoLists() {
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        List<ListOfItems> result = controller.getMyLists(_userIdDouble);

        assertTrue(result.isEmpty());
        verify(_iListOfItemsRepoDouble).findAll();
    }

    // --------------------
    // getItemsInMyLibrary
    // --------------------

    @Test
    void getItemsInMyLibraryShouldReturnItemsList() {
        //Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.of(_libraryDouble));

            when(_libraryDouble.getItemsIdInLibrary())
                    .thenReturn(List.of(_itemIdDouble));

            // SUT
            AddItemToListController controller =
                    new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

            // Act
            List<ItemId> result = controller.getItemsInMyLibrary(_userIdDouble);

            // Assert
            assertEquals(1, result.size());
            assertEquals(_itemIdDouble, result.get(0));
        }
    }

    @Test
    void getItemsInMyLibraryShouldThrowWhenUserLibraryNotFound() {

        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            // Arrange: control static mapping
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(libraryIdDouble);

            // Arrange: repo returns empty (NOT throws)
            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble))
                    .thenReturn(Optional.empty());

            // SUT (constructor likely triggers lookup)
            AddItemToListController controller =
                    new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

            // Act + Assert (constructor OR method depending on design)
            assertThrows(IllegalStateException.class, () ->
                    controller.getItemsInMyLibrary(_userIdDouble));
        }
    }

    // --------------
    // addItemToList
    // --------------

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
        // Arrange
        ListOfItems matchingList = mock(ListOfItems.class);

        when(matchingList.getUserId()).thenReturn(_userIdDouble);
        when(matchingList.getName()).thenReturn("My List");
        when(matchingList.getGenreId()).thenReturn(_genreIdDouble);

        // repo now returns ALL lists, controller filters
        when(_iListOfItemsRepoDouble.findAll())
                .thenReturn(List.of(matchingList));

        // simulate domain rule violation
        doThrow(new IllegalStateException("Item already in list"))
                .when(matchingList)
                .addItem(_itemIdDouble);

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> controller.addItemToList(_userIdDouble, "My List", _genreIdDouble, _itemIdDouble)
        );

        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        assertThrows(IllegalStateException.class,
                () -> controller.addItemToList(_userIdDouble, "My List", _genreIdDouble, _itemIdDouble));
    }

    @Test
    void findByOwnerNameAndGenreShouldIgnoreCase() {
        ListOfItems list = mock(ListOfItems.class);

        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn("My List");
        when(list.getGenreId()).thenReturn(_genreIdDouble);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        ListOfItems result = controller.findByOwnerNameAndGenre(_userIdDouble, "my list", _genreIdDouble);

        assertSame(list, result);
    }

    @Test
    void findByOwnerNameAndGenreShouldTrimName() {
        ListOfItems list = mock(ListOfItems.class);

        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn("My List");
        when(list.getGenreId()).thenReturn(_genreIdDouble);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        ListOfItems result = controller.findByOwnerNameAndGenre(_userIdDouble, "  My List  ", _genreIdDouble);

        assertSame(list, result);
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnNullWhenGenreDiffers() {
        GenreId otherGenre = mock(GenreId.class);

        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn("My List");
        when(list.getGenreId()).thenReturn(otherGenre);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        ListOfItems result = controller.findByOwnerNameAndGenre(_userIdDouble, "My List", _genreIdDouble);

        assertNull(result);
    }
}
