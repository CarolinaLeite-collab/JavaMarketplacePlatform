package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsRepoTest {

    private ListOfPublicationsFactory factory;

    private User _user1;
    private User _user2;
    private Genre _action;
    private Genre _poetry;
    private ListOfPublicationsRepo _repo;

    @BeforeEach
    void setUp() {
        factory = new ListOfPublicationsFactory();
        _repo = new ListOfPublicationsRepo();

        _action = new Genre("Action");
        _poetry = new Genre("Poetry");

        _user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));
        _user2 = new User(new Name("User Two"), new Email("user2@mail.com"));
    }

    @Test
    void addListOfPublicationsSuccessfully() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);

        // Act
        ListOfPublications list = _repo.addListOfPublications(created);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_user1, list.getUser()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_action, list.getGenre()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, _repo.getListOfListOfPublications().size())
        );
    }

    @Test
    void cannotAddDuplicateList() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);
        _repo.addListOfPublications(created);

        // Act
        ListOfPublications duplicate = _repo.addListOfPublications(created);

        // Assert
        assertNull(duplicate);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void addNullListShouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> _repo.addListOfPublications(null));
    }

    @Test
    void getListReturnsCopy() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);
        _repo.addListOfPublications(created);

        // Act
        List<ListOfPublications> lists = _repo.getListOfListOfPublications();

        // Assert
        assertAll(
                () -> assertEquals(1, lists.size()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> lists.add(factory.createListOfPublications(_user1, "Other List", _poetry)))
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {
        // Arrange
        ListOfPublications a = factory.createListOfPublications(_user1, "List A", _action);
        ListOfPublications b = factory.createListOfPublications(_user2, "List B", _action);
        ListOfPublications c = factory.createListOfPublications(_user1, "List C", _poetry);

        a.makePublic();
        c.makePublic();

        _repo.addListOfPublications(a);
        _repo.addListOfPublications(b);
        _repo.addListOfPublications(c);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("List A", result.get(0).getName()),
                () -> assertEquals(_user1, result.get(0).getUser()),
                () -> assertEquals(_action, result.get(0).getGenre()),
                () -> assertFalse(result.get(0).isPrivate())
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnEmptyWhenNoPublicListsForThatGenre() {
        // Arrange
        ListOfPublications a = factory.createListOfPublications(_user1, "List A", _action);
        _repo.addListOfPublications(a);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {
        // Arrange
        ListOfPublications a = factory.createListOfPublications(_user1, "List A", _action);
        a.makePublic();
        _repo.addListOfPublications(a);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

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
        _repo.addListOfPublications(factory.createListOfPublications(_user1, "U1 List", _action));
        _repo.addListOfPublications(factory.createListOfPublications(_user2, "U2 List", _action));

        // Act
        List<ListOfPublications> result = _repo.findListsByUser(_user1);

        // Assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("U1 List", result.get(0).getName()),
                () -> assertEquals(_user1, result.get(0).getUser())
        );
    }

    @Test
    void findListsByUser_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> _repo.findListsByUser(null));
    }

    @Test
    void findByOwnerNameAndGenreShouldReturnListWhenExists() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);
        _repo.addListOfPublications(created);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "My List", _action);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_ignoresCase() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);
        _repo.addListOfPublications(created);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "my list", _action);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_trimsName() {
        // Arrange
        ListOfPublications created = factory.createListOfPublications(_user1, "My List", _action);
        _repo.addListOfPublications(created);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "  My List  ", _action);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_returnsNullWhenListDoesNotExist() {
        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "Unknown", _action);

        // Assert
        assertNull(found);
    }

    @Test
    void findByOwnerNameAndGenre_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(null, "My List", _action));
    }

    @Test
    void findByOwnerNameAndGenre_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1, null, _action));
    }

    @Test
    void findByOwnerNameAndGenre_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1, "   ", _action));
    }

    @Test
    void findByOwnerNameAndGenre_nullGenre_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> _repo.findByOwnerNameAndGenre(_user1, "My List", null));
    }
}