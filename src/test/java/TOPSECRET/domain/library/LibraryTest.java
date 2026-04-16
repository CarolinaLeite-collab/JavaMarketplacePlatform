package TOPSECRET.domain.library;

import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LibraryTest {

    private UserId _userIdDouble;
    private LibraryId _libraryIdDouble;
    private ItemId _itemIdDouble;
    private Email _emailDouble;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _libraryIdDouble = mock(LibraryId.class);
        _itemIdDouble = mock(ItemId.class);
        _emailDouble = mock(Email.class);

        when(_userIdDouble.getEmail()).thenReturn(_emailDouble);

    }

    @Test
    void testConstructor() {

        // SUT
        new Library(_userIdDouble);

    }

    @Test
    void sameAsShouldReturnTrueWhenLibrariesAreSame() {
        // Arrange
        Library library1 = new Library(_userIdDouble);
        Library library2 = new Library(_userIdDouble);

        // Act & Assert
        assertTrue(library1.sameAs(library2));

    }

    @Test
    void sameAsShouldReturnFalseWhenLibrariesAreNotSame() {
        // Arrange
        LibraryId otherLibraryIdDouble = mock(LibraryId.class);

        // SUT
        Library library = new Library(_userIdDouble);

        // Act & Assert
        assertFalse(library.sameAs(otherLibraryIdDouble));
    }

    @Test
    void libraryShouldNotBeEqualsWithNull() {

        // SUT
        Library libraryDouble = new Library(_userIdDouble);

        //Assert
        assertFalse(libraryDouble.equals(null));

    }

    @Test
    void equalLibrariesShouldHaveSameHashCode() {

        //SUT
        Library library1 = new Library(_userIdDouble);
        Library library2 = new Library(_userIdDouble);

        //Assert
        assertEquals(library1.hashCode(), library2.hashCode());
    }

    @Test
    void differentLibrariesShouldHavedifferentHashCode() {

        //Arrange
        Email emailDouble = mock(Email.class);
        UserId otherUserIdDouble = mock(UserId.class);
        when(otherUserIdDouble.getEmail()).thenReturn(emailDouble);

        //SUT
        Library library1 = new Library(_userIdDouble);
        Library library2 = new Library(otherUserIdDouble);

        //Assert
        assertNotEquals(library1.hashCode(), library2.hashCode());
    }

    @Test
    void libraryShouldBeSameAsWithTheSameObject() {

        // SUT
        Library libraryDouble = new Library(_userIdDouble);

        // Assert
        assertTrue(libraryDouble.sameAs(libraryDouble));
    }

    @Test
    void libraryShouldNotBeSameAsWithDifferentObject() {

        // SUT
        Library libraryDouble = new Library(_userIdDouble);
        String otherObject = "I am another object type";

        // Assert
        assertFalse(libraryDouble.sameAs(otherObject));
    }

    @Test
    void librariesShouldBeSameAsWhenLibraryIdIsEqual() {

        // SUT
        Library library1Double = new Library(_userIdDouble);
        Library library2Double = new Library(_userIdDouble);

        // Assert
        assertTrue(library1Double.sameAs(library2Double));
    }

    @Test
    void librariesShouldNotBeSameAsWhenLibraryIdIsDifferent() {

        // SUT
        UserId userId2Double = mock(UserId.class);
        Library library1Double = new Library(_userIdDouble);
        Library library2Double = new Library(userId2Double);

        // Assert
        assertFalse(library1Double.sameAs(library2Double));
    }

    @Test
    void ifUserIsNullShouldReturnFalse() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(null));

    }

    @Test
    void testGetIdentityShouldReturnId() {

        // Arrange
        when(_userIdDouble.getEmail()).thenReturn(_emailDouble);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userIdDouble))
                    .thenReturn(_libraryIdDouble);

            // SUT
            Library myLibrary = new Library(_userIdDouble);

            // Assert
            assertEquals(_libraryIdDouble, myLibrary.identity());
        }
    }

    @Test
    void getItemsInLibraryShouldReturnEmptyListWhenNoItemsExist() {
        // Arrange
        Library library = new Library(_userIdDouble);

        // Act
        List<ItemId> result = library.getItemsIdInLibrary();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void getItemsInLibraryShouldReturnUnmodifiableList() {
        // Arrange
        Library library = new Library(_userIdDouble);

        // Act
        List<ItemId> result = library.getItemsIdInLibrary();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemIdDouble));
    }

    @Test
    void getItemsInLibraryShouldReturnItemsWhenItemsExist() {
        // Arrange / SUT
        Library library = new Library(_userIdDouble);
        library.addItemIdToLibrary(_itemIdDouble);

        // Act
        List<ItemId> result = library.getItemsIdInLibrary();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addItemToLibraryShouldReturnTrueWhenItemIdisValidAndAdded() {
        // Arrange / SUT
        Library library = new Library(_userIdDouble);

        // Act
        boolean result = library.addItemIdToLibrary(_itemIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void addItemToLibraryShouldAddItemWhenValid() {
        // Arrange / SUT
        Library library = new Library(_userIdDouble);

        // Act
        library.addItemIdToLibrary(_itemIdDouble);

        // Assert
        assertEquals(library.getItemsIdInLibrary().size(), 1);

    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsNull() {
        // Arrange / SUT
        Library library = new Library(_userIdDouble);

        // Act
        boolean result = library.addItemIdToLibrary(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsAlreadyInLibrary() {
        // Arrange
        Library library = new Library(_userIdDouble);

        // Act
        library.addItemIdToLibrary(_itemIdDouble);
        boolean result = library.addItemIdToLibrary(_itemIdDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnTrueWhenItemIdExists() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);

        //SUT
        Library library = new Library(_userIdDouble);

        //Act
        library.addItemIdToLibrary(itemIdDouble);

        boolean result = library.containsItemId(itemIdDouble);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenItemIdDoesNotExist() {

        //Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        //SUT
        Library library = new Library(_userIdDouble);

        //Act
        library.addItemIdToLibrary(itemIdDouble);

        boolean result = library.containsItemId(itemIdDouble2);

        assertFalse(result);
    }
}