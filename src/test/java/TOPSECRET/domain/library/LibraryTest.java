package TOPSECRET.domain.library;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.LibraryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LibraryTest {

    private LibraryId _libraryIdDouble;
    private Item _itemDouble;

    @BeforeEach
    void setUp() {

        _libraryIdDouble = mock(LibraryId.class);
        _itemDouble = mock(Item.class);

    }

    @Test
    void testConstructor() {

        // SUT
        new Library(_libraryIdDouble);

    }

    @Test
    void sameAsShouldReturnTrueWhenLibrariesAreSame() {
        // Arrange
        Library library1 = new Library(_libraryIdDouble);
        Library library2 = new Library(_libraryIdDouble);

        // Act & Assert
        assertTrue(library1.sameAs(library2));

    }

    @Test
    void sameAsShouldReturnFalseWhenLibrariesAreNotSame() {
        // Arrange
        LibraryId otherLibraryIdDouble = mock(LibraryId.class);

        // SUT
        Library library = new Library(_libraryIdDouble);

        // Act & Assert
        assertFalse(library.sameAs(otherLibraryIdDouble));
    }

    @Test
    void libraryShouldNotBeEqualsWithNull() {

        // SUT
        Library libraryDouble = new Library(_libraryIdDouble);

        //Assert
        assertNotEquals(libraryDouble, null);

    }

    @Test
    void libraryShouldBeSameAsWithTheSameObject() {

        // SUT
        Library libraryDouble = new Library(_libraryIdDouble);

        // Assert
        assertTrue(libraryDouble.sameAs(libraryDouble));
    }

    @Test
    void libraryShouldNotBeSameAsWithDifferentObject() {

        // SUT
        Library libraryDouble = new Library(_libraryIdDouble);
        String otherObject = "I am another object type";

        // Assert
        assertFalse(libraryDouble.sameAs(otherObject));
    }

    @Test
    void librariesShouldBeSameAsWhenLibraryIdIsEqual() {

        // SUT
        Library library1Double = new Library(_libraryIdDouble);
        Library library2Double = new Library(_libraryIdDouble);

        // Assert
        assertTrue(library1Double.sameAs(library2Double));
    }

    @Test
    void librariesShouldNotBeSameAsWhenLibraryIdIsDifferent() {

        // SUT
        LibraryId libraryId2Double = mock(LibraryId.class);
        Library library1Double = new Library(_libraryIdDouble);
        Library library2Double = new Library(libraryId2Double);

        // Assert
        assertFalse(library1Double.sameAs(library2Double));
    }

    @Test
    void ifUserIsNull_shouldReturnFalse() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(null));

    }

    @Test
    void test_get_identity() {

        // Arrange / SUT
        Library myLibrary = new Library(_libraryIdDouble);

        // Assert
        assertEquals(_libraryIdDouble,myLibrary.identity());
    }

    @Test
    void getItemsInLibraryShouldReturnEmptyListWhenNoItemsExist() {
        // Arrange
        Library library = new Library(_libraryIdDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void getItemsInLibraryShouldReturnUnmodifiableList() {
        // Arrange
        Library library = new Library(_libraryIdDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void getItemsInLibraryShouldReturnItemsWhenItemsExist() {
        // Arrange / SUT
        Library library = new Library(_libraryIdDouble);
        library.addItemToLibrary(_itemDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addItemToLibraryShouldAddItemWhenValid() {
        // Arrange / SUT
        Library library = new Library(_libraryIdDouble);

        // Act
        boolean result = library.addItemToLibrary(_itemDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsNull() {
        // Arrange / SUT
        Library library = new Library(_libraryIdDouble);

        // Act
        boolean result = library.addItemToLibrary(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsAlreadyInLibrary() {
        // Arrange
        Library library = new Library(_libraryIdDouble);

        // Act
        library.addItemToLibrary(_itemDouble);
        boolean result = library.addItemToLibrary(_itemDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void getItemsInLibraryShouldReturnItemWhenItemFound() {
        // Arrange
        Item _itemDouble2 = mock(Item.class);

        // SUT
        Library library = new Library(_libraryIdDouble);

        // Act
        library.addItemToLibrary(_itemDouble);
        library.addItemToLibrary(_itemDouble2);

        Item itemResult = library.getItem(_itemDouble);

        // Assert
        assertEquals(_itemDouble, itemResult);

    }

    @Test
    void getItemsInLibraryShouldReturnNullWhenNoItemFound() {

        // Arrange
        Item _itemDouble2 = mock(Item.class);
        Item _itemDouble3 = mock(Item.class);

        // SUT
        Library library = new Library(_libraryIdDouble);

        // Act
        library.addItemToLibrary(_itemDouble);
        library.addItemToLibrary(_itemDouble2);

        Item itemResult = library.getItem(_itemDouble3);

        // Assert
        assertNull(itemResult);
    }

//    @Test
//    void getPublicationDetailsShouldReturnPublicationDetailsOfItem() {
//
//        // Arrange
//        Item _itemDouble2 = mock(Item.class);
//
//        when(_itemDouble.get_publication()).thenReturn(mock(Publication.class));
//        when(_itemDouble.get_publication().getTitle()).thenReturn(mock(Title.class));
//        when(_itemDouble.get_publication().getAuthor()).thenReturn(mock(AuthorId.class));
//        when(_itemDouble.get_publication().getPublicationType()).thenReturn(mock(PublicationTypeId.class));
//        when(_itemDouble.get_publication().getIdentifier()).thenReturn(mock(EditionId.class));
//
//        when(_itemDouble2.get_publication()).thenReturn(mock(Publication.class));
//        when(_itemDouble2.get_publication().getTitle()).thenReturn(mock(Title.class));
//        when(_itemDouble2.get_publication().getAuthor()).thenReturn(mock(AuthorId.class));
//        when(_itemDouble2.get_publication().getPublicationType()).thenReturn(mock(PublicationTypeId.class));
//        when(_itemDouble2.get_publication().getIdentifier()).thenReturn(mock(Identifier.class));
//
//        // SUT
//        Library library = new Library(_libraryIdDouble);
//
//        // Act
//        library.addItemToLibrary(_itemDouble);
//        library.addItemToLibrary(_itemDouble2);
//
//        List<PublicationDetails> result = library.getItemDetails();
//
//        // Assert
//        assertEquals(2, result.size());
//
//    }
}