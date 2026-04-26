package MITELOVERS.persistence.mem;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
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
 *   <li>{@link ListOfItemsFactory} — mocked collaborator (creation dependency)</li>
 *   <li>{@link UserId} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link GenreId} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class MemListOfItemsRepoTest {

    private UserId _userId1Double;
    private UserId _userId2Double;
    private GenreId _genreIdDouble;
    private GenreId _genreId2Double;
    private ListOfItemsId _listIdDouble;

    @BeforeEach
    void setUp() {
        _genreIdDouble = mock(GenreId.class);
        _genreId2Double = mock(GenreId.class);
        _userId1Double = mock(UserId.class);
        _userId2Double = mock(UserId.class);
        _listIdDouble = mock(ListOfItemsId.class);
    }

    @Test
    void shouldCreateEmptyRepository() {
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        assertNotNull(repo);
        assertEquals(0, repo.findAll().spliterator().getExactSizeIfKnown());
    }

    @Test
    void saveShouldInsertNewItem() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);
        when(created.getName()).thenReturn("My List");

        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        ListOfItems result = repo.save(created);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown()),
                () -> assertEquals("My List", result.getName())
        );
    }

    @Test
    void saveShouldReplaceExistingItem() {
        // Arrange
        ListOfItems first = mock(ListOfItems.class);
        ListOfItems second = mock(ListOfItems.class);

        when(first.identity()).thenReturn(_listIdDouble);
        when(second.identity()).thenReturn(_listIdDouble);

        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        repo.save(first);
        repo.save(second);

        // Assert
        assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown());
        assertEquals(second, repo.ofIdentity(_listIdDouble).orElse(null));
    }

    @Test
    void saveShouldInsertOrReplace() {
        // Arrange
        ListOfItems first = mock(ListOfItems.class);
        ListOfItems second = mock(ListOfItems.class);

        when(first.identity()).thenReturn(_listIdDouble);
        when(second.identity()).thenReturn(_listIdDouble);

        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        // Act
        repo.save(first);
        repo.save(second);

        // Assert
        assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown());
        assertEquals(second, repo.ofIdentity(_listIdDouble).orElse(null));
    }

    @Test
    void saveShouldReturnSavedEntity() {
        ListOfItems item = mock(ListOfItems.class);
        when(item.identity()).thenReturn(mock(ListOfItemsId.class));

        MemListOfItemsRepo repo = new MemListOfItemsRepo();

        ListOfItems result = repo.save(item);

        assertSame(item, result);
    }

    @Test
    void ofIdentityShouldReturnCorrectItem() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

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
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(created);

        assertTrue(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenNotExists() {
        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        assertFalse(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void findAllKeysShouldReturnEmptyListWhenRepoIsEmpty() {
        // Arrange
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

        MemListOfItemsRepo repo = new MemListOfItemsRepo();
        repo.save(list);

        // Act
        List<ListOfItemsId> keys = repo.findAllKeys();
        keys.clear(); // modify returned list

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

}
