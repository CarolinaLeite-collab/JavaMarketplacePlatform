
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
    private LibraryId _libraryIdDouble;
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
        _libraryIdDouble = LibraryId.fromUserId(_userIdDouble);
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
