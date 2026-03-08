package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListOfPublicationsRepoTest {

    @Mock
    private ListOfPublicationsFactory factory;

    private User _user1;
    private User _user2;
    private Genre _action;
    private Genre _poetry;
    private ListOfPublicationsRepo _repo;

    @BeforeEach
    void setUp() {
        _repo = new ListOfPublicationsRepo(factory);

        _action = new Genre("Action");
        _poetry = new Genre("Poetry");

        _user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));
        _user2 = new User(new Name("User Two"), new Email("user2@mail.com"));
    }

    @Test
    void createListSuccessfully() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        when(factory.createListOfPublications(_user1, "My List", _action)).thenReturn(created);

        // Act
        ListOfPublications list = _repo.createListOfPublications(_user1, "My List", _action);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(_user1, list.getUser()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(_action, list.getGenre()),
                () -> assertTrue(list.isPrivate()),
                () -> assertEquals(1, _repo.getListOfListOfPublications().size())
        );

        verify(factory).createListOfPublications(_user1, "My List", _action);
    }

    @Test
    void cannotCreateDuplicateList() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        when(factory.createListOfPublications(_user1, "My List", _action)).thenReturn(created);

        _repo.createListOfPublications(_user1, "My List", _action);

        // Act
        ListOfPublications duplicate = _repo.createListOfPublications(_user1, "My List", _action);

        // Assert
        assertNull(duplicate);
        assertEquals(1, _repo.getListOfListOfPublications().size());
        verify(factory, times(2)).createListOfPublications(_user1, "My List", _action);
    }

    @Test
    void createListWithNullsShouldThrow() {
        // Arrange
        when(factory.createListOfPublications(null, "Name", _action))
                .thenThrow(new IllegalArgumentException("User is mandatory"));
        when(factory.createListOfPublications(_user1, null, _action))
                .thenThrow(new IllegalArgumentException("Name is mandatory"));
        when(factory.createListOfPublications(_user1, "Name", null))
                .thenThrow(new IllegalArgumentException("Genre is mandatory"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(null, "Name", _action));
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(_user1, null, _action));
        assertThrows(IllegalArgumentException.class, () -> _repo.createListOfPublications(_user1, "Name", null));
    }

    @Test
    void getListReturnsCopy() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        when(factory.createListOfPublications(_user1, "My List", _action)).thenReturn(created);

        _repo.createListOfPublications(_user1, "My List", _action);

        // Act
        List<ListOfPublications> lists = _repo.getListOfListOfPublications();

        // Assert
        assertAll(
                () -> assertEquals(1, lists.size()),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> lists.add(new ListOfPublications(_user1, "Other List", _poetry)))
        );
    }

    @Test
    void findPublicListsByGenreShouldReturnOnlyPublicListsOfThatGenre() {
        // Arrange
        ListOfPublications a = new ListOfPublications(_user1, "List A", _action);
        ListOfPublications b = new ListOfPublications(_user2, "List B", _action);
        ListOfPublications c = new ListOfPublications(_user1, "List C", _poetry);

        a.makePublic();
        c.makePublic();

        _repo.addListOnRepo(a);
        _repo.addListOnRepo(b);
        _repo.addListOnRepo(c);

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
        ListOfPublications a = new ListOfPublications(_user1, "List A", _action);
        _repo.addListOnRepo(a);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicListsByGenreShouldReturnImmutableCopy() {
        // Arrange
        ListOfPublications a = new ListOfPublications(_user1, "List A", _action);
        a.makePublic();
        _repo.addListOnRepo(a);

        // Act
        List<ListOfPublications> result = _repo.findPublicListsByGenre(_action);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(a));
    }

    @Test
    void returnFalseWhenListIsNull() {
        // Act
        boolean result = _repo.addListOnRepo(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void storeListAndReturnTrueWhenListIsValid() {
        // Arrange
        ListOfPublications list1 = new ListOfPublications(_user1, "MyActionList", _action);

        // Act
        boolean result = _repo.addListOnRepo(list1);

        // Assert
        assertTrue(result);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void findListsByUserShouldReturnOnlyListsOfThatUser() {
        // Arrange
        _repo.addListOnRepo(new ListOfPublications(_user1, "U1 List", _action));
        _repo.addListOnRepo(new ListOfPublications(_user2, "U2 List", _action));

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
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        _repo.addListOnRepo(created);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "My List", _action);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_ignoresCase() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        _repo.addListOnRepo(created);

        // Act
        ListOfPublications found = _repo.findByOwnerNameAndGenre(_user1, "my list", _action);

        // Assert
        assertNotNull(found);
        assertEquals(created, found);
    }

    @Test
    void findByOwnerNameAndGenre_trimsName() {
        // Arrange
        ListOfPublications created = new ListOfPublications(_user1, "My List", _action);
        _repo.addListOnRepo(created);

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