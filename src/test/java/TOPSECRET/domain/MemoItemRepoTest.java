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
        Publication pubicationDouble = mock(Publication.class);
        ItemFactory factoryDouble = mock(ItemFactory.class);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

        // Act
        boolean result = repo.exists(pubicationDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsReturnsTrueWhenItemWithPublicationExists() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        Condition conditionDouble = mock(Condition.class);
        Item itemDouble = mock(Item.class);
        when(itemDouble.get_publication()).thenReturn(publicationDouble);
        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(publicationDouble, conditionDouble)).thenReturn(itemDouble);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);
        repo.createItem(publicationDouble, conditionDouble);

        // Act
        boolean result = repo.exists(publicationDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsReturnsFalseForNullPublication() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);;

        // Act
        boolean result = repo.exists(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void createItemCreatesAndStoresNewItem() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        Condition conditionDouble = mock(Condition.class);
        Item itemDouble = mock(Item.class);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(publicationDouble, conditionDouble)).thenReturn(itemDouble);

        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

        //SUT
        // Act
        Item result = repo.createItem(publicationDouble, conditionDouble);

        // Assert
        assertEquals(itemDouble, result);
    }

    @Test
    void createItemThrowsWhenPublicationAlreadyExists() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        Condition conditionDouble = mock(Condition.class);
        Item itemDouble = mock(Item.class);
        when(itemDouble.get_publication()).thenReturn(publicationDouble);
        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(publicationDouble, conditionDouble)).thenReturn(itemDouble);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);
        repo.createItem(publicationDouble, conditionDouble);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.createItem(publicationDouble, conditionDouble)
        );

        assertEquals("Item for this publication already exists!", ex.getMessage());
    }

    @Test
    void getAllReturnsUnmodifiableList() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        Condition conditionDouble = mock(Condition.class);
        ItemFactory factoryDouble = mock(ItemFactory.class);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);
        repo.createItem(publicationDouble, conditionDouble);

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
        Publication publication1Double = mock(Publication.class);
        Publication publication2Double = mock(Publication.class);
        Condition conditionDouble = mock(Condition.class);
        Item item1Double = mock(Item.class);
        when(item1Double.get_publication()).thenReturn(publication1Double);

        Item item2Double = mock(Item.class);
        when(item2Double.get_publication()).thenReturn(publication2Double);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(publication1Double, conditionDouble)).thenReturn(item1Double);
        when(factoryDouble.createItem(publication2Double, conditionDouble)).thenReturn(item2Double);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

        // Act + Assert
        repo.createItem(publication1Double, conditionDouble);
        List<Item> list1 = repo.getAll();
        assertEquals(1, list1.size());

        repo.createItem(publication2Double, conditionDouble);
        List<Item> list2 = repo.getAll();
        assertEquals(2, list2.size());
    }

    @Test
    void shouldReturnEmptyListWhenAllItemsExist() {
        // Arrange
        ItemFactory factoryDouble = mock(ItemFactory.class);
        Item _item1Double = mock(Item.class);
        Item _item2Double = mock(Item.class);

        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

        List<Item> existentItems = List.of(_item1Double, _item2Double);

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
        ItemFactory factoryDouble = mock(ItemFactory.class);
        //SUT
        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

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
        Publication publication1Double = mock(Publication.class);
        Publication publication2Double = mock(Publication.class);

        Condition conditionDouble = mock(Condition.class);

        Item item1Double = mock(Item.class);
        when(item1Double.get_publication()).thenReturn(publication1Double);

        Item item2Double = mock(Item.class);
        when(item2Double.get_publication()).thenReturn(publication2Double);

        ItemFactory factoryDouble = mock(ItemFactory.class);
        when(factoryDouble.createItem(publication1Double, conditionDouble)).thenReturn(item1Double);
        when(factoryDouble.createItem(publication2Double, conditionDouble)).thenReturn(item2Double);

        MemoItemRepo repo = new MemoItemRepo(factoryDouble);

        //SUT
        repo.createItem(publication1Double, conditionDouble);
        repo.createItem(publication2Double, conditionDouble);

        //Act
        List<Item> existentItems = List.of(item1Double);

        List<Item> result = repo.getDifferentOf(existentItems);

        //Assert
        assertEquals(List.of(item2Double), result);
    }
}
