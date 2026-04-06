package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.ListOfItems.ListOfItemsFactory;
import TOPSECRET.domain.User;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MemoListOfItemsRepo}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link ListOfItemsFactory} — mocked collaborator (creation dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Genre} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class MemoListOfItemsRepoTest {

    private ListOfItemsFactory _factoryDouble;

    private UserId _userId1Double;
    private UserId _userId2Double;
    private GenreId _genreIdDouble;
    private GenreId _genreId2Double;
    private ListOfItemsId _listIdDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(ListOfItemsFactory.class);
        _genreIdDouble = mock(GenreId.class);
        _genreId2Double = mock(GenreId.class);
        _userId1Double = mock(UserId.class);
        _userId2Double = mock(UserId.class);
        _listIdDouble = mock(ListOfItemsId.class);
    }

    @Test
    void shouldCreateEmptyRepository() {
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo();
        assertNotNull(repo);
        assertEquals(0, repo.findAll().spliterator().getExactSizeIfKnown());
    }

    @Test
    void addListOfItemsSuccessfully() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);
        when(created.getUserId()).thenReturn(_userId1Double);
        when(created.getGenreId()).thenReturn(_genreIdDouble);
        when(created.getName()).thenReturn("My List");
        when(created.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble))
                .thenReturn(created);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        ListOfItems result = repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown()),
                () -> assertEquals("My List", result.getName())
        );
    }

    @Test
    void cannotAddDuplicateList() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble))
                .thenReturn(created);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);
        ListOfItems duplicate = repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown());
    }

    @Test
    void saveShouldInsertOrReplace() {
        // Arrange
        ListOfItems first = mock(ListOfItems.class);
        ListOfItems second = mock(ListOfItems.class);

        when(first.identity()).thenReturn(_listIdDouble);
        when(second.identity()).thenReturn(_listIdDouble);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.save(first);
        repo.save(second);

        // Assert
        assertEquals(1, repo.findAll().spliterator().getExactSizeIfKnown());
        assertEquals(second, repo.ofIdentity(_listIdDouble).orElse(null));
    }

    @Test
    void ofIdentityShouldReturnCorrectItem() {
        // Arrange
        ListOfItems created = mock(ListOfItems.class);
        when(created.identity()).thenReturn(_listIdDouble);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
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

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(created);

        assertTrue(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void containsOfIdentityShouldReturnFalseWhenNotExists() {
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        assertFalse(repo.containsOfIdentity(_listIdDouble));
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicLists() {
        // Arrange
        ListOfItems pub = mock(ListOfItems.class);
        ListOfItems priv = mock(ListOfItems.class);

        when(pub.identity()).thenReturn(mock(ListOfItemsId.class));
        when(priv.identity()).thenReturn(mock(ListOfItemsId.class));

        when(pub.getGenreId()).thenReturn(_genreIdDouble);
        when(priv.getGenreId()).thenReturn(_genreIdDouble);

        when(pub.isPrivate()).thenReturn(false);
        when(priv.isPrivate()).thenReturn(true);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(pub);
        repo.save(priv);

        // Act
        List<ListOfItems> result = repo.findPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.get(0).isPrivate());
    }

    @Test
    void findListsByUserIdShouldReturnCorrectLists() {
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.identity()).thenReturn(mock(ListOfItemsId.class));
        when(list2.identity()).thenReturn(mock(ListOfItemsId.class));

        when(list1.getUserId()).thenReturn(_userId1Double);
        when(list2.getUserId()).thenReturn(_userId2Double);

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(list1);
        repo.save(list2);

        List<ListOfItems> result = repo.findListsByUserId(_userId1Double);

        assertEquals(1, result.size());
        assertEquals(list1, result.get(0));
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnCorrectItem() {
        ListOfItems item = mock(ListOfItems.class);

        when(item.identity()).thenReturn(mock(ListOfItemsId.class));
        when(item.getUserId()).thenReturn(_userId1Double);
        when(item.getGenreId()).thenReturn(_genreIdDouble);
        when(item.getName()).thenReturn("My List");

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(item);

        ListOfItems result = repo.findByOwnerNameAndGenre(_userId1Double, "My List", _genreIdDouble);

        assertEquals(item, result);
    }

    @Test
    void getIdNameMapShouldReturnCorrectMapping() {
        ListOfItems item = mock(ListOfItems.class);
        ListOfItemsId id = mock(ListOfItemsId.class);

        when(item.identity()).thenReturn(id);
        when(item.getName()).thenReturn("My List");

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(item);

        Map<ListOfItemsId, String> map = repo.getIdNameMap();

        assertEquals(1, map.size());
        assertEquals("My List", map.get(id));
    }

    @Test
    void getIdNameMapShouldBeImmutable() {
        ListOfItems item = mock(ListOfItems.class);
        ListOfItemsId id = mock(ListOfItemsId.class);

        when(item.identity()).thenReturn(id);
        when(item.getName()).thenReturn("My List");

        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);
        repo.save(item);

        Map<ListOfItemsId, String> map = repo.getIdNameMap();

        assertThrows(UnsupportedOperationException.class, () -> map.put(id, "Other"));
    }




    @Test
    void findPublicListsByGenreShouldReturnEmptyWhenNoPublicListsForThatGenre() {

        // Arrange
        ListOfItems _listPubDouble = mock(ListOfItems.class);
        when(_listPubDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _genreIdDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _genreIdDouble);
        List<ListOfItems> result = repo.findPublicListsByGenre(_genreIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findListsByUserShouldReturnOnlyListsOfThatUser() {

        // Arrange
        ListOfItems _listPubDouble1 = mock(ListOfItems.class);
        ListOfItems _listPubDouble2 = mock(ListOfItems.class);
        ListOfItemsId id1 = mock(ListOfItemsId.class);
        ListOfItemsId id2 = mock(ListOfItemsId.class);

        when(_listPubDouble1.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble1.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble1.getName()).thenReturn("U1 List");
        when(_listPubDouble1.identity()).thenReturn(id1);

        when(_listPubDouble2.getUserId()).thenReturn(_userId2Double);
        when(_listPubDouble2.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble2.getName()).thenReturn("U2 List");
        when(_listPubDouble2.identity()).thenReturn(id2);

        when(_factoryDouble.createListOfItems(_userId1Double, "U1 List", _genreIdDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfItems(_userId2Double, "U2 List", _genreIdDouble)).thenReturn(_listPubDouble2);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "U1 List", _genreIdDouble);
        repo.addListOfItems(_userId2Double, "U2 List", _genreIdDouble);

        List<ListOfItems> result = repo.findListsByUserId(_userId1Double);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("U1 List", result.get(0).getName()),
                () -> assertEquals(_userId1Double, result.get(0).getUserId())
        );
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnListWhenExists() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreIgnoresCase() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "my list", _genreIdDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreTrimsName() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "  My List  ", _genreIdDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListDoesNotExist() {

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        ListOfItems found = repo.findByOwnerNameAndGenre(_userId1Double, "Unknown", _genreIdDouble);

        // Assert
        assertNull(found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListExistsButDoesNotMatch() {

        // Arrange
        ListOfItems list = mock(ListOfItems.class);
        when(list.getUserId()).thenReturn(_userId2Double);
        when(list.getName()).thenReturn("Other List");
        when(list.getGenreId()).thenReturn(_genreIdDouble);

        when(_factoryDouble.createListOfItems(_userId2Double, "Other List", _genreIdDouble))
                .thenReturn(list);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId2Double, "Other List", _genreIdDouble);
        ListOfItems result = repo.findByOwnerNameAndGenre(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertNull(result);
    }
}
