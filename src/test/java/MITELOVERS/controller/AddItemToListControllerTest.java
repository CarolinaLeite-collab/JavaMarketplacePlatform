package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AddItemToListControllerTest {

    @MockBean
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @MockBean
    ILibraryRepo _iLibraryRepoDouble;

    @MockBean
    UserId _userIdDouble;

    @InjectMocks
    AddItemToListController _controller;

    private GenreId _genreIdDouble;
    private ItemId _itemIdDouble;
    private Library _libraryDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() throws InstantiationException {
        MockitoAnnotations.openMocks(this);

        _genreIdDouble = mock(GenreId.class);
        _itemIdDouble = mock(ItemId.class);
        _libraryDouble = mock(Library.class);
        _nameDouble = new Name("My List");
    }

    @Test
    void getMyListsShouldReturnOnlyListsBelongingToUser() {
        // Arrange
        UserId otherUser = mock(UserId.class);
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.getUserId()).thenReturn(_userIdDouble);
        when(list2.getUserId()).thenReturn(otherUser);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list1, list2));

        // SUT
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
        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListsByUserId(null));
    }

    @Test
    void getMyListsShouldReturnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getMyLists(_userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void getItemsInMyLibraryShouldReturnItemsList() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble)).thenReturn(libraryIdDouble);
            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble)).thenReturn(Optional.of(_libraryDouble));
            when(_libraryDouble.getItemsIdInLibrary()).thenReturn(List.of(_itemIdDouble));

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
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {
            mocked.when(() -> LibraryId.fromUserId(_userIdDouble)).thenReturn(libraryIdDouble);
            when(_iLibraryRepoDouble.ofIdentity(libraryIdDouble)).thenReturn(Optional.empty());

            // SUT
            AddItemToListController controller =
                    new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

            // Act & Assert
            assertThrows(IllegalStateException.class,
                    () -> controller.getItemsInMyLibrary(_userIdDouble));
        }
    }

    @Test
    void addItemToListShouldThrowWhenListNameIsNull() {
        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addItemToList(_userIdDouble, null, _genreIdDouble, _itemIdDouble));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        // Arrange
        ListOfItems matchingList = mock(ListOfItems.class);
        when(matchingList.getUserId()).thenReturn(_userIdDouble);
        when(matchingList.getName()).thenReturn(_nameDouble);
        when(matchingList.getGenreId()).thenReturn(_genreIdDouble);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(matchingList));
        doThrow(new IllegalStateException("Item already in list"))
                .when(matchingList).addItem(_itemIdDouble);

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble));
        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble));
    }

    @Test
    void addItemToListShouldSaveListAfterAddingItem() {
        // Arrange
        ListOfItems matchingList = mock(ListOfItems.class);
        when(matchingList.getUserId()).thenReturn(_userIdDouble);
        when(matchingList.getName()).thenReturn(_nameDouble);
        when(matchingList.getGenreId()).thenReturn(_genreIdDouble);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(matchingList));

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act
        controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble);

        // Assert
        verify(matchingList).addItem(_itemIdDouble);
        verify(_iListOfItemsRepoDouble).save(matchingList);
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnNullWhenGenreDiffers() {
        // Arrange
        GenreId otherGenre = mock(GenreId.class);
        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn(_nameDouble);
        when(list.getGenreId()).thenReturn(otherGenre);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.findByOwnerNameAndGenre(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertNull(result);
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnNullWhenNameDiffers() {
        // Arrange
        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn(new Name("Other Name"));
        when(list.getGenreId()).thenReturn(_genreIdDouble);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.findByOwnerNameAndGenre(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertNull(result);
    }

    @Test
    void findByOwnerNameAndGenreShouldThrowWhenUserIdIsNull() {
        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findByOwnerNameAndGenre(null, _nameDouble, _genreIdDouble));
    }

    @Test
    void findByOwnerNameAndGenreShouldThrowWhenNameIsNull() {
        // SUT
        AddItemToListController controller =
                new AddItemToListController(_iListOfItemsRepoDouble, _iLibraryRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findByOwnerNameAndGenre(_userIdDouble, null, _genreIdDouble));
    }

}