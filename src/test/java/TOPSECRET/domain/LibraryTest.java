package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryTest {

    private User _userDouble;
    private Item _itemDouble;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _itemDouble = mock(Item.class);

    }

    @Test
    void testConstructor() {

        //SUT
        new Library(_userDouble);

    }

    @Test
    void belongsTo_shouldReturnTrueWhenUserIsOwner() {
        // Arrange / SUT
        Library library = new Library(_userDouble);

        // Act & Assert
        assertTrue(library.belongsTo(_userDouble));
    }

    @Test
    void belongsTo_shouldReturnFalseWhenUserIsNotOwner() {
        // Arrange
        User otherUserDouble = mock(User.class);

        //SUT
        Library library = new Library(_userDouble);

        // Act & Assert
        assertFalse(library.belongsTo(otherUserDouble));
    }

    @Test
    void ifUserIsNull_shouldReturnFalse() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(null));
    }

    @Test
    void test_get_userID() {

        //arrange / SUT
        Library myLibrary = new Library(_userDouble);

        //assert
        assertEquals(_userDouble,myLibrary.getUser());
    }

    @Test
    void getItemsInLibraryShouldReturnEmptyListWhenNoItemsExist() {
        // Arrange
        Library library = new Library(_userDouble);

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
        Library library = new Library(_userDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_itemDouble));
    }

    @Test
    void getItemsInLibraryShouldReturnItemsWhenItemsExist() {
        // Arrange / SUT
        Library library = new Library(_userDouble);
        library.addItemToLibrary(_itemDouble);

        // Act
        List<Item> result = library.getItemsInLibrary();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addItemToLibraryShouldAddItemWhenValid() {
        // Arrange / SUT
        Library library = new Library(_userDouble);

        // Act
        boolean result = library.addItemToLibrary(_itemDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsNull() {
        // Arrange / SUT
        Library library = new Library(_userDouble);

        // Act
        boolean result = library.addItemToLibrary(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void addItemToLibraryShouldReturnFalseWhenItemIsAlreadyInLibrary() {
        // Arrange
        Library library = new Library(_userDouble);

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

        //SUT
        Library library = new Library(_userDouble);

        //Act
        library.addItemToLibrary(_itemDouble);
        library.addItemToLibrary(_itemDouble2);

        Item itemResult = library.getItem(_itemDouble);

        //Assert
        assertEquals(_itemDouble, itemResult);

    }

    @Test
    void getItemsInLibraryShouldReturnNullWhenNoItemFound() {

        // Arrange
        Item _itemDouble2 = mock(Item.class);
        Item _itemDouble3 = mock(Item.class);

        //SUT
        Library library = new Library(_userDouble);

        //Act
        library.addItemToLibrary(_itemDouble);
        library.addItemToLibrary(_itemDouble2);

        Item itemResult = library.getItem(_itemDouble3);

        //Assert
        assertNull(itemResult);
    }

    @Test
    void getPublicationDetailsShouldReturnPublicationDetailsOfItem() {

        //Arrange
        Item _itemDouble2 = mock(Item.class);

        when(_itemDouble.get_publication()).thenReturn(mock(Publication.class));
        when(_itemDouble.get_publication().getTitle()).thenReturn(mock(Title.class));
        when(_itemDouble.get_publication().getAuthor()).thenReturn(mock(Author.class));
        when(_itemDouble.get_publication().getPublicationType()).thenReturn(mock(PublicationType.class));
//        when(_itemDouble.get_publication().getIdentifier()).thenReturn(mock(Identifier.class));

        when(_itemDouble2.get_publication()).thenReturn(mock(Publication.class));
        when(_itemDouble2.get_publication().getTitle()).thenReturn(mock(Title.class));
        when(_itemDouble2.get_publication().getAuthor()).thenReturn(mock(Author.class));
        when(_itemDouble2.get_publication().getPublicationType()).thenReturn(mock(PublicationType.class));
//        when(_itemDouble2.get_publication().getIdentifier()).thenReturn(mock(Identifier.class));

        //SUT
        Library library = new Library(_userDouble);

        //act
        library.addItemToLibrary(_itemDouble);
        library.addItemToLibrary(_itemDouble2);

        List<PublicationDetails> result = library.getItemDetails();

        //assert
        assertEquals(2, result.size());

    }

}