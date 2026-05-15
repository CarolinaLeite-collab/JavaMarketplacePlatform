package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("My favorite books"), _genre1IdDouble);

        assertNotNull(list);
        assertTrue(list.isPrivate());
        assertTrue(list.getItemIds().isEmpty());
    }

    @Test
    void throwsExceptionWhenListNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_user1IdDouble, null, _genre1IdDouble));
    }

    @Test
    void sameAsShouldReturnFalseForDifferentInstances() {
        ListOfItems list1 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertFalse(list1.sameAs(list2));
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertNotEquals(list, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertNotEquals(list, "not a ListOfItems");
    }

    @Test
    void listShouldBePrivateByDefault() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        assertTrue(list.isPrivate());
    }

    @Test
    void makePublicShouldMakeListPublic() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        list.makePublic(new SharedDuration(7));

        assertFalse(list.isPrivate());
    }

    @Test
    void makePublicShouldBeIdempotent() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        list.makePublic(new SharedDuration(7));
        list.makePublic(new SharedDuration(7));

        assertFalse(list.isPrivate());
    }

    @Test
    void makePublicShouldThrowWhenDurationIsNull() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        assertThrows(IllegalArgumentException.class, () -> list.makePublic(null));
    }

    @Test
    void makePublicShouldSetSharedUntil() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);
        LocalDateTime before = LocalDateTime.now().plusDays(7).minusSeconds(1);

        list.makePublic(new SharedDuration(7));

        assertTrue(list.getSharedUntil().isAfter(before));
    }

    @Test
    void getSharedUntilShouldReturnNullByDefault() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        assertNull(list.getSharedUntil());
    }

    @Test
    void isPrivateShouldReturnTrueAfterExpiry() {
        LocalDateTime expired = LocalDateTime.now().minusDays(1);
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, new Name("Lista"),
                _genre1IdDouble, false, expired);

        assertTrue(list.isPrivate());
    }

    @Test
    void isPrivateShouldReturnFalseWhenNotExpired() {
        LocalDateTime future = LocalDateTime.now().plusDays(7);
        ListOfItems list = new ListOfItems(_listIdDouble, _user1IdDouble, new Name("Lista"),
                _genre1IdDouble, false, future);

        assertFalse(list.isPrivate());
    }

    @Test
    void addItemShouldAddSuccessfully() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);

        list.addItem(_itemIdDouble);

        assertEquals(1, list.getItemIds().size());
        assertEquals(_itemIdDouble, list.getItemIds().get(0));
    }

    @Test
    void addItemShouldThrowWhenNull() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        assertThrows(IllegalArgumentException.class, () -> list.addItem(null));
    }

    @Test
    void addItemShouldThrowWhenDuplicate() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);
        list.addItem(_itemIdDouble);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> list.addItem(_itemIdDouble));
        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void getItemIdsShouldReturnImmutableCopy() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);
        list.addItem(_itemIdDouble);

        var items = list.getItemIds();

        assertThrows(UnsupportedOperationException.class, () -> items.add(_itemIdDouble));
    }

    @Test
    void identityShouldReturnNonNullId() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertNotNull(list.identity());
    }

    @Test
    void constructorShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(null, new Name("My List"), _genre1IdDouble));
    }

    @Test
    void constructorShouldThrowWhenGenreIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_user1IdDouble, new Name("My List"), null));
    }

    @Test
    void equalsShouldReturnFalseForDifferentInstances() {
        ListOfItems list1 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertNotEquals(list1, list2);
    }

    @Test
    void modifyingReturnedListShouldNotAffectInternalState() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);
        _itemIdDouble = mock(ItemId.class);
        list.addItem(_itemIdDouble);

        var copy = list.getItemIds();
        assertThrows(UnsupportedOperationException.class, () -> copy.remove(0));
        assertEquals(1, list.getItemIds().size());
    }

    @Test
    void makePublicShouldNotChangeOtherFields() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("Lista"), _genre1IdDouble);

        list.makePublic(new SharedDuration(7));

        assertEquals(_user1IdDouble, list.getUserId());
        assertEquals(new Name("Lista"), list.getName());
        assertEquals(_genre1IdDouble, list.getGenreId());
    }

    @Test
    void hashCodeShouldDependOnIdObject() {
        ListOfItems list1 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertNotEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void sameAsShouldReturnTrueForSameInstance() {
        ListOfItems list = new ListOfItems(_user1IdDouble, new Name("My List"), _genre1IdDouble);

        assertTrue(list.sameAs(list));
    }

    @Test
    void sameAsShouldReturnFalseForDifferentIds() {
        ListOfItems list1 = new ListOfItems(_user1IdDouble, new Name("My"), _genre1IdDouble);
        ListOfItems list2 = new ListOfItems(_user1IdDouble, new Name("My"), _genre1IdDouble);

        assertFalse(list1.sameAs(list2));
    }

    @Test
    void reconstructionConstructorShouldConstructSuccessfully() {
        ListOfItemsId id = mock(ListOfItemsId.class);

        ListOfItems list = new ListOfItems(id, _user1IdDouble, new Name("My List"),
                _genre1IdDouble, true, null);

        assertNotNull(list);
        assertEquals(id, list.identity());
        assertTrue(list.isPrivate());
        assertTrue(list.getItemIds().isEmpty());
    }

    @Test
    void reconstructionConstructorShouldThrowWhenListOfItemsIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(null, _user1IdDouble, new Name("My List"),
                        _genre1IdDouble, true, null));
    }

    @Test
    void reconstructionConstructorShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, null, new Name("My List"),
                        _genre1IdDouble, true, null));
    }

    @Test
    void reconstructionConstructorShouldThrowWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, _user1IdDouble, null,
                        _genre1IdDouble, true, null));
    }

    @Test
    void reconstructionConstructorShouldThrowWhenGenreIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfItems(_listIdDouble, _user1IdDouble, new Name("My List"),
                        null, true, null));
    }

    @Test
    void equalsShouldReturnTrueForSameId() {
        ListOfItemsId id = mock(ListOfItemsId.class);
        ListOfItems list1 = new ListOfItems(id, _user1IdDouble, new Name("My List"),
                _genre1IdDouble, true, null);
        ListOfItems list2 = new ListOfItems(id, _user1IdDouble, new Name("My List"),
                _genre1IdDouble, true, null);

        assertEquals(list1, list2);
    }
}