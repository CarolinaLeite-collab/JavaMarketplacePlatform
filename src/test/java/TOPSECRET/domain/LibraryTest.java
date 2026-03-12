package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link Library}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link User} — mocked dummy (structural input, owner identity)</li>
 *   <li>{@link Publication} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Item} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class LibraryTest {

    private User _userDouble;
    private Publication _publicationDouble;
    private Item _itemDouble;

    // Creating doubles for User and Publication classes
    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _publicationDouble = mock(Publication.class);
        _itemDouble = mock(Item.class);

    }

    @Test
    void testConstructor() {

        new Library(_userDouble);

    }

    @Test
    void belongsTo_shouldReturnTrueWhenUserIsOwner() {
        // Arrange
        Library library = new Library(_userDouble);

        // Act & Assert
        assertTrue(library.belongsTo(_userDouble));
    }

    @Test
    void belongsTo_shouldReturnFalseWhenUserIsNotOwner() {
        // Arrange
        User otherUserDouble = mock(User.class);
        Library library = new Library(_userDouble);

        // Act & Assert
        assertFalse(library.belongsTo(otherUserDouble));
    }

    @Test
    void test_get_userID() {

        //arrange and act
        Library myLibrary = new Library(_userDouble);
        User userID = myLibrary.getUser();

        //assert
        assertEquals(userID,myLibrary.getUser());

    }

    @Test
    void getItemsInLibraryShouldReturnEmptyListWhenNoItemsExist() {
        // Arrange
        Library library = new Library(_userDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getItemsInLibraryShouldReturnUnmodifiableList() {
        // Arrange
        Library library = new Library(_userDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void getItemsInLibraryShouldReturnItemsWhenItemsExist() {
        // Arrange
        Library library = new Library(_userDouble);
        library.addItemToLibrary(_itemDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addItemToLibraryShouldAddItemWhenValid() {
        // Arrange
        Library library = new Library(_userDouble);

        // Act
        boolean result = library.addItemToLibrary(_itemDouble);

        // Assert
        assertEquals(1, library.getItemsInLibrary().size());
    }

}