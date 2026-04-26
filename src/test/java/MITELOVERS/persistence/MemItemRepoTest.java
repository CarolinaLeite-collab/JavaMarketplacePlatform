package MITELOVERS.persistence;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.ItemId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemItemRepoTest {

    // ------------------------------------------------------------
    // save
    // ------------------------------------------------------------

    @Test
    void saveValidItemReturnsSameItem() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo();

        // Act
        Item result = sut.save(itemDouble);

        // Assert
        assertSame(itemDouble, result);
    }

    @Test
    void saveValidItemStoresItemInRepository() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo();

        // Act
        sut.save(itemDouble);
        Optional<Item> result = sut.ofIdentity(itemIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(itemDouble, result.get());
    }

    // ------------------------------------------------------------
    // findAll
    // ------------------------------------------------------------

    @Test
    void findAllEmptyRepositoryReturnsEmptyIterable() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo();

        // Act
        Iterable<Item> result = sut.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findAllRepositoryWithItemsReturnsStoredItems() {

        // Arrange
        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo();
        sut.save(item1Double);
        sut.save(item2Double);

        // Act
        List<Item> result = new ArrayList<>();
        sut.findAll().forEach(result::add);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(item1Double));
        assertTrue(result.contains(item2Double));
    }

    // ------------------------------------------------------------
    // ofIdentity
    // ------------------------------------------------------------

    @Test
    void ofIdentityExistingIdReturnsItemWrappedInOptional() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo();
        sut.save(itemDouble);

        // Act
        Optional<Item> result = sut.ofIdentity(itemIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(itemDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingIdReturnsEmptyOptional() {

        // Arrange
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo();

        // Act
        Optional<Item> result = sut.ofIdentity(unknownIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ofIdentityNullIdReturnsEmptyOptional() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo();

        // Act
        Optional<Item> result = sut.ofIdentity(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // containsOfIdentity
    // ------------------------------------------------------------

    @Test
    void containsOfIdentityExistingIdReturnsTrue() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo();
        sut.save(itemDouble);

        // Act
        boolean result = sut.containsOfIdentity(itemIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingIdReturnsFalse() {

        // Arrange
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo();

        // Act
        boolean result = sut.containsOfIdentity(unknownIdDouble);

        // Assert
        assertFalse(result);
    }

    // ------------------------------------------------------------
    // findAllKeys
    // ------------------------------------------------------------

    @Test
    void findAllKeysShouldReturnEmptyListWhenRepoIsEmpty() {

        // SUT + Arrange
        MemItemRepo sut = new MemItemRepo();

        // Act
        List<ItemId> result = sut.findAllKeys();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeysWhenRepoHasMultipleItems() {

        // Arrange
        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo();
        sut.save(item1Double);
        sut.save(item2Double);

        // Act
        List<ItemId> result = sut.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(itemId1Double));
        assertTrue(result.contains(itemId2Double));
    }

    @Test
    void findAllKeysShouldReturnIndependentList() {

        // Arrange
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo();
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.findAllKeys();
        result.clear();
        List<ItemId> newResult = sut.findAllKeys();

        // Assert
        assertEquals(1, newResult.size());
        assertTrue(newResult.contains(itemIdDouble));
    }
}
