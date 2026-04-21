package MITELOVERS.persistence.mem;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
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
        ItemFactory factoryDouble = mock(ItemFactory.class);
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        Item result = sut.save(itemDouble);

        // Assert
        assertSame(itemDouble, result);
    }

    @Test
    void saveValidItemStoresItemInRepository() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

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
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        Iterable<Item> result = sut.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findAllRepositoryWithItemsReturnsStoredItems() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
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
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
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
        ItemFactory factoryDouble = mock(ItemFactory.class);
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        Optional<Item> result = sut.ofIdentity(unknownIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ofIdentityNullIdReturnsEmptyOptional() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

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
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(itemDouble);

        // Act
        boolean result = sut.containsOfIdentity(itemIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingIdReturnsFalse() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        ItemId unknownIdDouble = mock(ItemId.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

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
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        List<ItemId> result = sut.findAllKeys();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeysWhenRepoHasMultipleItems() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
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
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.findAllKeys();
        result.clear();
        List<ItemId> newResult = sut.findAllKeys();

        // Assert
        assertEquals(1, newResult.size());
        assertTrue(newResult.contains(itemIdDouble));
    }

    // ------------------------------------------------------------
    // addItem
    // ------------------------------------------------------------

    @Test
    void addItemValidArgumentsUsesFactoryToCreateItem() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Condition conditionDouble = mock(Condition.class);
        Description descriptionDouble = mock(Description.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                .thenReturn(itemDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        verify(factoryDouble).createItem(editionIdDouble, conditionDouble, descriptionDouble);
    }

    @Test
    void addItemValidArgumentsReturnsCreatedItemId() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Condition conditionDouble = mock(Condition.class);
        Description descriptionDouble = mock(Description.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                .thenReturn(itemDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        ItemId result = sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertSame(itemIdDouble, result);
    }

    @Test
    void addItemValidArgumentsStoresCreatedItemInRepository() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Condition conditionDouble = mock(Condition.class);
        Description descriptionDouble = mock(Description.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(editionIdDouble, conditionDouble, descriptionDouble))
                .thenReturn(itemDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);

        // Act
        sut.addItem(editionIdDouble, conditionDouble, descriptionDouble);

        // Assert
        assertTrue(sut.containsOfIdentity(itemIdDouble));
        assertEquals(Optional.of(itemDouble), sut.ofIdentity(itemIdDouble));
    }

    @Test
    void addItemShouldThrowWhenItemAlreadyExists() {
        // Arrange
        EditionId editionId = mock(EditionId.class);
        Condition condition = mock(Condition.class);
        Description description = mock(Description.class);
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item item = mock(Item.class);
        ItemId itemId = mock(ItemId.class);

        when(factoryDouble.createItem(editionId, condition, description)).thenReturn(item);
        when(item.identity()).thenReturn(itemId);

        MemItemRepo repo = new MemItemRepo(factoryDouble);

        repo.save(item);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.addItem(editionId, condition, description)
        );

        assertEquals("Item already exists", exception.getMessage());
    }

    // ------------------------------------------------------------
    // getDifferentOf
    // ------------------------------------------------------------

    @Test
    void getDifferentOfNullListReturnsAllStoredItemIds() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(item1Double);
        sut.save(item2Double);

        // Act
        List<ItemId> result = sut.getDifferentOf(null);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(itemId1Double));
        assertTrue(result.contains(itemId2Double));
    }

    @Test
    void getDifferentOfEmptyListReturnsAllStoredItemIds() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.getDifferentOf(List.of());

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(itemIdDouble));
    }

    @Test
    void getDifferentOfAllItemIdsAlreadyExistReturnsEmptyList() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.getDifferentOf(List.of(itemIdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getDifferentOfSomeItemIdsAlreadyExistReturnsOnlyDifferentItemIds() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item item1Double = mock(Item.class);
        ItemId itemId1Double = mock(ItemId.class);
        when(item1Double.identity()).thenReturn(itemId1Double);

        Item item2Double = mock(Item.class);
        ItemId itemId2Double = mock(ItemId.class);
        when(item2Double.identity()).thenReturn(itemId2Double);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(item1Double);
        sut.save(item2Double);

        // Act
        List<ItemId> existentItemIds = List.of(itemId1Double);
        List<ItemId> result = sut.getDifferentOf(existentItemIds);

        // Assert
        assertEquals(List.of(itemId2Double), result);
    }

    @Test
    void getDifferentOfResultIsUnmodifiable() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);

        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);

        // SUT
        MemItemRepo sut = new MemItemRepo(factoryDouble);
        sut.save(itemDouble);

        // Act
        List<ItemId> result = sut.getDifferentOf(List.of());

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(ItemId.class)));
    }
}
