package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListOfPublicationsTest {

    private User _user1Double;
    private User _user2Double;
    private Genre _genre1Double;
    private Genre _genre2Double;

    @BeforeEach
    void setUp() {
        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
        _genre1Double = mock(Genre.class);
        _genre2Double = mock(Genre.class);
    }

    @Test
    void constructsListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = new ListOfPublications(_user1Double,"My favorite books", _genre1Double);

        // Assert
        assertNotNull(list);
        assertTrue(list.isPrivate());
        assertTrue(list.getItems().isEmpty());

    }

    @Test
    void throwsExceptionWhenUserIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(null, "Lista", _genre1Double));
    }

    @Test
    void throwsExceptionWhenListNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(_user1Double, null, _genre1Double));
    }

    @Test
    void throwsExceptionWhenGenreIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(_user1Double, "Lista", null));
    }


    @Test
    void equalsShouldReturnTrueForSameArguments() {
        //Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "My List", _genre1Double);

        // Act & Assert
        assertEquals(list1, list2);
    }

    @Test
    void hashCodeShouldReturnTrueForSameArguments() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "My List", _genre1Double);

        // Act & Assert
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentUser() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user2Double, "My List", _genre1Double);

        // Act & Assert
        assertNotEquals(list1, list2);
    }

    @Test
    void hashCodeShouldReturnFalseForDifferentUser() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user2Double, "My List", _genre1Double);

        // Act & Assert
        assertNotEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentName() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "Other List", _genre1Double);

        // Act & Assert
        assertNotEquals(list1, list2);
    }

    @Test
    void hashCodeShouldReturnFalseForDifferentName() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "Other List", _genre1Double);

        // Act & Assert
        assertNotEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentGenre() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "My List", _genre2Double);

        // Act & Assert
        assertNotEquals(list1, list2);
    }

    @Test
    void hashCodeShouldReturnFalseForDifferentGenre() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1Double, "My List", _genre1Double);
        ListOfPublications list2 = new ListOfPublications(_user1Double, "My List", _genre2Double);

        // Act & Assert
        assertNotEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "My List", _genre1Double);

        // Act & Assert
        assertNotEquals(list, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "My List", _genre1Double);
        String notAList = "not a ListOfPublications";

        // Act & Assert
        assertNotEquals(list, notAList);
    }

    @Test
    void listShouldBePrivateByDefault() {
        // Arrange & Act
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);

        // Assert
        assertTrue(list.isPrivate());
    }

    @Test
    void makePublicShouldMakeListPublic() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);

        // Act
        list.makePublic();

        // Assert
        assertFalse(list.isPrivate());
    }

    @Test
    void makePublicShouldBeIdempotent() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);

        // Act
        list.makePublic();
        list.makePublic();

        // Assert
        assertFalse(list.isPrivate());
    }

    @Test
    void addItemShouldAddSuccessfully() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista",_genre1Double);
        Item _itemDouble = mock(Item.class);

        // Act
        list.addItem(_itemDouble);

        // Assert
        assertEquals(1, list.getItems().size());
        assertEquals(_itemDouble, list.getItems().get(0));
    }

    @Test
    void addItemShouldThrowWhenNull() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> list.addItem(null));
    }

    @Test
    void addItemShouldThrowWhenDuplicate() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);
        Item _itemDouble = mock(Item.class);

        list.addItem(_itemDouble);

        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> list.addItem(_itemDouble) // SUT
        );
        assertEquals("Item already in list", ex.getMessage());
    }
    @Test
    void getItemsShouldReturnImmutableCopy() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1Double, "Lista", _genre1Double);
        Item _itemDouble = mock(Item.class);
        list.addItem(_itemDouble);

        // Act
        var publications = list.getItems();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> publications.add(_itemDouble));
    }

    //    @Test
//    void makeListPrivateWhenPublic() {
//        // switchVisibility() – changes visibility from public back to private
//        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);
//
//        list.switchVisibility();
//        assertFalse(list.isPrivate());
//
//        list.switchVisibility();
//        assertTrue(list.isPrivate());
//    }
}