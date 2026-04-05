package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListOfItemsTest {

    private UserId _user1IdDouble;
    private UserId _user2IdDouble;
    private GenreId _genre1IdDouble;
    private GenreId _genre2IdDouble;
    private ListOfItemsId _listIdDouble;
    private ItemId _itemIdDouble;

    @BeforeEach
    void setUp() {
        _user1IdDouble = mock(UserId.class);
        _user2IdDouble = mock(UserId.class);
        _genre1IdDouble = mock(GenreId.class);
        _genre2IdDouble = mock(GenreId.class);
        _listIdDouble = mock(ListOfItemsId.class);
    }

    @Test
    void constructsListSuccessfully() {
        // Arrange & Act
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble,"My favorite books", _genre1IdDouble);

        // Assert
        assertNotNull(list);
        assertTrue(list.isPrivate());
        assertTrue(list.getItemIds().isEmpty());
    }

    @Test
    void throwsExceptionWhenListNameIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, _user1IdDouble, null, _genre1IdDouble));
    }

    @Test
    void sameAsShouldReturnTrueForSameArguments() {
        //Arrange
        ListOfItems list1 = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);

        // Act & Assert
        assertTrue(list1.sameAs(list2));
    }

    @Test
    void hashCodeShouldReturnTrueForSameArguments() {
        // Arrange
        ListOfItems list1 = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);

        // Act & Assert
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);

        // Act & Assert
        assertNotEquals(list, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);
        String notAList = "not a ListOfItems";

        // Act & Assert
        assertNotEquals(list, notAList);
    }

    @Test
    void listShouldBePrivateByDefault() {
        // Arrange & Act
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);

        // Assert
        assertTrue(list.isPrivate());
    }

    @Test
    void makePublicShouldMakeListPublic() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);

        // Act
        list.makePublic();

        // Assert
        assertFalse(list.isPrivate());
    }

    @Test
    void makePublicShouldBeIdempotent() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);

        // Act
        list.makePublic();
        list.makePublic();

        // Assert
        assertFalse(list.isPrivate());
    }

    @Test
    void addItemShouldAddSuccessfully() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);

        // Act
        list.addItem(_itemIdDouble);

        // Assert
        assertEquals(1, list.getItemIds().size());
        assertEquals(_itemIdDouble, list.getItemIds().get(0));
    }

    @Test
    void addItemShouldThrowWhenNull() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> list.addItem(null));
    }

    @Test
    void addItemShouldThrowWhenDuplicate() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);

        list.addItem(_itemIdDouble);

        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> list.addItem(_itemIdDouble) // SUT
        );
        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void getItemIdsShouldReturnImmutableCopy() {
        // Arrange
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);
        list.addItem(_itemIdDouble);

        // Act
        var items = list.getItemIds();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> items.add(_itemIdDouble));
    }

    @Test
    void getListOfItemsIDShouldReturnCorrectId() {
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "My List", _genre1IdDouble);

        assertEquals(_listIdDouble, list.identity());
    }

    @Test
    void constructorShouldThrowWhenListIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(null, _user1IdDouble, "My List", _genre1IdDouble));
    }

    @Test
    void constructorShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, null, "My List", _genre1IdDouble));
    }

    @Test
    void constructorShouldThrowWhenGenreIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, _user1IdDouble, "My List", null));
    }

    @Test
    void equalsShouldReturnFalseForDifferentId() {
        ListOfItems list1 = new ListOfItems(ListOfItemsId.newId(), _user1IdDouble, "My List", _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(ListOfItemsId.newId(), _user1IdDouble, "My List", _genre1IdDouble);

        assertNotEquals(list1, list2);
    }

    @Test
    void modifyingReturnedListShouldNotAffectInternalState() {

        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);
        list.addItem(_itemIdDouble);

        var copy = list.getItemIds();
        assertThrows(UnsupportedOperationException.class, () -> copy.remove(0));

        // Ensure original list remains unchanged
        assertEquals(1, list.getItemIds().size());
    }

    @Test
    void makePublicShouldNotChangeOtherFields() {
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, "Lista", _genre1IdDouble);

        list.makePublic();

        assertEquals(_user1IdDouble, list.getUserId());
        assertEquals("Lista", list.getName());
        assertEquals(_genre1IdDouble, list.getGenreId());
    }

    @Test
    void hashCodeShouldReturnDifferentValuesForDifferentIds() {
        ListOfItems list1 = new ListOfItems(ListOfItemsId.newId(), _user1IdDouble, "My List", _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(ListOfItemsId.newId(), _user1IdDouble, "My List", _genre1IdDouble);

        assertNotEquals(list1.hashCode(), list2.hashCode());
    }
}