package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoItemRepoTest {

    @Test
    void existsReturnsFalseWhenRepoIsEmpty() {
        // Arrange
        Publication pub = mock(Publication.class);
        ItemFactory factory = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);

        // Act
        boolean result = repo.exists(pub);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsReturnsTrueWhenItemWithPublicationExists() {
        // Arrange
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);
        Item item = mock(Item.class);
        when(item.getPublication()).thenReturn(pub);
        ItemFactory factory = mock(ItemFactory.class);
        when(factory.createItem(pub, condition)).thenReturn(item);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);
        repo.createItem(pub, condition);

        // Act
        boolean result = repo.exists(pub);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsReturnsFalseForNullPublication() {
        // Arrange
        ItemFactory factory = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);;

        // Act
        boolean result = repo.exists(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void createItemCreatesAndStoresNewItem() {
        // Arrange
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);
        Item item = mock(Item.class);

        ItemFactory factory = mock(ItemFactory.class);
        when(factory.createItem(pub, condition)).thenReturn(item);

        MemoItemRepo repo = new MemoItemRepo(factory);

        //SUT
        // Act
        Item result = repo.createItem(pub, condition);

        // Assert
        assertEquals(item, result);
    }

    @Test
    void createItemThrowsWhenPublicationAlreadyExists() {
        // Arrange
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);
        Item item = mock(Item.class);
        when(item.getPublication()).thenReturn(pub);
        ItemFactory factory = mock(ItemFactory.class);
        when(factory.createItem(pub, condition)).thenReturn(item);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);
        repo.createItem(pub, condition);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.createItem(pub, condition)
        );

        assertEquals("Item for this publication already exists!", ex.getMessage());
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        // Arrange
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemFactory factory = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);
        repo.createItem(pub, condition);

        // Act
        List<Item> list = repo.getAll();

        // Assert
        assertAll(
                () -> assertEquals(1, list.size()),
                () -> assertThrows(UnsupportedOperationException.class, () -> list.add(null))
        );
    }

    @Test
    void getAllReflectsNewItems() {
        // Arrange
        Publication pub1 = mock(Publication.class);
        Publication pub2 = mock(Publication.class);
        Condition condition = mock(Condition.class);
        Item item1 = mock(Item.class);
        when(item1.getPublication()).thenReturn(pub1);

        Item item2 = mock(Item.class);
        when(item2.getPublication()).thenReturn(pub2);

        ItemFactory factory = mock(ItemFactory.class);
        when(factory.createItem(pub1, condition)).thenReturn(item1);
        when(factory.createItem(pub2, condition)).thenReturn(item2);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);

        // Act + Assert
        repo.createItem(pub1, condition);
        List<Item> list1 = repo.getAll();
        assertEquals(1, list1.size());

        repo.createItem(pub2, condition);
        List<Item> list2 = repo.getAll();
        assertEquals(2, list2.size());
    }

    @Test
    void shouldReturnEmptyListWhenAllItemsExist() {
        // Arrange
        ItemFactory factory = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> existentItems = List.of(_itemDouble1, _itemDouble2);

        // Act
        List<Item> result = repo.getDifferentOf(existentItems);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );

    }

    @Test
    void shouldHandleEmptyInputList() {
        // Arrange
        ItemFactory factory = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factory);

        List<Item> existentItems = List.of();

        // Act
        List<Item> result = repo.getDifferentOf(existentItems);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void shouldReturnListWhenOnlySomeItemsExist() {
        //Arrange
        Publication pub1 = mock(Publication.class);
        Publication pub2 = mock(Publication.class);

        Condition cond = mock(Condition.class);

        Item item1 = mock(Item.class);
        when(item1.getPublication()).thenReturn(pub1);

        Item item2 = mock(Item.class);
        when(item2.getPublication()).thenReturn(pub2);

        ItemFactory factory = mock(ItemFactory.class);
        when(factory.createItem(pub1, cond)).thenReturn(item1);
        when(factory.createItem(pub2, cond)).thenReturn(item2);

        MemoItemRepo repo = new MemoItemRepo(factory);

        //SUT
        repo.createItem(pub1, cond);
        repo.createItem(pub2, cond);

        //Act
        List<Item> existentItems = List.of(item1);

        List<Item> result = repo.getDifferentOf(existentItems);

        //Assert
        assertEquals(List.of(item2), result);
    }
}
