package MITELOVERS.persistence.mem;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MemListOfItemsRepo}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link UserId} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link GenreId} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */
class MemListOfItemsRepoTest {

    private ListOfItemsId _listIdDouble;

    @BeforeEach
    void setUp() {
        _listIdDouble = mock(ListOfItemsId.class);
    }

    @Test
    void shouldCreateEmptyRepository() {
        // SUT & Act & Assert
        assertDoesNotThrow(MemListOfItemsRepo::new);
    }

    @Test
    void newRepositoryShouldBeEmpty() {
        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        int count = 0;
        for (ListOfItems ignored : repo.findAll()) count++;

        // Assert
        assertEquals(0, count);
    }

    @Test
    void saveShouldInsertNewItem() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);
        when(created.getName()).thenReturn(new Name("My List"));

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        ListOfItems result = repo.save(created);

        // Assert
        int count = 0;
        for (ListOfItems ignored : repo.findAll()) count++;
        final int finalCount = count;
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, finalCount),
                () -> assertEquals(new Name("My List"), result.getName())
        );
    }

    @Test
    void saveShouldReplaceExistingItem() {
        // Arrange
        ListOfItems first = mock(ListOfItems.class);
        ListOfItems second = mock(ListOfItems.class);

        when(first.identity()).thenReturn(_listIdDouble);
        when(second.identity()).thenReturn(_listIdDouble);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        repo.save(first);
        repo.save(second);

        // Assert
        int count = 0;
        for (ListOfItems ignored : repo.findAll()) count++;
        assertEquals(1, count);
        assertEquals(second, repo.ofIdentity(_listIdDouble).orElse(null));
    }

    @Test
    void saveShouldReturnSavedEntity() {
        // Arrange
        ListOfItems item = mock(ListOfItems.class);
        when(item.identity()).thenReturn(mock(ListOfItemsId.class));

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        ListOfItems result = repo.save(item);

        // Assert
        assertSame(item, result);
    }

    @Test
    void ofIdentityShouldReturnCorrectItem() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(created);

        // Act
        Optional<ListOfItems> result = repo.ofIdentity(_listIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(created, result.get());
    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenExists() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(created);

        // Act & Assert
        assertTrue(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenNotExists() {
        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act & Assert
        assertFalse(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void findAllKeysShouldReturnEmptyListWhenRepoIsEmpty() {
        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        List<ListOfItemsId> keys = repo.findAllKeys();

        // Assert
        assertNotNull(keys);
        assertTrue(keys.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeysInRepo() {
        // Arrange
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        ListOfItemsId id1 = mock(ListOfItemsId.class);
        ListOfItemsId id2 = mock(ListOfItemsId.class);

        when(list1.identity()).thenReturn(id1);
        when(list2.identity()).thenReturn(id2);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(list1);
        repo.save(list2);

        // Act
        List<ListOfItemsId> keys = repo.findAllKeys();

        // Assert
        assertAll(
                () -> assertEquals(2, keys.size()),
                () -> assertTrue(keys.contains(id1)),
                () -> assertTrue(keys.contains(id2))
        );
    }

    @Test
    void findAllKeysShouldReturnIndependentList() {
        // Arrange
        ListOfItems list = mock(ListOfItems.class);
        ListOfItemsId id = mock(ListOfItemsId.class);
        when(list.identity()).thenReturn(id);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(list);

        // Act
        List<ListOfItemsId> keys = repo.findAllKeys();
        keys.clear();

        // Assert
        assertEquals(1, repo.findAllKeys().size());
    }

    @Test
    void findAllKeysShouldMatchStoredIdentitiesExactly() {
        // Arrange
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        ListOfItemsId id1 = mock(ListOfItemsId.class);
        ListOfItemsId id2 = mock(ListOfItemsId.class);

        when(list1.identity()).thenReturn(id1);
        when(list2.identity()).thenReturn(id2);

        // SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(list1);
        repo.save(list2);

        // Act
        List<ListOfItemsId> keys = repo.findAllKeys();

        // Assert
        assertAll(
                () -> assertEquals(2, keys.size()),
                () -> assertTrue(keys.contains(id1)),
                () -> assertTrue(keys.contains(id2))
        );
    }

    @Test
    void findByUserIdShowThrowUnsupportedOperation(){
        //arrange
        UserId userIdDouble =  mock(UserId.class);

        //SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        //act + assert
        assertThrows(UnsupportedOperationException.class, () -> {repo.findListOfItemsByUserId(userIdDouble);});
    }

    @Test
    void deleteShouldShowThrowUnsupportedOperation(){
        //arrange
        ListOfItemsId listOfItemsIdDouble =  mock(ListOfItemsId.class);

        //SUT
        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        //act + assert
        assertThrows(UnsupportedOperationException.class, () -> {repo.deleteListOfItems(listOfItemsIdDouble);});
    }
}