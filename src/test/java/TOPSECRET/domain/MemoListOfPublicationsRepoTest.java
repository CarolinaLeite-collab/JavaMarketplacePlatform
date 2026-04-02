package TOPSECRET.domain;

import TOPSECRET.domain.genre.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MemoListOfPublicationsRepo}.
 *
 * <p>The following Mockito doubles are used:
 * <ul>
 *   <li>{@link ListOfPublicationsFactory} — mocked collaborator (creation dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Genre} — mocked dummy (structural input, no behaviour required)</li>
 * </ul>
 */

class MemoListOfPublicationsRepoTest {

    private ListOfPublicationsFactory _factoryDouble;

    private User _user1Double;
    private User _user2Double;
    private Genre _actionDouble;
    private Genre _poetryDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(ListOfPublicationsFactory.class);

        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
    }

    @Test
    void shouldCreateEmptyListOfPublication() {
        new MemoListOfPublicationsRepo();
    }

    @Test
    void addListOfPublicationsSuccessfully() {
        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);
        when(_createdDouble.getUser()).thenReturn(_user1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");
        when(_createdDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        ListOfPublications list = repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_user1Double, list.getUser()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_actionDouble, list.getGenre()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, repo.getListOfListOfPublications().size())
        );
    }

    @Test
    void cannotAddDuplicateList() {

        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "My List", _actionDouble);
        ListOfPublications duplicate = repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.getListOfListOfPublications().size());
    }

    @Test
    void getListReturnsCopy() {

        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        List<ListOfPublications> lists = repo.getListOfListOfPublications();

        // Assert
        assertAll(
                () -> assertEquals(1, lists.size()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> lists.add(new ListOfPublications(_user1Double, "Other List", _poetryDouble)))
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {

        // Arrange
        ListOfPublications _listPubDouble1 = mock(ListOfPublications.class);
        when(_listPubDouble1.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble1.getName()).thenReturn("List A");
        when(_listPubDouble1.getUser()).thenReturn(_user1Double);
        when(_listPubDouble1.isPrivate()).thenReturn(false);


        ListOfPublications _listPubDouble2 = mock(ListOfPublications.class);
        when(_listPubDouble2.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble2.getName()).thenReturn("List B");
        when(_listPubDouble2.getUser()).thenReturn(_user2Double);
        when(_listPubDouble2.isPrivate()).thenReturn(true);


        ListOfPublications _listPubDouble3 = mock(ListOfPublications.class);
        when(_listPubDouble3.getGenre()).thenReturn(_poetryDouble);
        when(_listPubDouble3.getName()).thenReturn("List C");
        when(_listPubDouble3.getUser()).thenReturn(_user1Double);
        when(_listPubDouble3.isPrivate()).thenReturn(false);

        when(_factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfPublications(_user2Double, "List B", _actionDouble)).thenReturn(_listPubDouble2);
        when(_factoryDouble.createListOfPublications(_user1Double, "List C", _poetryDouble)).thenReturn(_listPubDouble3);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "List A", _actionDouble);
        repo.addListOfPublications(_user2Double, "List B", _actionDouble);
        repo.addListOfPublications(_user1Double, "List C", _poetryDouble);

        List<ListOfPublications> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("List A", result.get(0).getName()),
                () -> assertEquals(_user1Double, result.get(0).getUser()),
                () -> assertEquals(_actionDouble, result.get(0).getGenre()),
                () -> assertFalse(result.get(0).isPrivate())
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnEmptyWhenNoPublicListsForThatGenre() {

        // Arrange
        ListOfPublications _listPubDouble = mock(ListOfPublications.class);
        when(_listPubDouble.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUser()).thenReturn(_user1Double);
        when(_listPubDouble.isPrivate()).thenReturn(true);

        when(_factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "List A", _actionDouble);
        List<ListOfPublications> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {

        // Arrange
        ListOfPublications _listPubDouble = mock(ListOfPublications.class);
        when(_listPubDouble.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble.getName()).thenReturn("List A");
        when(_listPubDouble.getUser()).thenReturn(_user1Double);
        when(_listPubDouble.isPrivate()).thenReturn(false);

        when(_factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(_listPubDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "List A", _actionDouble);
        List<ListOfPublications> result = repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(_listPubDouble));
    }

    @Test
    void findListsByUserShouldReturnOnlyListsOfThatUser() {

        // Arrange
        ListOfPublications _listPubDouble1 = mock(ListOfPublications.class);
        when(_listPubDouble1.getUser()).thenReturn(_user1Double);
        when(_listPubDouble1.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble1.getName()).thenReturn("U1 List");

        ListOfPublications _listPubDouble2 = mock(ListOfPublications.class);
        when(_listPubDouble2.getUser()).thenReturn(_user2Double);
        when(_listPubDouble2.getGenre()).thenReturn(_actionDouble);
        when(_listPubDouble2.getName()).thenReturn("U2 List");

        when(_factoryDouble.createListOfPublications(_user1Double, "U1 List", _actionDouble)).thenReturn(_listPubDouble1);
        when(_factoryDouble.createListOfPublications(_user2Double, "U2 List", _actionDouble)).thenReturn(_listPubDouble2);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "U1 List", _actionDouble);
        repo.addListOfPublications(_user2Double, "U2 List", _actionDouble);

        List<ListOfPublications> result = repo.findListsByUser(_user1Double);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("U1 List", result.get(0).getName()),
                () -> assertEquals(_user1Double, result.get(0).getUser())
        );
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnListWhenExists() {

        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);
        when(_createdDouble.getUser()).thenReturn(_user1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "My List", _actionDouble);
        ListOfPublications found = repo.findByOwnerNameAndGenre(_user1Double, "My List", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreIgnoresCase() {

        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);
        when(_createdDouble.getUser()).thenReturn(_user1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "My List", _actionDouble);
        ListOfPublications found = repo.findByOwnerNameAndGenre(_user1Double, "my list", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreTrimsName() {

        // Arrange
        ListOfPublications _createdDouble = mock(ListOfPublications.class);
        when(_createdDouble.getUser()).thenReturn(_user1Double);
        when(_createdDouble.getGenre()).thenReturn(_actionDouble);
        when(_createdDouble.getName()).thenReturn("My List");

        when(_factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(_createdDouble);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user1Double, "My List", _actionDouble);
        ListOfPublications found = repo.findByOwnerNameAndGenre(_user1Double, "  My List  ", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(_createdDouble, found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListDoesNotExist() {

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        ListOfPublications found = repo.findByOwnerNameAndGenre(_user1Double, "Unknown", _actionDouble);

        // Assert
        assertNull(found);
    }

    @Test
    void findByOwnerNameAndGenreReturnsNullWhenListExistsButDoesNotMatch() {

        // Arrange
        ListOfPublications list = mock(ListOfPublications.class);
        when(list.getUser()).thenReturn(_user2Double);
        when(list.getName()).thenReturn("Other List");
        when(list.getGenre()).thenReturn(_actionDouble);

        when(_factoryDouble.createListOfPublications(_user2Double, "Other List", _actionDouble))
                .thenReturn(list);

        // SUT
        MemoListOfPublicationsRepo repo = new MemoListOfPublicationsRepo(_factoryDouble);

        // Act
        repo.addListOfPublications(_user2Double, "Other List", _actionDouble);
        ListOfPublications result = repo.findByOwnerNameAndGenre(_user1Double, "My List", _actionDouble);

        // Assert
        assertNull(result);
    }
}