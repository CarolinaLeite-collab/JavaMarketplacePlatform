package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LibraryTest {

    private User _userDouble;
    private Publication _publicationDouble;

    // Creating doubles for User and Publication classes
    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _publicationDouble = mock(Publication.class);

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
    void getPublicationsInLibraryShouldReturnEmptyListWhenNoPublications() {
        // Arrange
        Library library = new Library(_userDouble);

        // Act
        List<PublicationDetails> result = library.getPublicationsInLibrary();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublicationsInLibraryShouldReturnPublicationsDetailsWhenPublicationsExist() {

        Library library = new Library(_userDouble);

        library.addPublicationToLibrary(_publicationDouble);

        List<PublicationDetails> result = library.getPublicationsInLibrary();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(_publicationDouble.getTitle(), result.get(0).getTitle());
        assertEquals(_publicationDouble.getAuthor(), result.get(0).getAuthor());
        assertEquals(_publicationDouble.getPublicationType(), result.get(0).getPublicationType());
        assertEquals(_publicationDouble.getIdentifier(), result.get(0).getIdentifier());
    }

    @Test
    void getPublicationInLibraryShouldReturnPublicationWhenItExistInLibrary() {

        Library library = new Library(_userDouble);

        library.addPublicationToLibrary(_publicationDouble);

        Publication myPublicationInLibrary = library.getPublicationFromLibrary(_publicationDouble);
        assertEquals(_publicationDouble, myPublicationInLibrary); // asserts both are equal
        assertSame(_publicationDouble, myPublicationInLibrary); // asserts they are the same object in memory
    }

    @Test
    void getPublicationInLibraryThrowsWhenPublicationNotInLibrary() {

        Library library = new Library(_userDouble);

        // p was never added to user's library
        assertThrows(IllegalArgumentException.class, () -> library.getPublicationFromLibrary(_publicationDouble));

    }

    @Test
    void addPublicationToLibraryShouldAddPublicationWhenValid() {
        // Verifies that a valid publication is successfully added to the library

        // Arrange
        Library library = new Library(_userDouble);

        // Act
        boolean result = library.addPublicationToLibrary(_publicationDouble);

        // Assert
        assertTrue(result);
        assertEquals(1, library.getAllPublications().size());
        assertTrue(library.getAllPublications().contains(_publicationDouble));
    }

    @Test
    void getAllPublicationsShouldReturnEmptyListWhenLibraryIsEmpty() {
        // Verifies that an empty library returns an empty list of publications

        // Arrange
        Library library = new Library(_userDouble);

        // Act
        List<Publication> result = library.getAllPublications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsShouldReturnUnmodifiableList() {
        // Ensures that getAllPublications returns an unmodifiable list

        // Arrange
        Library library = new Library(_userDouble);

        library.addPublicationToLibrary(_publicationDouble);

        // Act
        List<Publication> result = library.getAllPublications();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_publicationDouble));
    }

    @Test
    void addPublicationToLibraryShouldNotAllowDuplicatePublication() {
        // Checks that the library rejects duplicate publications

        // Arrange
        Library library = new Library(_userDouble);

        library.addPublicationToLibrary(_publicationDouble);

        // Act
        boolean result = library.addPublicationToLibrary(_publicationDouble);

        // Assert
        assertFalse(result);
        assertEquals(1, library.getAllPublications().size());
    }

    @Test
    void addPublicationToLibraryShouldReturnFalseWhenPublicationIsNull() {
        // Ensures that null publications are not added to the library

        // Arrange
        Library library = new Library(_userDouble);

        // Act
        boolean result = library.addPublicationToLibrary(null);

        // Assert
        assertFalse(result);
        assertTrue(library.getAllPublications().isEmpty());
    }

    @Test
    void getAllItemsReturnsCopyOfItems() {
        Item _idemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);
        Library _library = new Library(_userDouble);

        _library.addItemToLibrary(_idemDouble1, _userDouble);
        _library.addItemToLibrary(_itemDouble2, _userDouble);

        List<Item> result = _library.getAllItems();

        assertEquals(2, result.size());
        assertTrue(result.contains(_idemDouble1));
        assertTrue(result.contains(_itemDouble2));
    }

    @Test
    void addItemToLibraryAddsItem() {
        Item _itemDouble = mock(Item.class);
        Library _library = new Library(_userDouble);

        boolean result = _library.addItemToLibrary(_itemDouble, _userDouble);

        assertTrue(result);
        assertEquals(1, _library.getAllItems().size());
        assertTrue(_library.getAllItems().contains(_itemDouble));
    }

    @Test
    void addItemToLibraryReturnsFalseWhenItemIsNull() {
        Library _library = new Library(_userDouble);

        boolean result = _library.addItemToLibrary(null, _userDouble);

        assertFalse(result);
        assertTrue(_library.getAllItems().isEmpty());
    }


}