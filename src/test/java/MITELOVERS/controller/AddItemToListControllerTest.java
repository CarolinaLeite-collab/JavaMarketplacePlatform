package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddItemToListControllerTest {

    @Mock
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @Mock
    ILibraryRepo _iLibraryRepoDouble;

    @Mock
    UserId _userIdDouble;

    @InjectMocks
    AddItemToListController _controller;

    private GenreId _genreIdDouble;
    private ItemId _itemIdDouble;
    private Library _libraryDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

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

        // Act
        List<ListOfItems> result = _controller.getMyLists(_userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.findListsByUserId(null));
    }

    @Test
    void getMyListsShouldReturnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<ListOfItems> result = _controller.getMyLists(_userIdDouble);

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

            // Act
            List<ItemId> result = _controller.getItemsInMyLibrary(_userIdDouble);

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

            // Act & Assert
            assertThrows(IllegalStateException.class,
                    () -> _controller.getItemsInMyLibrary(_userIdDouble));
        }
    }

    @Test
    void addItemToListShouldThrowWhenListNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addItemToList(_userIdDouble, null, _genreIdDouble, _itemIdDouble));
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

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble));
        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble));
    }

    @Test
    void addItemToListShouldSaveListAfterAddingItem() {
        // Arrange
        ListOfItems matchingList = mock(ListOfItems.class);
        when(matchingList.getUserId()).thenReturn(_userIdDouble);
        when(matchingList.getName()).thenReturn(_nameDouble);
        when(matchingList.getGenreId()).thenReturn(_genreIdDouble);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(matchingList));

        // Act
        _controller.addItemToList(_userIdDouble, _nameDouble, _genreIdDouble, _itemIdDouble);

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

        // Act
        ListOfItems result = _controller.findByOwnerNameAndGenre(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertNull(result);
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnNullWhenNameDiffers() {
        // Arrange
        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userIdDouble);
        when(list.getName()).thenReturn(new Name("Other Name"));
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        // Act
        ListOfItems result = _controller.findByOwnerNameAndGenre(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertNull(result);
    }

    @Test
    void findByOwnerNameAndGenreShouldThrowWhenUserIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.findByOwnerNameAndGenre(null, _nameDouble, _genreIdDouble));
    }

    @Test
    void findByOwnerNameAndGenreShouldThrowWhenNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.findByOwnerNameAndGenre(_userIdDouble, null, _genreIdDouble));
    }

}