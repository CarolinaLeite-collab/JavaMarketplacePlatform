package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void shouldCreateEmptyListOfItems() {
        new MemoListOfItemsRepo();
    }

    @Test
    void addListOfItemsSuccessfully() {
        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        when(_createdDouble.getUserId()).thenReturn(_userId1Double);
        when(_createdDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_createdDouble.getName()).thenReturn("My List");
        when(_createdDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        ListOfItems list = repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_userId1Double, list.getUserId()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_genreIdDouble, list.getGenreId()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, repo.getListOfListOfItems().size())
        );
    }

    @Test
    void cannotAddDuplicateList() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);
        ListOfItemsId id = mock(ListOfItemsId.class);

        when(_createdDouble.identity()).thenReturn(id);
        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);
        ListOfItems duplicate = repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.getListOfListOfItems().size());
    }

    @Test
    void getListReturnsCopy() {

        // Arrange
        ListOfItems _createdDouble = mock(ListOfItems.class);

        when(_factoryDouble.createListOfItems(_userId1Double, "My List", _genreIdDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        repo.addListOfItems(_userId1Double, "My List", _genreIdDouble);

        // Act
        List<ListOfItems> lists = repo.getListOfListOfItems();

        // Assert
        assertAll(
                () -> assertEquals(1, lists.size()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> lists.add(new ListOfItems(_listIdDouble, _userId1Double, "Other List", _genreId2Double)))
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {

        // Arrange
        ListOfItems _listPubDouble1 = mock(ListOfItems.class);
        ListOfItemsId id1 = mock(ListOfItemsId.class);
        when(_listPubDouble1.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble1.getName()).thenReturn("List A");
        when(_listPubDouble1.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble1.isPrivate()).thenReturn(false);
        when(_listPubDouble1.identity()).thenReturn(id1);


        ListOfItems _listPubDouble2 = mock(ListOfItems.class);
        ListOfItemsId id2 = mock(ListOfItemsId.class);
        when(_listPubDouble2.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble2.getName()).thenReturn("List B");
        when(_listPubDouble2.getUserId()).thenReturn(_userId2Double);
        when(_listPubDouble2.isPrivate()).thenReturn(true);
        when(_listPubDouble2.identity()).thenReturn(id2);


        ListOfItems _listPubDouble3 = mock(ListOfItems.class);
        ListOfItemsId id3 = mock(ListOfItemsId.class);
        when(_listPubDouble3.getGenreId()).thenReturn(_genreId2Double);
        when(_listPubDouble3.getName()).thenReturn("List C");
        when(_listPubDouble3.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble3.isPrivate()).thenReturn(false);
        when(_listPubDouble3.identity()).thenReturn(id3);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _genreIdDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfItems(_userId2Double, "List B", _genreIdDouble)).thenReturn(_listPubDouble2);
        when(_factoryDouble.createListOfItems(_userId1Double, "List C", _genreId2Double)).thenReturn(_listPubDouble3);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _genreIdDouble);
        repo.addListOfItems(_userId2Double, "List B", _genreIdDouble);
        repo.addListOfItems(_userId1Double, "List C", _genreId2Double);

        List<ListOfItems> result = repo.findPublicListsByGenre(_genreIdDouble);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("List A", result.get(0).getName()),
                () -> assertEquals(_userId1Double, result.get(0).getUserId()),
                () -> assertEquals(_genreIdDouble, result.get(0).getGenreId()),
                () -> assertFalse(result.get(0).isPrivate())
        );
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
    void findPublicListsByGenreShouldReturnImmutableCopy() {

        // Arrange
        ListOfItems _listPubDouble = mock(ListOfItems.class);
        when(_listPubDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUserId()).thenReturn(_userId1Double);
        when(_listPubDouble.isPrivate()).thenReturn(false);

        when(_factoryDouble.createListOfItems(_userId1Double, "List A", _genreIdDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfItemsRepo repo = new MemoListOfItemsRepo(_factoryDouble);

        // Act
        repo.addListOfItems(_userId1Double, "List A", _genreIdDouble);
        List<ListOfItems> result = repo.findPublicListsByGenre(_genreIdDouble);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_listPubDouble));
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
