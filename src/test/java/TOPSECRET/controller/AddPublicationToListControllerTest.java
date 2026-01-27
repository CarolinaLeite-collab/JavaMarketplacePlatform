package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddPublicationToListControllerTest {

    private ListOfPublicationsRepo _listRepo;
    private LibraryRepo _libraryRepo;
    private AddPublicationToListController _controller;

    private UserRepo _userRepo;
    private GenreRepo _genreRepo;

    private User _user;
    private Genre _action;
    private Library _library;

    @BeforeEach
    void setUp() {
        _listRepo = new ListOfPublicationsRepo();
        _libraryRepo = new LibraryRepo();
        _controller = new AddPublicationToListController(_listRepo, _libraryRepo);

        _userRepo = new UserRepo();
        _genreRepo = new GenreRepo();

        _user = _userRepo.registerNewUser("User One", "user1@mail.com");

        _action = _genreRepo.addGenre("Action");
        assertNotNull(_action);

        _library = _libraryRepo.createMyLibrary(_user);

        ListOfPublications myList = _listRepo.createListOfPublications(_user, "My List", _action);
        assertNotNull(myList);
    }

    // getMyLists

    @Test
    void getMyListsShouldReturnOnlyListsOfThatUser() {
        User user2 = _userRepo.registerNewUser("User Two", "user2@mail.com");
        _listRepo.createListOfPublications(user2, "Other List", _action);

        List<ListOfPublications> result = _controller.getMyLists(_user);

        assertEquals(1, result.size());
        assertEquals("My List", result.get(0).getName());
        assertEquals(_user, result.get(0).getUser());
    }

    @Test
    void getMyListsShouldThrowWhenUserIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getMyLists(null)
        );
        assertEquals("User is mandatory", ex.getMessage());
    }

    // getPublicationsInMyLibrary

    @Test
    void getPublicationsInMyLibraryShouldReturnPublicationDetails() {
        Identifier id = new ISBN("978-0-306-40615-7"); // VALID ISBN-13

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(id)
                .year(Year.of(2000))
                .title(new Title("Some Title"))
                .author(new Author("Some Author"))
                .publisher(new Publisher("Some Publisher"))
                .genre(_action)
                .build();

        assertTrue(_library.addPublicationToLibrary(pub));

        List<PublicationDetails> details = _controller.getPublicationsInMyLibrary(_user);

        assertEquals(1, details.size());
        assertEquals(id, details.get(0).getIdentifier());
        assertEquals(pub.getTitle(), details.get(0).getTitle());
        assertEquals(pub.getPublicationType(), details.get(0).getPublicationType());
    }

    @Test
    void getPublicationsInMyLibraryShouldThrowWhenUserHasNoLibrary() {
        User userNoLib = _userRepo.registerNewUser("User Three", "user3@mail.com");

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.getPublicationsInMyLibrary(userNoLib)
        );
        assertTrue(ex.getMessage().startsWith("Library not found for user:"));
    }

    @Test
    void getPublicationsInMyLibraryShouldThrowWhenUserIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getPublicationsInMyLibrary(null)
        );
        assertEquals("User is mandatory", ex.getMessage());
    }

    // addPublicationToList (BOOK + ISBN)

    @Test
    void addPublicationToListShouldAddBookWithISBNWhenEverythingIsValid() {
        Identifier id = new ISBN("0306406152"); // VALID ISBN-10

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(id)
                .year(Year.of(2001))
                .title(new Title("Book A"))
                .author(new Author("Author A"))
                .publisher(new Publisher("Publisher A"))
                .genre(_action)
                .build();

        assertTrue(_library.addPublicationToLibrary(pub));

        assertDoesNotThrow(() ->
                _controller.addPublicationToList(_user, "My List", _action, id)
        );

        ListOfPublications list = _listRepo.findByOwnerNameAndGenre(_user, "My List", _action);
        assertNotNull(list);

        assertEquals(1, list.getPublications().size());
        assertEquals(id, list.getPublications().get(0).getIdentifier());
    }

    // addPublicationToList (MAGAZINE + ISSN)

    @Test
    void addPublicationToListShouldAddMagazineWithISSNWhenEverythingIsValid() {
        Identifier id = new ISSN("1234-5678"); // VALID FORMAT for your ISSN

        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(id)
                .year(Year.of(2020))
                .title(new Title("Magazine A"))
                .publisher(new Publisher("Publisher M"))
                .genre(_action)
                .build();

        assertTrue(_library.addPublicationToLibrary(pub));

        assertDoesNotThrow(() ->
                _controller.addPublicationToList(_user, "My List", _action, id)
        );

        ListOfPublications list = _listRepo.findByOwnerNameAndGenre(_user, "My List", _action);
        assertNotNull(list);

        assertEquals(1, list.getPublications().size());
        assertEquals(id, list.getPublications().get(0).getIdentifier());
    }

    // Negative cases

    @Test
    void addPublicationToListShouldThrowWhenListNotFound() {
        Identifier id = new ISBN("9780306406157"); // VALID ISBN-13

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(id)
                .year(Year.of(2002))
                .title(new Title("Book B"))
                .author(new Author("Author B"))
                .publisher(new Publisher("Publisher B"))
                .genre(_action)
                .build();

        assertTrue(_library.addPublicationToLibrary(pub));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "Unknown", _action, id)
        );
        assertEquals("List not found", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenPublicationNotInLibrary() {
        Identifier id = new ISBN("9780306406157"); // not added

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "My List", _action, id)
        );
        assertEquals("Publication not found in library", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenIdentifierIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, "My List", _action, null)
        );
        assertEquals("Identifier is mandatory", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenListNameIsBlank() {
        Identifier id = new ISSN("1234-5678");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, "   ", _action, id)
        );
        assertEquals("List name is mandatory", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenGenreIsNull() {
        Identifier id = new ISBN("0306406152");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, "My List", null, id)
        );
        assertEquals("Genre is mandatory", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenUserIsNull() {
        Identifier id = new ISBN("0306406152");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.addPublicationToList(null, "My List", _action, id)
        );
        assertEquals("User is mandatory", ex.getMessage());
    }

    @Test
    void addPublicationToListShouldThrowWhenAddingSamePublicationTwice() {
        Identifier id = new ISBN("0306406152"); // VALID ISBN-10

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(id)
                .year(Year.of(2003))
                .title(new Title("Book C"))
                .author(new Author("Author C"))
                .publisher(new Publisher("Publisher C"))
                .genre(_action)
                .build();

        assertTrue(_library.addPublicationToLibrary(pub));

        _controller.addPublicationToList(_user, "My List", _action, id);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "My List", _action, id)
        );
        assertEquals("Publication already in list", ex.getMessage());
    }
}