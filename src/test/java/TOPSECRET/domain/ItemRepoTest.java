package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ItemRepoTest {

    @Test
    void existsReturnsFalseWhenRepoIsEmpty() {
        Publication pub = mock(Publication.class);
        ItemRepo repo = new ItemRepo();

        assertFalse(repo.exists(pub));
    }

    @Test
    void existsReturnsTrueWhenItemWithPublicationExists() {
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        assertTrue(repo.exists(pub));
    }

    @Test
    void existsReturnsFalseForNullPublication() {
        ItemRepo repo = new ItemRepo();

        assertFalse(repo.exists(null));
    }

    @Test
    void createItemCreatesAndStoresNewItem() {
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
        Item item = repo.createItem(pub, condition);

        assertNotNull(item);
        assertEquals(condition, item.getCondition());
        assertTrue(repo.exists(pub));
    }

    @Test
    void createItemThrowsWhenPublicationAlreadyExists() {
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.createItem(pub, condition)
        );

        assertEquals("Item for this publication already exists!", ex.getMessage());
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        List<Item> list = repo.getAll();

        assertEquals(1, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add(null));
    }

    @Test
    void getAllReflectsNewItems() {
        Publication pub1 = mock(Publication.class);
        Publication pub2 = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub1, condition);

        List<Item> list1 = repo.getAll();
        assertEquals(1, list1.size());

        repo.createItem(pub2, condition);

        List<Item> list2 = repo.getAll();
        assertEquals(2, list2.size());
    }

    @Test
    void shouldReturnEmptyListWhenAllItemsExist() {
        ItemRepo repo = new ItemRepo();

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> existentItems = List.of(_itemDouble1, _itemDouble2);

        List<Item> result = repo.getDifferentOf(existentItems);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleEmptyInputList() {
        ItemRepo repo = new ItemRepo();

        List<Item> existentItems = List.of();

        List<Item> result = repo.getDifferentOf(existentItems);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
