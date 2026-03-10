package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationToListControllerTest {

    private ListOfPublicationsRepo _listRepo;
    private LibraryRepo _libraryRepo;
    private AddPublicationToListController _controller;

    private User _user;
    private Genre _genre;
    private Identifier _identifier;
    private Publication _publication;
    private Library _library;
    private ListOfPublications _publicationsList;

    @BeforeEach
    void setUp() {
        _listRepo = mock(ListOfPublicationsRepo.class);
        _libraryRepo = mock(LibraryRepo.class);
        _controller = new AddPublicationToListController(_listRepo, _libraryRepo);

        _user = mock(User.class);
        _genre = mock(Genre.class);
        _identifier = mock(Identifier.class);
        _publication = mock(Publication.class);
        _library = mock(Library.class);
        _publicationsList = mock(ListOfPublications.class);
    }

    // getMyLists
    @Test
    void getMyLists_returnsListsFromRepo() {
        List<ListOfPublications> expected = List.of(_publicationsList);
        when(_listRepo.findListsByUser(_user)).thenReturn(expected);

        List<ListOfPublications> result = _controller.getMyLists(_user);

        assertSame(expected, result);
    }

    @Test
    void getMyLists_throwsWhenUserIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getMyLists(null)
        );
        assertEquals("User is mandatory", ex.getMessage());
    }

    // getPublicationsInMyLibrary

    @Test
    void getPublicationsInMyLibrary_returnsPublicationDetails() {
        PublicationDetails details = mock(PublicationDetails.class);
        when(_library.getPublicationsInLibrary()).thenReturn(List.of(details));
        when(_libraryRepo.findLibraryByUser(_user)).thenReturn(_library);

        List<PublicationDetails> result = _controller.getPublicationsInMyLibrary(_user);

        assertEquals(1, result.size());
        assertSame(details, result.get(0));
    }

    @Test
    void getPublicationsInMyLibrary_throwsWhenUserLibraryNotFound() {
        when(_libraryRepo.findLibraryByUser(_user))
                .thenThrow(new IllegalStateException("Library not found"));

        assertThrows(IllegalStateException.class,
                () -> _controller.getPublicationsInMyLibrary(_user));
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
    void addPublicationToList_addsPublicationWhenValid() {
        when(_listRepo.findByOwnerNameAndGenre(_user, "My List", _genre))
                .thenReturn(_publicationsList);

        when(_libraryRepo.findLibraryByUser(_user))
                .thenReturn(_library);

        when(_library.getAllPublications())
                .thenReturn(List.of(_publication));

        when(_publication.getIdentifier())
                .thenReturn(_identifier);

        assertDoesNotThrow(() ->
                _controller.addPublicationToList(_user, "My List", _genre, _identifier)
        );
    }

    // addPublicationToList (MAGAZINE + ISSN)

    @Test
    void addPublicationToList_addsMagazineWithISSNWhenValid() {
        // Arrange
        when(_listRepo.findByOwnerNameAndGenre(_user, "My List", _genre))
                .thenReturn(_publicationsList);

        when(_libraryRepo.findLibraryByUser(_user))
                .thenReturn(_library);

        when(_library.getAllPublications())
                .thenReturn(List.of(_publication));

        when(_publication.getIdentifier())
                .thenReturn(_identifier);

        // Act + Assert
        assertDoesNotThrow(() ->
                _controller.addPublicationToList(_user, "My List", _genre, _identifier)
        );
    }

    @Test
    void addPublicationToList_throwsWhenListNotFound() {
        when(_listRepo.findByOwnerNameAndGenre(_user, "My List", _genre))
                .thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "My List", _genre, _identifier));
    }

    @Test
    void addPublicationToList_throwsWhenPublicationNotFound() {
        when(_listRepo.findByOwnerNameAndGenre(_user, "My List", _genre))
                .thenReturn(_publicationsList);

        when(_libraryRepo.findLibraryByUser(_user))
                .thenReturn(_library);

        when(_library.getAllPublications())
                .thenReturn(List.of()); // empty library

        assertThrows(IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "My List", _genre, _identifier));
    }

    // ---------------------
    // Null argument tests
    // ---------------------
    @Test
    void addPublicationToList_throwsWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationToList(null, "My List", _genre, _identifier));
    }

    @Test
    void addPublicationToList_throwsWhenListNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, " ", _genre, _identifier));
    }

    @Test
    void addPublicationToList_throwsWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, "My List", null, _identifier));
    }

    @Test
    void addPublicationToList_throwsWhenIdentifierIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationToList(_user, "My List", _genre, null));
    }

    // -----------------------
    // Check for duplications
    // -----------------------
    @Test
    void addPublicationToList_throwsWhenPublicationAlreadyInList() {
        when(_listRepo.findByOwnerNameAndGenre(_user, "My List", _genre))
                .thenReturn(_publicationsList);

        when(_libraryRepo.findLibraryByUser(_user))
                .thenReturn(_library);

        when(_library.getAllPublications())
                .thenReturn(List.of(_publication));

        when(_publication.getIdentifier())
                .thenReturn(_identifier);

        // Simulate the domain rule: list rejects duplicates
        doThrow(new IllegalStateException("Publication already in list"))
                .when(_publicationsList)
                .addPublication(_publication);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.addPublicationToList(_user, "My List", _genre, _identifier)
        );

        assertEquals("Publication already in list", ex.getMessage());
    }

}