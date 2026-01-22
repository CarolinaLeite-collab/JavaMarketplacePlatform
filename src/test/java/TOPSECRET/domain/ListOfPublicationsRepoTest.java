package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsRepoTest {

    private User _user1;
    private User _user2;
    private Genre _action;
    private Genre _poetry;
    private GenreRepo _genreRepo;
    private ListOfPublicationsRepo _repo;
    private UserRepo _userRepo;

    @BeforeEach
    void setUp() {
        _genreRepo = new GenreRepo();
        _userRepo = new UserRepo();
        _repo = new ListOfPublicationsRepo();

        _action = _genreRepo.addGenre("Action");
        _poetry = _genreRepo.addGenre("Poetry");

        assertNotNull(_action);
        assertNotNull(_poetry);


        _user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));
        _user2 = _userRepo.registerNewUser("User Two", "user2@mail.com");
    }

    @Test
    void createListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = _repo.createListOfPublications(_user1, "My List", _action);

        // Assert
        assertNotNull(list);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void cannotCreateDuplicateList() {
        // Arrange
        _repo.createListOfPublications(_user1, "My List", _action);

        // Act
        ListOfPublications duplicate = _repo.createListOfPublications(_user1, "My List", _action);

        // Assert
        assertNull(duplicate);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void createListWithNullsShouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(null, "Name", _action));
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(_user1, null, _action));
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(_user1, "Name", null));
    }

    @Test
    void getListReturnsCopy() {
        // Arrange
        _repo.createListOfPublications(_user1, "My List", _action);

        // Act
        var lists = _repo.getListOfListOfPublications();

        // Assert
        assertEquals(1, lists.size());
        assertThrows(UnsupportedOperationException.class, () -> lists.add(new ListOfPublications(_user1, "Other List", _poetry)));
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {
        ListOfPublications a = _repo.createListOfPublications(_user1, "List A", _action);
        ListOfPublications b = _repo.createListOfPublications(_user2, "List B", _action);
        ListOfPublications c = _repo.createListOfPublications(_user1, "List C", _poetry);

        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(c);

        a.makePublic(); // public + Action
        // b stays private
        c.makePublic(); // public but Poetry

        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getName());
        assertEquals(_user1, result.get(0).getUser());
        assertEquals(_action, result.get(0).getGenre());
        assertFalse(result.get(0).isPrivate());
    }

    @Test
    void findPublicListsByGenreShouldReturnEmptyWhenNoPublicListsForThatGenre() {
        ListOfPublications a = _repo.createListOfPublications(_user1, "List A", _action);
        assertNotNull(a);
        assertTrue(a.isPrivate()); // default

        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {
        ListOfPublications a = _repo.createListOfPublications(_user1, "List A", _action);
        assertNotNull(a);
        a.makePublic();

        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        assertThrows(UnsupportedOperationException.class, () -> result.add(a));
    }
}