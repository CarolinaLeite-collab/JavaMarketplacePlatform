package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfPublicationsRepoTest {

    private ListOfPublicationsFactory factoryDouble;

    private User _user1Double;
    private User _user2Double;
    private Genre _actionDouble;
    private Genre _poetryDouble;
    private ListOfPublicationsRepo _repo;

    @BeforeEach
    void setUp() {
        factoryDouble = mock(ListOfPublicationsFactory.class);
        _repo = new ListOfPublicationsRepo(factoryDouble);

        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

        _user1Double = mock(User.class);
        _user2Double = mock(User.class);
    }

    @Test
    void addListOfPublicationsSuccessfully() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);

        // Act
        ListOfPublications list = _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_user1Double, list.getUser()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_actionDouble, list.getGenre()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, _repo.getListOfListOfPublications().size())
        );
    }

    @Test
    void cannotAddDuplicateList() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);

        _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        ListOfPublications duplicate = _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Assert
        assertNull(duplicate);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void addNullListShouldThrow() {
        // Arrange
        when(factoryDouble.createListOfPublications(null, "My List", _actionDouble)).thenThrow(new IllegalArgumentException("User is mandatory"));
        when(factoryDouble.createListOfPublications(_user1Double, null, _actionDouble)).thenThrow(new IllegalArgumentException("Name is mandatory"));
        when(factoryDouble.createListOfPublications(_user1Double, "My List", null)).thenThrow(new IllegalArgumentException("Genre is mandatory"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> _repo.addListOfPublications(null, "My List", _actionDouble));
        assertThrows(IllegalArgumentException.class, () -> _repo.addListOfPublications(_user1Double, null, _actionDouble));
        assertThrows(IllegalArgumentException.class, () -> _repo.addListOfPublications(_user1Double, "My List", null));
    }

    @Test
    void getListReturnsCopy() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);
        _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        List<ListOfPublications> lists = _repo.getListOfListOfPublications();

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
        ListOfPublications a = new ListOfPublications(_user1Double, "List A", _actionDouble);
        ListOfPublications b = new ListOfPublications(_user2Double, "List B", _actionDouble);
        ListOfPublications c = new ListOfPublications(_user1Double, "List C", _poetryDouble);

        a.makePublic();
        c.makePublic();

        when(factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(a);
        when(factoryDouble.createListOfPublications(_user2Double, "List B", _actionDouble)).thenReturn(b);
        when(factoryDouble.createListOfPublications(_user1Double, "List C", _poetryDouble)).thenReturn(c);

        _repo.addListOfPublications(_user1Double, "List A", _actionDouble);
        _repo.addListOfPublications(_user2Double, "List B", _actionDouble);
        _repo.addListOfPublications(_user1Double, "List C", _poetryDouble);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_actionDouble);

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
        ListOfPublications a = new ListOfPublications(_user1Double, "List A", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(a);
        _repo.addListOfPublications(_user1Double, "List A", _actionDouble);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {
        // Arrange
        ListOfPublications a = new ListOfPublications(_user1Double, "List A", _actionDouble);
        a.makePublic();
        when(factoryDouble.createListOfPublications(_user1Double, "List A", _actionDouble)).thenReturn(a);
        _repo.addListOfPublications(_user1Double, "List A", _actionDouble);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_actionDouble);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(a));
    }

    @Test
    void findPublicListsByGenre_nullGenre_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> _repo.findPublicListsByGenre(null));
    }

    @Test
    void findListsByUserShouldReturnOnlyListsOfThatUser() {
        // Arrange
        ListOfPublications u1 = new ListOfPublications(_user1Double, "U1 List", _actionDouble);
        ListOfPublications u2 = new ListOfPublications(_user2Double, "U2 List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "U1 List", _actionDouble)).thenReturn(u1);
        when(factoryDouble.createListOfPublications(_user2Double, "U2 List", _actionDouble)).thenReturn(u2);

        _repo.addListOfPublications(_user1Double, "U1 List", _actionDouble);
        _repo.addListOfPublications(_user2Double, "U2 List", _actionDouble);

        // Act
        List<ListOfPublications> result = _repo.findListsByUser(_user1Double);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("U1 List", result.get(0).getName()),
                () -> assertEquals(_user1Double, result.get(0).getUser())
        );
    }

    @Test
    void findListsByUser_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> _repo.findListsByUser(null));
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnListWhenExists() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);
        _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1Double, "My List", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_ignoresCase() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);
        _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1Double, "my list", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_trimsName() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1Double, "My List", _actionDouble);
        when(factoryDouble.createListOfPublications(_user1Double, "My List", _actionDouble)).thenReturn(created);
        _repo.addListOfPublications(_user1Double, "My List", _actionDouble);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1Double, "  My List  ", _actionDouble);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_returnsNullWhenListDoesNotExist() {
        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1Double, "Unknown", _actionDouble);

        // Assert
        assertNull(found);
    }

    @Test
    void findByOwnerNameAndGenre_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(null, "My List", _actionDouble));
    }

    @Test
    void findByOwnerNameAndGenre_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1Double, null, _actionDouble));
    }

    @Test
    void findByOwnerNameAndGenre_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1Double, "   ", _actionDouble));
    }

    @Test
    void findByOwnerNameAndGenre_nullGenre_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1Double, "My List", null));
    }
}