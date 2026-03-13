package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ItemRepo}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link Publication} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Condition} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Item} — mocked dummy (used only as input to {@code getDifferentOf})</li>
 * </ul>
 */

class ItemRepoTest {

    @Test
    void existsReturnsFalseWhenRepoIsEmpty() {
        // Arrange
        Publication pub = mock(Publication.class);
        ItemRepo repo = new ItemRepo();

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

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub, condition);

        // Act
        boolean result = repo.exists(pub);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsReturnsFalseForNullPublication() {
        // Arrange
        ItemRepo repo = new ItemRepo();

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

        ItemRepo repo = new ItemRepo();

        // Act
        Item item = repo.createItem(pub, condition);

        // Assert
        assertAll(
                () -> assertNotNull(item),
                () -> assertEquals(condition, item.getCondition()),
                () -> assertTrue(repo.exists(pub))
        );
    }

    @Test
    void createItemThrowsWhenPublicationAlreadyExists() {
        // Arrange
        Publication pub = mock(Publication.class);
        Condition condition = mock(Condition.class);

        ItemRepo repo = new ItemRepo();
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

        ItemRepo repo = new ItemRepo();
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

        ItemRepo repo = new ItemRepo();
        repo.createItem(pub1, condition);

        // Act + Assert
        List<Item> list1 = repo.getAll();
        assertEquals(1, list1.size());

        repo.createItem(pub2, condition);

        List<Item> list2 = repo.getAll();
        assertEquals(2, list2.size());
    }

    @Test
    void shouldReturnEmptyListWhenAllItemsExist() {
        // Arrange
        ItemRepo repo = new ItemRepo();

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
        ItemRepo repo = new ItemRepo();

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
        //Arrange / SUT
        ItemRepo repo = new ItemRepo();

        Item _itemDouble1 = repo.createItem(mock(Publication.class), mock(Condition.class));
        repo.createItem(mock(Publication.class), mock(Condition.class));

        //Act
        List<Item> existentItems = List.of(_itemDouble1);

        List<Item> result = repo.getDifferentOf(existentItems);

        //Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }
}
